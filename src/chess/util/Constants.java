package chess.util;

import chess.panels.PanelBase;

import java.awt.*;
import java.io.File;

public class Constants {
    public static final int FRAME_LENGTH = 1080,
            FRAME_HEIGHT = 720;
    public static final String SAVE_BUTTON = "SAVE", LOAD_BUTTON = "LOAD", REGRET_BUTTON = "REGRET", RESTART_BUTTON = "RESTART",
            APPLY_BUTTON = "APPLY", PREPARE_BUTTON = "PREP", CONFIRM_BUTTON = "CFR", FORGET_BUTTON = "FGB", SIGN_BUTTON = "SetS",
            YOUR_EMO = "Yem";

    public final static String BASE_PATH = "D:\\resource\\WChess\\statics", //这里设置资源文件根目录
            LIBRARY_PATH = BASE_PATH + "\\Library",
            IMAGE_PATH = BASE_PATH + File.separator + "image",
            EMO_PATH = IMAGE_PATH + File.separator +"emo",
            HEAD_PATH = IMAGE_PATH + File.separator + "head",
            STYLE_PATH = IMAGE_PATH + File.separator + "style";

    public final static String REGISTER_PATH = BASE_PATH + File.separator + "register\\reg.ethy";
    public final static String CHESS_REGIST = BASE_PATH + File.separator + "register\\chessReg.ethy";
    public final static int PANEL_WEIGHT = PanelBase.BOARD_WEIGHT + 200;
    public final static String[] HEADERS = new File(HEAD_PATH).list(),
                                EMOS = new File(EMO_PATH).list();
    public static final Font LITTLE_BLACK = new Font("微软雅黑", Font.BOLD, 16),
            LARGE_BLACK = new Font("微软雅黑", Font.BOLD, 50);

    //在这里修改网络
    public static final int PORT = 8888;//port
    public static final String HOST = "127.0.0.1";//IP


    private static String[] getHead(){
        return new File(HEAD_PATH).list();
    }
}
