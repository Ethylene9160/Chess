package chess.main;

import chess.panels.PanelBase;
import chess.panels.WebButtons;
import chess.panels.WebPanel;
import chess.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static chess.panels.PanelBase.FIRST;

public class WebStartPanel extends JPanel {
    private WebPanel webPanel;
    private WebButtons webButtons;

    public WebStartPanel(String IP, int port){
        super();
        webPanel = new WebPanel(new JLabel(FIRST), IP, port);
        webButtons = new WebButtons(webPanel);
        webPanel.setBounds(0, 0, Constants.PANEL_WEIGHT, PanelBase.BOARD_HEIGHT);
        webButtons.setBounds(Constants.PANEL_WEIGHT, 0, 200, 650);
        add(webButtons);
        add(webPanel);
        repaint();
    }

    public WebStartPanel() {
        super();
        webPanel = new WebPanel(new JLabel("红"));
        webButtons = new WebButtons(webPanel);
        webPanel.setBounds(0, 0, Constants.PANEL_WEIGHT, PanelBase.BOARD_HEIGHT);
        webButtons.setBounds(Constants.PANEL_WEIGHT, 0, 200, 650);
        add(webButtons);
        add(webPanel);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
//        webPanel.setBounds(0, 0, Constants.PANEL_WEIGHT, PanelBase.BOARD_HEIGHT);
        webButtons.setBounds(Constants.PANEL_WEIGHT, 0, 200, 650);
    }

    public static void main(String[] args) {
        JFrame kaishi = new JFrame("kaishi");
        JButton button = new JButton("开始");
        kaishi.setSize(200,200);
        kaishi.setDefaultCloseOperation(3);
        kaishi.add(button);
        kaishi.setVisible(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame jFrame = new JFrame();
                jFrame.setSize(Constants.FRAME_LENGTH,Constants.FRAME_HEIGHT);
                WebStartPanel panel = new WebStartPanel();
                jFrame.add(panel);
                jFrame.setVisible(true);
            }
        });

    }
}
