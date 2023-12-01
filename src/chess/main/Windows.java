package chess.main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Windows {
    public static void main(String[] args) {
        JFrame startFrame = new JFrame("开始");
        startFrame.setSize(200,500);;
        JButton localBtn = new JButton("本地");
        JButton webBtn = new JButton("网络");
        localBtn.setBounds(0,0,200,200);
        webBtn.setBounds(0,300,200,200);
        startFrame.add(localBtn);
        startFrame.add(webBtn);
        localBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameFrame();
            }
        });
        webBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WebFrame();
            }
        });
        startFrame.setResizable(false);
        startFrame.setLocationRelativeTo(null);
        startFrame.setDefaultCloseOperation(3);
        startFrame.setVisible(true);
    }
}
