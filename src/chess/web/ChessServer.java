package chess.web;

import chess.panels.WebPanel;
import chess.threads.ServerThread;
import chess.util.Constants;
import chess.util.RegisterUtil;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;



/**
 * 游戏操作端的server
 * 通过构建channel数组，实现多线程转发消息。
 */
public class ChessServer {
    //创建集合对象，从而实现“群发
    public static Map<Integer, ChessChannel> listMap = new HashMap<>();
    public static int webID;
    public static Map<Integer,Registers> registers;
    public static void main(String[] args) throws IOException {
        myFrame();
        registers = RegisterUtil.getRegisters(Constants.CHESS_REGIST);
        System.out.println(registers);
        webID  = 100;//从100开始，避免和错误信息代码重合
        System.out.println("开服");
        ServerSocket moveServer = new ServerSocket(8888);
        while(true){
            webID ++;
            Socket socket = moveServer.accept();
            //创建线程对象，此时说明有客户端进来了
            ChessChannel chessChannel = new ChessChannel(socket, webID);
            System.out.println("a newcomer! online: "+(listMap.size()+1));
            new DataOutputStream(socket.getOutputStream()).writeUTF(WebPanel.GET_ID + Integer.toString(webID));
            //添加到集合
            listMap.put(webID, chessChannel);
            //启动线程
            new Thread(chessChannel).start();
        }
    }

    private static void myFrame(){
        JFrame frame = new JFrame();
        JTextField textField = new JTextField("CHCess服务器启动");
        textField.setEditable(false);
        frame.setSize(120,100);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.add(textField);
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public static boolean isContainsRegisterID(int ID){
        return registers.containsKey(ID);
    }
}