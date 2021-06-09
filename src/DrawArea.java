import javax.swing.*;
import java.awt.*;

import static java.lang.Math.floor;

public class DrawArea extends JPanel {
    private TabletInfo tabletInfo = null;

    public DrawArea(TabletInfo tabletInfo) {
        this.tabletInfo = tabletInfo;

        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setPreferredSize(new Dimension((int) floor(tabletInfo.getFullAreaWidth()), (int) floor(tabletInfo.getFullAreaHeight())));
    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.clearRect(0, 0, (int) floor(tabletInfo.getFullAreaWidth()), (int) floor(tabletInfo.getFullAreaHeight()));
        g.drawRect((int) floor(tabletInfo.getAreaX()), (int) floor(tabletInfo.getAreaY()), (int) floor(tabletInfo.getAreaWidth()), (int) floor(tabletInfo.getAreaHeight()));
    }
}
