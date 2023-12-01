package chess.main;

import chess.panels.GamePanel;
import chess.panels.WebButtons;
import chess.panels.WebPanel;
import chess.panels.PanelBase;
import chess.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static chess.util.Constants.*;

public class WebFrame extends FrameBase implements ActionListener {
    WebPanel gamePanel;
    WebButtons webButtons;

    public WebFrame() throws HeadlessException {
        setTitle("网络");
        startPanel = new JPanel();
        startPanel.setBounds(0, 0, FRAME_LENGTH, FRAME_HEIGHT);

        setResizable(false);
        this.setLayout(null);
//        this.setLayout(new BorderLayout());

        JLabel redHint = new JLabel("红方");


        gamePanel = new WebPanel(redHint);

        //新建一个button按钮的面板
        webButtons = new WebButtons(gamePanel);
//        gamePanel.setBounds(0, 0, PanelBase.BOARD_WEIGHT, PanelBase.BOARD_HEIGHT);
//        webButtons.setBounds(PanelBase.BOARD_WEIGHT, 0, FRAME_LENGTH-PanelBase.BOARD_WEIGHT, 450);
//        startPanel.add(gamePanel);
//        startPanel.add(webButtons);

//        this.add(startPanel);
        this.add(gamePanel, BorderLayout.WEST);
        this.add(webButtons, BorderLayout.EAST);
        gamePanel.setBounds(0, 0, PanelBase.BOARD_WEIGHT, PanelBase.BOARD_HEIGHT);
//        webButtons.setBounds(PanelBase.BOARD_WEIGHT,0,300,450);
        webButtons.setBounds(400, 30, 300, 450);

        this.setVisible(true);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        gamePanel.setBounds(0, 0, 1024, 768);
    }

    public static void main(String[] args) {
        WebPanel panel = new WebPanel(new JLabel("HONG"));
        WebButtons buttons = new WebButtons(panel);
        JFrame frame = new JFrame("网络对战");
        frame.setSize(1024,768);
        frame.setLocationRelativeTo(null);
        panel.setBounds(0,0,640,640);
        buttons.setBounds(640,0,200,300);
        frame.add(panel);
        frame.add(buttons);


        frame.setVisible(true);
//        WebFrame webFrame = new WebFrame();
//        buttons.setBounds(0,0,700,10);
//        webFrame.add(panel);
//        webFrame.add(buttons);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        String command = e.getActionCommand();
//        switch (command){
//            case REGRET_BUTTON:
//                gamePanel.regret();
//                break;
//            case LOAD_BUTTON:
//                break;
//            case SAVE_BUTTON:
//                break;
//            case RESTART_BUTTON:
//                gamePanel.restart();
//                break;
//        }
    }

    public JButton setButton(String buttonName, String buttonCommander) {
        JButton button = new JButton(buttonName);
        button.addActionListener(this);
        button.setActionCommand(buttonCommander);
        return button;
    }

    public JButton setButton(String buttonName, String buttonCommander, int length, int height) {
        JButton button = setButton(buttonName, buttonCommander);
        button.setSize(length, height);
        return button;
    }
}
