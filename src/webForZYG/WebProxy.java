package webForZYG;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class WebProxy implements Closeable{
    private WebListener listener;
    private Sender sender;
    private Receiver receiver;
    private Socket socket;

    public WebProxy(WebListener listener, Socket socket) {
        this.listener = listener;
        this.socket = socket;
        receiver = new Receiver(listener,socket);
        sender = new Sender(listener,socket);
        new Thread(receiver).start();
    }

    public WebProxy(WebListener listener, String ip, int port){
        this.listener = listener;
        try {
            socket = new Socket(ip,port);
        } catch (IOException e) {
            listener.showError();
        }
        receiver = new Receiver(listener,socket);
        sender = new Sender(listener,socket);
        new Thread(receiver).start();

    }

    public void send(String message){
        sender.sendMessage(message);
    }

    @Override
    public void close() throws IOException {
        CloseUtil.close(sender, receiver, socket);
    }
}

class Sender implements Runnable, Closeable{
    private DataOutputStream outputStream;
    private Socket socket;
    private WebListener listener;
    private String str;

    public Sender(WebListener listener, Socket socket) {
        this.socket = socket;
        this.listener = listener;
        try {
            this.outputStream = new DataOutputStream(socket.getOutputStream());//对接上socket的流
        } catch (IOException e) {
            listener.showError();
            CloseUtil.close(socket, outputStream);
        }
    }


    private void send(){
        try {
            outputStream.writeUTF(str);//向对接的socket发送输出消息
        } catch (IOException e) {
            listener.showError();
            CloseUtil.close(outputStream,socket);
        }
    }

    public void sendMessage(String message){
        this.str = message;
        new Thread(this).start();
    }

    @Override
    public void run() {
        send();
    }

    @Override
    public void close() throws IOException {
        CloseUtil.close(outputStream,socket);
    }
}

class Receiver implements Runnable, Closeable {
    private DataInputStream inputStream;
    private Socket socket;
    private WebListener listener;

    public Receiver(WebListener listener, Socket socket) {
        this.socket = socket;
        this.listener = listener;
        try {
            this.inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            listener.showError();
            CloseUtil.close(socket, inputStream);
        }
    }

    private String getMessage() throws IOException{
        String str = inputStream.readUTF();//有消息，就继续往下；没有消息，就等待消息。
        return str;
    }

    @Override
    public void run() {
        while(true){
            try {
                listener.gettingAction(getMessage());
            } catch (IOException e) {
                listener.showError();
                CloseUtil.close(socket, inputStream);
                break;
            }
        }
    }

    @Override
    public void close() throws IOException {
        CloseUtil.close(socket, inputStream);
    }
}
