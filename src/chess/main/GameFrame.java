package chess.main;

import chess.panels.GamePanel;
import chess.panels.TransparentPanel;
import chess.util.Constants;

import static chess.util.Constants.REGRET_BUTTON;
import static chess.util.Constants.RESTART_BUTTON;
import static chess.util.Constants.LOAD_BUTTON;
import static chess.util.Constants.SAVE_BUTTON;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame implements ActionListener {
    GamePanel gamePanel;

    public GameFrame() throws HeadlessException {
        this.setSize(Constants.FRAME_LENGTH, Constants.FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
//        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //新建一个button按钮的面板
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        this.add(buttonPanel, BorderLayout.EAST);

        JLabel redHint = new JLabel("红方");
        JButton regretButton = setButton("悔棋", REGRET_BUTTON);
        JButton loadButton = setButton("读取存档", LOAD_BUTTON);
        JButton saveButton = setButton("保存",SAVE_BUTTON);
        JButton restartButton = setButton("重新开始", RESTART_BUTTON);
        buttonPanel.add(redHint);
        buttonPanel.add(regretButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(loadButton);

        gamePanel = new GamePanel(redHint);
        this.add(gamePanel, BorderLayout.WEST);
        this.add(new TransparentPanel(), BorderLayout.WEST);


        this.setVisible(true);
    }

    public static void main(String[] args) {
        new GameFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command){
            case REGRET_BUTTON:
                gamePanel.regret();
                System.out.println("ck: regret button: "+gamePanel.getRecordList());
                break;
            case LOAD_BUTTON:
                gamePanel.load();
                System.out.println("load!");
                System.out.println(gamePanel.getRecordList());
                break;
            case SAVE_BUTTON:
                gamePanel.save();
                System.out.println("save!");
                break;
            case RESTART_BUTTON:
                gamePanel.restart();
                break;
        }
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
