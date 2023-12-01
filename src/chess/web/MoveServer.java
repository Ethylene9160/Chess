package chess.web;

import chess.threads.ServerThread;
import chess.util.Constants;
import chess.util.RegisterUtil;

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
public class MoveServer {
    //创建集合对象，从而实现“群发
    //public static List<MoveChannel> list = new ArrayList<MoveChannel>();
    public static Map<Integer, MoveChannel> listMap = new HashMap<>();
    public static int webID;
    public static Map<Integer,Registers> registers;
//    public static Map<Integer,Registers> registers = new HashMap<>();
    public static void main(String[] args) throws IOException {
        new Thread(new ServerThread("Reversi")).start();
        registers = RegisterUtil.getRegisters(Constants.REGISTER_PATH);
        System.out.println(registers);
        webID  = 100;//从100开始，避免和错误信息代码重合
        System.out.println("开服");
        ServerSocket moveServer = new ServerSocket(8888);
        while(true){
            webID ++;
            Socket socket = moveServer.accept();
            //创建线程对象，此时说明有客户端进来了
            MoveChannel moveChannel = new MoveChannel(socket, webID);
//            moveChannel.ownID = webID;
            System.out.println("a newcomer! online: "+(listMap.size()+1));
            //添加到集合
            new DataOutputStream(socket.getOutputStream()).writeUTF(Integer.toString(webID));
            listMap.put(webID, moveChannel);
            //list.add(moveChannel);
            //启动线程
            new Thread(moveChannel).start();
        }
    }

    public static boolean isContainsRegisterID(int ID){
        return registers.containsKey(ID);
    }
}