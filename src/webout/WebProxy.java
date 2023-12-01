package webout;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.Socket;

public class WebProxy implements Closeable{

    private WebListener listener;
    private final Sender sender;
    private final Receiver receiver;

    public WebProxy(WebListener listener, Sender sender, Receiver receiver) {
        this.listener = listener;
        this.sender = sender;
        this.receiver = receiver;
        new Thread(sender).start();
        new Thread(receiver).start();
    }


    public WebProxy(WebListener listener, Socket client){
        this(listener, new Sender(client, listener), new Receiver(client,listener));
    }

    public WebProxy (WebListener listener, String ip, int port) throws IOException {
        this(listener, new Socket(ip,port));
        if(port > 32554) throw new PortUnreachableException("port should not be so large or small");
    }

    /**
     * 给<b>服务器</b>发送消息。
     * @param sendData 待发送的消息，将会发送给服务器
     * @author ethy9160
     */
    public void send(String sendData){
        sender.sendData(sendData);
    }


    @Override
    public void close() throws IOException {
        sender.close();
        receiver.close();
    }
}

class Sender implements Runnable, Closeable{
    //发送数据
    private DataOutputStream outputStream;
    //检测是否连接成功, 检查是否需要发送消息
    private boolean flag = true, isSuspended;
    private WebListener listener;
    private String sendData;
    void setSendData(String s){
        sendData = s;
    }
    //发送信息
    public final void send(String str) {
        if(str == null || str.isEmpty()) return;
        try {
            outputStream.writeUTF(str);
            outputStream.flush();//清空缓存
        } catch (IOException e) {
            flag = false;
            chess.util.CloseUtil.closeAll(outputStream);//关闭数据流
            e.printStackTrace();
            listener.showConnectError();
        }
    }

    public Sender(Socket client, WebListener listener) {
        isSuspended = false;
        this.listener = listener;
        try {
            outputStream = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            flag = false;
            chess.util.CloseUtil.closeAll(outputStream, client);
            e.printStackTrace();
            listener.showConnectError();
        }
    }

    @Override
    public void run() {
        //发送数据
        while (flag) {
            try {
                //暂停线程
                synchronized (this){
                    while(isSuspended){
                        this.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            send(sendData);
            isSuspended = true;
        }
    }

    final void sendData(String s){
        this.sendData = s;
        resume();
    }

    /**
     * 唤醒线程
     */
    synchronized void resume(){
        isSuspended = false;
        notify();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}

class Receiver implements Runnable, Closeable{
    protected DataInputStream inputStream;
    protected boolean flag = true;
    public String getData;
    private final WebListener listener;

    public Receiver(Socket client, WebListener webPanel) {
        this.listener = webPanel;
        try{
            inputStream = new DataInputStream(client.getInputStream());
        }catch (IOException e){
            flag = false;
            listener.showConnectError();
            chess.util.CloseUtil.closeAll(inputStream,client);
        }
    }

    public DataInputStream getInputStream(){
        return this.inputStream;
    }

    public String getMessage() {
        String str = "";
        try{
            str = inputStream.readUTF();
        } catch (IOException e) {
            flag = false;
            chess.util.CloseUtil.closeAll(inputStream);
            e.printStackTrace();
            listener.showConnectError();
        }
        return str;
    }

    @Override
    public void run() {
        while (flag){
            getData = getMessage();
            listener.gettingAction(getData);
        }
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}

//class CloseUtil{
//    public static void closeAll(Closeable...able) {
//        for(Closeable c : able){
//            if(c != null){
//                try{
//                    c.close();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}
