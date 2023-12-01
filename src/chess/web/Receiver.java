package chess.web;

import chess.panels.WebPanel;
import chess.util.CloseUtil;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 专供网络对局使用。
 */
public class Receiver implements Runnable{
    protected DataInputStream inputStream;
    protected boolean flag = true;
    protected WebPanel webPanel;
    public String getData;
    public Receiver(Socket client, WebPanel webPanel) {
        this.webPanel = webPanel;
        try{
            inputStream = new DataInputStream(client.getInputStream());
        }catch (IOException e){
            flag = false;
            CloseUtil.closeAll(inputStream,client);
        }
    }

    public DataInputStream getInputStream(){
        return this.inputStream;
    }

    public String getMessage(){
        String str = "";
        try{
            str = inputStream.readUTF();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"错误：没有连接到服务器。\n请退出游戏或者检查网络连接。","错误",0);
            flag = false;
            CloseUtil.closeAll(inputStream);
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public void run() {
        while (flag){
            getData = getMessage();
            webPanel.opponentAction(getData.charAt(0), getData.substring(1));
            System.out.println("ck Receivevr: 获取到数据：" + getData);
        }
    }
}
