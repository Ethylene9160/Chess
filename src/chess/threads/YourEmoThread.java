package chess.threads;

import chess.panels.WebPanel;

import javax.swing.*;

import static java.lang.Thread.sleep;

public class YourEmoThread implements Runnable {
    private boolean flag = true;
    WebPanel panel;
    String emoName;

    public YourEmoThread(WebPanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        //发送数据
        while (flag) {
            try {
                //暂停线程
                synchronized (this) {
                    System.out.println("ck yourEmo 线程等待");
                    this.wait();
                    System.out.println("ck yourEmo 线程启动");
                }
                //todo
                //画图
                panel.startDrawYourEmo(emoName);
                System.out.println(emoName);
                sleep(5000);
                //todo
                panel.endDrawYourEmo();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ck yourEmo 成功！");
        }
    }

    public void stopThis() {
        flag = false;
    }

    public void continuing(String emoName) {
        this.emoName = emoName;
        resume();
    }

    /**
     * 唤醒线程
     */
    synchronized void resume() {
        notify();
    }
}
