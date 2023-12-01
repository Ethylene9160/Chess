package webForZYG.example;

import webForZYG.CloseUtil;
import webForZYG.WebListener;
import webForZYG.WebProxy;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server{
    public static final Map<Integer,Channel> clientMap = new HashMap<>();
    static int cnt = 100;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        while(true) {
            Socket clientSocket = serverSocket.accept();//等待客户机连接
            new DataOutputStream(clientSocket.getOutputStream()).writeUTF("客户机你好，我是服务器");
            Channel channel = new Channel(clientSocket, cnt);
            System.out.println("new comer!" + cnt);
            clientMap.put(cnt++, channel);
            new Thread(channel).start();
        }
    }
}

class Channel implements WebListener, Runnable{
    private WebProxy proxy;
    private Socket clientSocket;
    private int myID, targetID;

    public Channel(Socket clientSocket, int myID) {
        this.clientSocket = clientSocket;
        this.myID = myID;
        proxy = new WebProxy(this, clientSocket);
    }

    @Override
    public void run() {

    }

    @Override
    public void gettingAction(String message) {
        System.out.println("接收到："+message);
        String ss[] = message.split("#");
        try {
            targetID = Integer.parseInt(ss[0]);
            Server.clientMap.get(targetID).proxy.send(ss[1]);//获取目标机器，转发出消息。
        }catch (NumberFormatException E1){
            E1.printStackTrace();
        }catch (NullPointerException e2){
            e2.printStackTrace();
        }

    }

    @Override
    public void showError() {
        CloseUtil.close(proxy);
        Server.clientMap.remove(myID);
        try {
            Server.clientMap.get(targetID).proxy.send("你的对象跑了。");
        }catch (NullPointerException e){

        }
    }
}
