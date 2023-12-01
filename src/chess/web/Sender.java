package chess.web;

import chess.util.CloseUtil;

import java.io.*;
import java.net.Socket;

public class Sender implements Runnable {
    //发送数据
    private DataOutputStream outputStream;
    //检测是否连接成功, 检查是否需要发送消息
    private boolean flag = true, isSuspended;
    private String sendData = "";

    //发送信息
    public void send(String str) {
        try {
            outputStream.writeUTF(str);
            outputStream.flush();//清空缓存
        } catch (IOException e) {
            flag = false;
            CloseUtil.closeAll(outputStream);//关闭数据流
            e.printStackTrace();
        }
    }

    public Sender(Socket client) {
        isSuspended = false;
        try {
            outputStream = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            flag = false;
            CloseUtil.closeAll(outputStream, client);
            e.printStackTrace();
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
                        System.out.println("ck Sender 线程等待");
                        this.wait();
                    }
                    System.out.println("ck Sender 线程启动");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            send(sendData);
            isSuspended = true;
            System.out.println("ck Sender 发送成功！");
        }
    }

    public void sendData(String message){
        this.sendData = message;
        resume();
    }

    @Deprecated
    public void continuing(){
        resume();
    }

    /**
     * 唤醒线程
     */
    synchronized void resume(){
        isSuspended = false;
        notify();
    }
}
