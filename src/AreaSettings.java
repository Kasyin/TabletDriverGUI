import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AreaSettings extends JPanel implements ActionListener {
    private TabletInfo tabletInfo = null;
    private DrawArea drawArea = null;

    private JTextField txtWidth = null;
    private JTextField txtHeight = null;
    private JTextField txtX = null;
    private JTextField txtY = null;

    private JCheckBox chbFullArea = null;
    private JCheckBox chbForceAspectRatio = null;
    private JCheckBox chbLeftHanded = null;

    public AreaSettings(TabletInfo tabletInfo, DrawArea drawArea) {
        this.tabletInfo = tabletInfo;
        this.drawArea = drawArea;
        setLayout(new FlowLayout(FlowLayout.LEFT));

        initUI();

        setSettings();
    }

    private void initUI() {
        initAreaFields();
        initCheckBoxes();
    }

    private void initAreaFields() {
        JPanel areaFields = new JPanel(new FlowLayout(FlowLayout.LEFT));

        initWidth(areaFields);
        initHeight(areaFields);
        initX(areaFields);
        initY(areaFields);

        add(areaFields);
    }

    private void initWidth(JPanel areaFields) {
        JPanel pnlWidth = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblWidth = new JLabel("Width");
        txtWidth = new JTextField(7);
        txtWidth.setText(tabletInfo.getAreaWidth() + "");

        txtWidth.addActionListener(this);
        txtWidth.getDocument().addDocumentListener(new FieldsDocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actionWidth();
            }
        });
        txtWidth.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                actionWidth();
                forceAspectRatio();
                txtWidth.setText(tabletInfo.getAreaWidth() + "");
                return true;
            }
        });

        pnlWidth.add(lblWidth);
        pnlWidth.add(txtWidth);

        areaFields.add(pnlWidth);
    }

    private void initHeight(JPanel areaFields) {
        JPanel pnlHeight = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblHeight = new JLabel("Height");
        txtHeight = new JTextField(7);
        txtHeight.setText(tabletInfo.getAreaHeight() + "");

        txtHeight.addActionListener(this);
        txtHeight.getDocument().addDocumentListener(new FieldsDocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actionHeight();
            }
        });
        txtHeight.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                actionHeight();
                txtHeight.setText(tabletInfo.getAreaHeight() + "");
                return true;
            }
        });

        pnlHeight.add(lblHeight);
        pnlHeight.add(txtHeight);

        areaFields.add(pnlHeight);
    }

    private void initX(JPanel areaFields) {
        JPanel pnlX = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblX = new JLabel("X");
        txtX = new JTextField(7);
        txtX.setText(tabletInfo.getAreaX() + "");

        txtX.addActionListener(this);
        txtX.getDocument().addDocumentListener(new FieldsDocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actionX();
            }
        });
        txtX.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                actionX();
                txtX.setText(tabletInfo.getAreaX() + "");
                return true;
            }
        });

        pnlX.add(lblX);
        pnlX.add(txtX);

        areaFields.add(pnlX);
    }

    private void initY(JPanel areaFields) {
        JPanel pnlY = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblY = new JLabel("Y");
        txtY = new JTextField(7);
        txtY.setText(tabletInfo.getAreaY() + "");

        txtY.addActionListener(this);
        txtY.getDocument().addDocumentListener(new FieldsDocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actionY();
            }
        });
        txtY.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                actionY();
                txtY.setText(tabletInfo.getAreaY() + "");
                return true;
            }
        });


        pnlY.add(lblY);
        pnlY.add(txtY);

        areaFields.add(pnlY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == txtWidth) {
            actionWidth();
            forceAspectRatio();
        }
        if (e.getSource() == txtHeight) {
            actionHeight();
        }
        if (e.getSource() == txtX) {
            actionX();
        }
        if (e.getSource() == txtY) {
            actionY();
        }

        //things to do all the time
        updateTextFields();
    }

    private void actionWidth() {
        double n = 0.0;
        try {
            n = Double.parseDouble(txtWidth.getText());
        } catch (NumberFormatException e) { }

        tabletInfo.setAreaWidth(n);
        drawArea.repaint();
    }

    private void actionHeight() {
        double n = 0.0;
        try {
            n = Double.parseDouble(txtHeight.getText());
        } catch (NumberFormatException e) { }

        tabletInfo.setAreaHeight(n);
        drawArea.repaint();
    }

    private void actionX() {
        double n = 0.0;
        try {
            n = Double.parseDouble(txtX.getText());
        } catch (NumberFormatException e) { }

        tabletInfo.setAreaX(n);
        drawArea.repaint();
    }

    private void actionY() {
        double n = 0.0;
        try {
            n = Double.parseDouble(txtY.getText());
        } catch (NumberFormatException e) { }

        tabletInfo.setAreaY(n);
        drawArea.repaint();
    }


    private void initCheckBoxes() {
        JPanel pnlCheckBoxesContainer = new JPanel(new GridLayout(2, 1));
        pnlCheckBoxesContainer.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 0));

        intiFullAreaBox(pnlCheckBoxesContainer);
        initForceAspectRayioBox(pnlCheckBoxesContainer);

        add(pnlCheckBoxesContainer);
    }


    private void setSettings() {
        setToFullArea();
        forceAspectRatio();
    }

    private void intiFullAreaBox(JPanel pnlCheckBoxesContainer) {
        chbFullArea = new JCheckBox("full area");

        chbFullArea.setSelected(tabletInfo.isFullarea());
        chbFullArea.addActionListener(e -> {
            setToFullArea();
        });

        pnlCheckBoxesContainer.add(chbFullArea);
    }

    private void setToFullArea() {
        if (chbFullArea.isSelected()) {
            tabletInfo.setFullarea(true);
            tabletInfo.setForceAspectRatio(false);
            updateCheckBoxes();

            tabletInfo.setAreaWidth(tabletInfo.getFullAreaWidth());
            tabletInfo.setAreaHeight(tabletInfo.getFullAreaHeight());
            updateTextFields();

            changeTextFieldsEditability(false);

            drawArea.repaint();
        } else {
            tabletInfo.setFullarea(false);
            changeTextFieldsEditability(true);
            drawArea.repaint();
        }
    }

    private void changeTextFieldsEditability(boolean b) {
        txtWidth.setEditable(b);
        txtHeight.setEditable(b);
        txtX.setEditable(b);
        txtY.setEditable(b);
    }

    private void initForceAspectRayioBox(JPanel pnlCheckBoxesContainer) {
        chbForceAspectRatio = new JCheckBox("force aspect ratio");

        chbForceAspectRatio.setSelected(tabletInfo.isForceAspectRatio());
        chbForceAspectRatio.addActionListener(e -> {
            forceAspectRatio();
        });

        pnlCheckBoxesContainer.add(chbForceAspectRatio);
    }

    private void forceAspectRatio() {
        if (chbForceAspectRatio.isSelected()) {
            tabletInfo.setForceAspectRatio(true);
            tabletInfo.setFullarea(false);
            updateCheckBoxes();

            txtHeight.setEditable(false);

            double ratio = tabletInfo.getMonitorsRatio().get(tabletInfo.getActiveMonitor());
            tabletInfo.setAreaHeight(tabletInfo.getAreaWidth() / ratio );
            updateTextFields();
        } else {
            txtHeight.setEditable(true);
        }

    }

    public void updateTextFields() {
        txtHeight.setText(tabletInfo.getAreaHeight() + "");
        txtWidth.setText(tabletInfo.getAreaWidth() + "");
        txtX.setText(tabletInfo.getAreaX() + "");
        txtY.setText(tabletInfo.getAreaY() + "");
    }

    public void updateCheckBoxes() {
        chbFullArea.setSelected(tabletInfo.isFullarea());
        changeTextFieldsEditability(true);
        chbForceAspectRatio.setSelected(tabletInfo.isForceAspectRatio());
    }
}
