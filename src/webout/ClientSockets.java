package webout;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientSockets implements Runnable{
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean flag = true;
    private Socket clientSocket;
    private Server server;

    public ClientSockets(Socket clientSocket, Server server) throws IOException{
        this.clientSocket = clientSocket;
        this.server = server;
        try{
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        }catch (IOException e){
            flag = false;
            CloseUtil.closeAll(inputStream, outputStream);
        }
    }

    //接收数据
    private String receive(){
        try {
            return inputStream.readUTF();
        }catch (IOException e){
            this.server.webError(this);
            flag = false;
            CloseUtil.closeAll(inputStream, outputStream, clientSocket);
        }
        return null;
    }

    /**
     * 发送消息。这里的消息将被发送到目标客户机。
     * @param str 待发送的<b>已经被处理好了的</b>消息。
     * @author ethy9160
     */
    public void send(String str){
        try {
            if (str != null && str.length() != 0) {
                outputStream.writeUTF(str);
                outputStream.flush();
            }
        }catch (IOException e){
            this.server.webError(this);
            flag = false;
            CloseUtil.closeAll(inputStream, outputStream, clientSocket);
        }
    }

    @Override
    public void run() {
        while(flag){
            this.server.transmit(receive());
        }
    }
}

class CloseUtil{
    static void closeAll(Closeable...able) {
        for(Closeable c : able){
            if(c != null){
                try{
                    c.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
