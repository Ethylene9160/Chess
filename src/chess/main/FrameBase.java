package chess.main;

import chess.util.Constants;

import javax.swing.*;
import java.awt.*;

public class FrameBase extends JFrame {
    protected JPanel startPanel;
    public FrameBase() throws HeadlessException {
        this.setSize(Constants.FRAME_LENGTH, Constants.FRAME_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
