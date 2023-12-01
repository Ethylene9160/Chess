package chess.panels;

import chess.pieces.Chess;
import chess.util.Constants;
import chess.util.ImagePath;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class TransparentPanel extends JPanel {
    public TransparentPanel() {
        this("default.png",0,0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBackground(Color.blue);
        JPanel panel = new JPanel();
        panel.setBounds(0,0,1024,768);

        frame.add(new TransparentPanel("avar0.png",50,50));
        frame.add(new TransparentPanel());
        frame.setVisible(true);
        frame.setBounds(0,0,1026,768);

    }

    String name;
    int x, y;

    public TransparentPanel(String name, int x, int y) {
        this.name = name;
        setBackground(null);
        setOpaque(false);
        setBounds(0,0, PanelBase.BOARD_WEIGHT + 200, PanelBase.BOARD_HEIGHT);
        this.x = x;
        this.y = y;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Toolkit.getDefaultToolkit().getImage(Constants.HEAD_PATH + File.separator + name),x, y, this);
    }
}
