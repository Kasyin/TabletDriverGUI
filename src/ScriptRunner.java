import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

import static java.lang.Math.floor;

public class ScriptRunner {
    private static final int id = getTabletId();
    private static final int SCALE_MULTIPLIER = 200;

    public static Hashtable<String, String> getMonitors() {
        String CommandgetMonitorName = "xrandr --listactivemonitors | grep \"^ \" | cut -d \" \" -f 6";
        String CommandgetMonitorid = "xrandr -q | grep \" connected\" | grep -Eo \"[0-9]+[x][0-9]+[+][0-9]+[+][0-9]+\"";
        String[] names = executeBashCommand(CommandgetMonitorName);
        String[] ids = executeBashCommand(CommandgetMonitorid);
        Hashtable<String, String> ret = new Hashtable<String, String>();
        for (int i = 0; i < names.length; i++) {
            ret.put(names[i], ids[i]);
        }
        return ret;
    }

    public static Hashtable<String, Double> getMonitorRatio() {
        String CommandgetMonitorName = "xrandr --listactivemonitors | grep \"^ \" | cut -d \" \" -f 6";
        String CommandgetMonitorRatio = "xrandr -q | grep \" connected\" | grep -Eo \"[0-9]+[x][0-9]+\"";
        String[] names = executeBashCommand(CommandgetMonitorName);
        String[] ratios = executeBashCommand(CommandgetMonitorRatio);
        Hashtable<String, Double> ret = new Hashtable<>();
        for (int i = 0; i < names.length; i++) {
            String[] stringRatios = ratios[i].split("x");
            double ratio = Double.parseDouble(stringRatios[0]) / Double.parseDouble(stringRatios[1]);
            ret.put(names[i], ratio);
        }
        return ret;
    }

    public static int getTabletId() {
        String CommandgetId = "xsetwacom --list | grep \"STYLUS\" | cut -f 2 | cut -d ' ' -f 2 | tr -d \"\\r\"";
        String[] id = executeBashCommand(CommandgetId);
        return Integer.parseInt(id[0]);
    }

    public static Double[] getFullArea() {
        String CommandSetFullArea = "xsetwacom set " + id + " area -1 -1 -1 -1";
        String CommandGetFullArea = "xsetwacom get " + id + " area";
        executeBashCommand(CommandSetFullArea);
        String[] fullAreaString = executeBashCommand(CommandGetFullArea);
        fullAreaString = fullAreaString[0].split(" ");
        Double[] ret = new Double[2];
        ret[0] = Double.parseDouble(fullAreaString[2])/SCALE_MULTIPLIER;
        ret[1] = Double.parseDouble(fullAreaString[3])/SCALE_MULTIPLIER;
        return ret;
    }

    public static Double[] getCurrentArea() {
        String CommandGetCurrentArea = "xsetwacom get " + id + " area";
        String[] fullAreaString = executeBashCommand(CommandGetCurrentArea);
        fullAreaString = fullAreaString[0].split(" ");
        Double[] ret = new Double[fullAreaString.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = Double.parseDouble(fullAreaString[i])/SCALE_MULTIPLIER;
        }
        return ret;
    }

    public static void setArea(TabletInfo tabletInfo) {
        int xHighDx = (int) floor(tabletInfo.getAreaX()*SCALE_MULTIPLIER + tabletInfo.getAreaWidth()*SCALE_MULTIPLIER);
        int yHighDx = (int) floor(tabletInfo.getAreaY()*SCALE_MULTIPLIER + tabletInfo.getAreaHeight()*SCALE_MULTIPLIER);
        String CommandSetArea = "xsetwacom set " + id + " area " + (int) floor(tabletInfo.getAreaX()*SCALE_MULTIPLIER) + " " + (int) floor(tabletInfo.getAreaY()*SCALE_MULTIPLIER) + " " + xHighDx+ " " + yHighDx;
        executeBashCommand(CommandSetArea);
    }

    public static void setMonitor(TabletInfo tabletInfo) {
        String ChangeMonitorCommand = "xsetwacom set " + id + " MapToOutput " + tabletInfo.getMonitors().get(tabletInfo.getActiveMonitor());
        executeBashCommand(ChangeMonitorCommand);
    }

    private static String[] toArray(ArrayList<String> listwrap) {
        String[] ret = new String[listwrap.size()];
        for(int i = 0; i < listwrap.size(); i++) {
            ret[i] = listwrap.get(i);
        }
        return ret;
    }

    public static String[] executeBashCommand(String command) {
        //System.out.println("Executing BASH command:\n   " + command);
        Runtime r = Runtime.getRuntime();
        // Use bash -c so we can handle things like multi commands separated by ; and
        // things like quotes, $, |, and \. My tests show that command comes as
        // one argument to bash, so we do not need to quote it to make it one thing.
        // Also, exec may object if it does not have an executable file as the first thing,
        // so having bash here makes it happy provided bash is installed and in path.
        String[] commands = {"bash", "-c", command};
        String[] ret = {};
        try {
            Process p = r.exec(commands);

            p.waitFor();
            BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            ArrayList<String> listwrap = new ArrayList<String>();
            while ((line = b.readLine()) != null) {
                listwrap.add(line);
            }
            ret = toArray(listwrap);
            b.close();
        } catch (Exception e) {
            System.err.println("Failed to execute bash with command: " + command);
            e.printStackTrace();
        }
        return ret;
    }
}