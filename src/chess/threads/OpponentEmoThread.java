package chess.threads;

import chess.panels.WebPanel;

import javax.swing.*;

import static java.lang.Thread.sleep;

public class OpponentEmoThread implements Runnable {
    private boolean flag = true;
    WebPanel panel;
    String emoName;

    public OpponentEmoThread(WebPanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        //发送数据
        while (flag) {
            try {
                //暂停线程
                synchronized (this) {
                    System.out.println("ck opponentEmo 线程等待");
                    this.wait();
                    System.out.println("ck opponentEmo 线程启动");
                }
                //todo
                //画图
                panel.startDrawOpponentEmo(emoName);
                sleep(5_000);
                //todo
                panel.endDrawOpponentEmo();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ck opponentEmo 成功！");
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
