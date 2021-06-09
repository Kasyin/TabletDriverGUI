import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;

public class DriverGUI extends JFrame {
    private String filename = System.getProperty("user.home") + "/.tablet_config";

    private TabletInfo tabletInfo = null;
    private DrawArea drawArea = null;

    public DriverGUI() {
        setTitle("Tablet driver GUI");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        initAttributes();
        initUI();

        pack();
        setVisible(true);
    }

    private void initAttributes() {
        try {
            tabletInfo = new TabletInfo(filename);
        } catch (IOException e) {
            tabletInfo = new TabletInfo();
        }
        ScriptRunner.setArea(tabletInfo);
    }

    private void initUI() {
        initMonitorSelection();
        initArea();
        initAreaSettings();
        initBottomButtons();
    }

    private void initMonitorSelection() {
        JPanel pnlMonitorContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel lblMonitors = new JLabel("Monitors");

        JComboBox<String> cmbMonitors = new JComboBox<>(tabletInfo.getMonitors().keySet().toArray(new String[0]));
        cmbMonitors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabletInfo.setActiveMonitor((String) cmbMonitors.getSelectedItem());
                ScriptRunner.setMonitor(tabletInfo);
            }
        });

        pnlMonitorContainer.add(lblMonitors);
        pnlMonitorContainer.add(cmbMonitors);

        add(pnlMonitorContainer);
    }

    private void initArea() {
        JPanel areaContainer = new JPanel();
        drawArea = new DrawArea(tabletInfo);
        areaContainer.add(drawArea);
        add(areaContainer);
    }

    private void initAreaSettings() {
        AreaSettings pnlAreaSettingsContainer = new AreaSettings(tabletInfo, drawArea);
        add(pnlAreaSettingsContainer);
    }

    private void initBottomButtons() {
        JPanel pnlBottomButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBottomButtons.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentSettings(pnlBottomButtons);
                ScriptRunner.setArea(tabletInfo);
            }
        });

        pnlBottomButtons.add(btnSave);

        add(pnlBottomButtons);
    }

    private void saveCurrentSettings(JPanel pnlBottomButtons) {
        try {
            tabletInfo.toJson(filename);
            JLabel lblSaved = new JLabel("Settings Saved!!");
            pnlBottomButtons.add(lblSaved);
            revalidate();
            repaint();
            Timer timer = new Timer(2000, e -> {
                pnlBottomButtons.remove(lblSaved);
                revalidate();
                repaint();
            });
            timer.setRepeats(false);
            timer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 
