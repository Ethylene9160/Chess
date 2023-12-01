package animation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public interface AnimationGenerator extends ActionListener {
    void drawLine(int startX, int startY, int finalX, int finalY, Graphics g, JPanel panel);
}
