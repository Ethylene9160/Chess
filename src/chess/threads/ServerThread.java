package chess.threads;

import javax.swing.*;

public class ServerThread implements Runnable{

    private String name;

    public ServerThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        JFrame frame = new JFrame();
        JTextField textField = new JTextField(name + "服务器启动");
        textField.setEditable(false);
        frame.setSize(120,100);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.add(textField);
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
