package webForZYG.example;

import webForZYG.WebListener;
import webForZYG.WebProxy;

import javax.swing.*;

public class Client implements WebListener {

    public static void main(String[] args) throws InterruptedException {
        Client c1 = new Client(1);
        Client c2 = new Client(2);
        c1.webProxy.send("101#你好c2");
        Thread.sleep(100);
    }

    WebProxy webProxy;
    int testID;
    public Client(int testID) {
        this.testID = testID;
        webProxy = new WebProxy(this, "127.0.0.1", 8888);
    }

    @Override
    public void gettingAction(String message) {
        System.out.println(testID + "接收到： "+message);
    }

    @Override
    public void showError() {
        JOptionPane.showMessageDialog(null,"没有链接到客户机");
    }
}
