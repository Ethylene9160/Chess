package chess.threads;

import chess.main.GameFrame;

public class LocalThread implements Runnable{
    @Override
    public void run() {
        new GameFrame();
    }
}
