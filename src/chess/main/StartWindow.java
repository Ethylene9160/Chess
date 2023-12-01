package chess.main;

import chess.threads.LocalThread;
import chess.threads.WebThread;

import javax.swing.*;
import java.awt.*;

public class StartWindow {
    static JDialog dia;
    public static void main(String[] args) {
        JFrame frame = new JFrame("中国象棋");
        frame.setSize(160,300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JButton localBut = new JButton("本地模式");
        JTextField ipField = new JTextField("127.0.0.1");
        JTextField portField = new JTextField("8888");


        localBut.addActionListener(e ->{
            new Thread(new LocalThread()).start();
        });

        JButton webBut = new JButton("联机模式");


        JButton conWeb = new JButton("确定");
        conWeb.addActionListener(e->{
            try {
                new Thread(new WebThread(ipField.getText(), Integer.parseInt(portField.getText()))).start();
                dia.setVisible(false);
            }catch (NumberFormatException e1){
                JOptionPane.showMessageDialog(null,"输入错误","ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton moren = new JButton("默认端口");
        moren.addActionListener(e->{
            new Thread(new WebThread("103.46.128.49",37856)).start();
            dia.setVisible(false);
        });

        dia = setDialog(ipField, conWeb, portField, moren);
        webBut.addActionListener(e -> {
            dia.setVisible(true);
        });
        frame.setLayout(new GridLayout(2,1));
        frame.add(localBut);

        frame.add(webBut);
        frame.setVisible(true);
    }

    private static JDialog setDialog(JTextField f1, JButton b1, JTextField f2, JButton b2){
        JDialog dialog = new JDialog();
        dialog.setSize(180,135);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(false);
        dialog.setLayout(new GridLayout(3,2));
        dialog.add(new JLabel("服务器IP："));
        dialog.add(f1);
        dialog.add(new JLabel("服务器端口:"));
        dialog.add(f2);
        dialog.add(b1);
        dialog.add(b2);
        return dialog;
    }
}
