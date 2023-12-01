package chess.threads;

import chess.main.WebStartPanel;
import chess.util.Constants;

import javax.swing.*;

public class WebThread implements Runnable{
    private String IP;
    private int port;

    public WebThread(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            JFrame jFrame = new JFrame("中国象棋-联机模式");
            jFrame.setSize(Constants.FRAME_LENGTH, Constants.FRAME_HEIGHT);
            WebStartPanel panel = new WebStartPanel(IP, port);
            jFrame.add(panel);
            jFrame.setVisible(true);
        }catch (Exception e1){
//            JOptionPane.showMessageDialog(null,"错误：没有连接到服务器","Error", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }
    }


}
