package animation;


import javax.swing.*;
import java.awt.*;
import java.util.Timer;

public class AnimationProxy implements Runnable{
    private AnimationGenerator generator;
    private boolean isSuspended;
    private Timer timer;


    public AnimationProxy(AnimationGenerator generator) {
        this.generator = generator;
        this.timer = new Timer();
    }

    public void draw(int x0, int y0, int x1, int y1, Graphics g, JPanel panel, Image image){
        panel.paint(g);
    }

    synchronized void resume(){
        isSuspended = false;
        notify();
    }

    @Override
    public void run() {
        while(true){


            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            while(isSuspended) {
//                try {
//
//                    wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            isSuspended = true;
        }
    }


}
