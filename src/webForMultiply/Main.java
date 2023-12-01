package webForMultiply;


public class Main implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("multi:"+i);
        }
    }
    public static void main(String[] args) {
        Thread thread = new Thread(new Main());
        thread.start();
        for (int i = 0; i < 1000; i++) {
            System.out.println("main:"+i);
        }
    }

}

