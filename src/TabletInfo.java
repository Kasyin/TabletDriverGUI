import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.Hashtable;

public class TabletInfo {
    private double fullAreaWidth = 0.0;
    private double fullAreaHeight = 0.0;
    private Hashtable<String, String> monitors = null;
    private Hashtable<String, Double> monitorsRatio = null;

    private double areaWidth = 0.0;
    private double areaHeight = 0.0;
    private double areaX = 0.0;
    private double areaY = 0.0;
    private double ratio = 0.0;

    private String activeMonitor = "";

    private boolean fullarea = false;
    private boolean forceAspectRatio = false;

    public TabletInfo(String filename) throws IOException {
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
        TabletInfo attributes = gson.fromJson(br , new TypeToken<TabletInfo>() {}.getType());
        br.close();

        this.fullAreaWidth = attributes.getFullAreaWidth();
        this.fullAreaHeight = attributes.getFullAreaHeight();
        this.areaWidth = attributes.getAreaWidth();
        this.areaHeight = attributes.getAreaHeight();
        this.areaX = attributes.getAreaX();
        this.areaY = attributes.getAreaY();
        this.monitors = attributes.getMonitors();
        this.monitorsRatio = attributes.getMonitorsRatio();
        this.activeMonitor = attributes.getActiveMonitor();
        this.fullarea = attributes.isFullarea();
        this.forceAspectRatio = attributes.isForceAspectRatio();
    }

    public TabletInfo() {
        Double[] fullArea = ScriptRunner.getFullArea();

        this.fullAreaWidth = fullArea[0];
        this.fullAreaHeight = fullArea[1];
        monitors = ScriptRunner.getMonitors();
        monitorsRatio = ScriptRunner.getMonitorRatio();

        this.areaWidth = fullAreaWidth;
        this.areaHeight = fullAreaHeight;
        this.areaX = 0.0;
        this.areaY = 0.0;
        this.activeMonitor = monitors.keys().nextElement();
        this.fullarea = false;
        this.forceAspectRatio = false;
    }

    public TabletInfo(TabletInfo tabletInfo) {
        this.fullAreaWidth = tabletInfo.getFullAreaWidth();
        this.fullAreaHeight = tabletInfo.getFullAreaHeight();
        this.areaWidth = tabletInfo.getAreaWidth();
        this.areaHeight = tabletInfo.getAreaHeight();
        this.areaX = tabletInfo.getAreaX();
        this.areaY = tabletInfo.getAreaY();
        this.monitors = tabletInfo.getMonitors();
        this.monitorsRatio = tabletInfo.getMonitorsRatio();
        this.activeMonitor = tabletInfo.getActiveMonitor();
        this.fullarea = tabletInfo.isFullarea();
        this.forceAspectRatio = tabletInfo.isForceAspectRatio();
    }

    public boolean isFullarea() {
        return fullarea;
    }

    public void setFullarea(boolean fullarea) {
        this.fullarea = fullarea;
    }

    public boolean isForceAspectRatio() {
        return forceAspectRatio;
    }

    public void setForceAspectRatio(boolean forceAspectRatio) {
        this.forceAspectRatio = forceAspectRatio;
    }

    public Hashtable<String, Double> getMonitorsRatio() {
        return monitorsRatio;
    }

    public void setMonitorsRatio(Hashtable<String, Double> monitorsRatio) {
        this.monitorsRatio = monitorsRatio;
    }

    public Hashtable<String, String> getMonitors() {
        return monitors;
    }

    public void setMonitors(Hashtable<String, String> monitors) {
        this.monitors = monitors;
    }

    public String getActiveMonitor() {
        return activeMonitor;
    }

    public void setActiveMonitor(String activeMonitor) {
        this.activeMonitor = activeMonitor;
    }

    public Double getFullAreaWidth() {
        return fullAreaWidth;
    }

    public void setFullAreaWidth(Double fullAreaWidth) {
        this.fullAreaWidth = fullAreaWidth;
    }

    public Double getFullAreaHeight() {
        return fullAreaHeight;
    }

    public void setFullAreaHeight(Double fullAreaHeight) {
        this.fullAreaHeight = fullAreaHeight;
    }

    public Double getAreaWidth() {
        return areaWidth;
    }

    public Double getAreaHeight() {
        return areaHeight;
    }

    public Double getAreaX() {
        return areaX;
    }

    public Double getAreaY() {
        return areaY;
    }

    public double getRatio() {
        return ratio;
    }
    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public void setAreaWidth(Double areaWidth) {
        if (areaWidth < 0)
            areaWidth = 0.0;
        while (areaWidth + areaX > fullAreaWidth) {
            if (areaX > 0)
                areaX = 0.0;
            else
                areaWidth = fullAreaWidth;
        }

        areaWidth = Math.round(areaWidth * 100.0) / 100.0;
        this.areaWidth = areaWidth;
        this.ratio = Math.round(this.areaWidth / this.areaHeight * 100.0) / 100.0;
    }

    public void setAreaHeight(Double areaHeight) {
        if (areaHeight < 0)
            areaHeight = 0.0;
        while (areaHeight + areaY > fullAreaHeight) {
            if (areaY > 0)
                areaY = 0.0;
            else
                areaHeight = fullAreaHeight;
        }

        areaHeight = Math.round(areaHeight * 100.0) / 100.0;
        this.areaHeight = areaHeight;
        this.ratio = Math.round(this.areaWidth / this.areaHeight * 100.0) / 100.0;
    }

    public void setAreaX(Double areaX) {
        if (areaX < 0)
            areaX = 0.0;
        if (areaX + areaWidth > fullAreaWidth)
            areaX = fullAreaWidth - areaWidth;

        areaX = Math.round(areaX * 100.0) / 100.0;
        this.areaX = areaX;
    }

    public void setAreaY(Double areaY) {
        if (areaY < 0)
            areaY = 0.0;
        if (areaY + areaHeight > fullAreaHeight)
            areaY = fullAreaHeight - areaHeight;

        areaY = Math.round(areaY * 100.0) / 100.0;
        this.areaY = areaY;
    }

    public void toJson(String filename) throws IOException {
        Gson gson = new Gson();
        String file = gson.toJson(this);
        File f = new File(filename);
        PrintWriter pr = new PrintWriter(new FileWriter(filename));
        pr.print(file);
        pr.close();
    }

    @Override
    public String toString() {
        return "TabletInfo{" +
                "fullAreaWidth=" + fullAreaWidth +
                ", fullAreaHeight=" + fullAreaHeight +
                ", monitors=" + monitors +
                ", areaWidth=" + areaWidth +
                ", areaHeight=" + areaHeight +
                ", areaX=" + areaX +
                ", areaY=" + areaY +
                ", activeMonitor='" + activeMonitor + '\'' +
                '}';
    }

//    public static void main(String[] args) {
//        TabletInfo tabletInfo = new TabletInfo();
//
//        Gson gson = new Gson();
//        String file = gson.toJson(tabletInfo);
//
//        try {
//            PrintWriter pr = new PrintWriter(new FileWriter("/home/odd/file.json"));
//            pr.print(file);
//            pr.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //System.out.println(file);
//
//        TabletInfo tabletInfo1 = null;
//        try {
//            tabletInfo1 = new TabletInfo("/home/odd/file.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(tabletInfo1);
//    }
} 
