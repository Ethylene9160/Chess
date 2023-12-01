package chess.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

import chess.pieces.Chess;
import chess.recorder.Record;
import chess.shop.MoneyShop;
import chess.shop.ShengwangShop;
import chess.shop.Shop;
import chess.style.Style;
import chess.threads.OpponentEmoThread;
import chess.threads.YourEmoThread;
import chess.util.Constants;
import chess.util.ImagePath;
import chess.web.ChessChannel;
import chess.web.Receiver;
import chess.web.Sender;

public class WebPanel extends PanelBase implements MouseListener, ActionListener, KeyListener {
    public final static String SIGN_SPLIT = "&", LINE_SPLIT = "#";
    public final static char SIGN_SPLIT_CHAR = '&', LINE_SPLIT_CHAR = '#';
    public static final char INVITE_CONNECT = 'a', NO_PERSON_FOUND = 'b', REGIST = 'c', PREPARE = 'd', APPLY_ACCOUNT = 'e', POSITION = 'f',
            ACCEPT_CONNECT = 'g', ERROR = 'h', APPLY_REGRET = 'i', ACCEPT_REGRET = 'j', REJECT_REGRET = 'k', SET_NAME = 'l', RESET_PASSWARD = 'm',
            SUCCESS_SET_PASSWARD = 'n', SYSTEM_WINDOWS = 'o', REJECT_CONNECT = 'p', FIND_ONLINE = 'q', SURREND = 'r', GET_ID = 's',
            SEND_MESSAGE = 't', APPLY_PEACE = 'u', AGREE_PEACE = 'v', REJECT_PEACE = 'w', DISCONNECT = 'x', PLAYER_INFO = 'y', WRONG_PASSWARD = 'z',
            APPLY_SWAP = 'A', AGREE_SWAP = 'B', REJECT_SWAP ='C', SERVER_CLOSE = 'D', CHANGE_HEAD = 'E', CHANGE_NAME ='F', EMO_MES ='G',
            SHENGBIAN = 'H', LONG_CHANGE = 'I', TO_SHENGWANG ='J', GET_MONEY = 'K';
    public static final String DEFAULT_NAME = "default", COLOR_BUTTON = "Cl", DISCONNECT_BUTTON = "Db", DEFAULT_HEAD = "default.png";
    private String opponentName = DEFAULT_NAME, opponentID, yourName = DEFAULT_NAME, yourID, yourHead = DEFAULT_HEAD, opponentHead = DEFAULT_HEAD,
                    yourEmo, opponentEmo;
    private StringBuilder messageBuilder = new StringBuilder();
    private boolean isConnect, isYouPrepare, isOpponentPrepare, isYou;
    private int yourColor, oOX,oOY,oNX, oNY;
    private JButton confirmButton, applyButton, forgetButton, emoButton,
            setSignButton, prepareButton, clorButton, disconnectBut;
    public JDialog registerDialog = new JDialog();
    private JTextField account = new JTextField("10001"), passward = new JTextField("123456");
    private Sender sender;
    private Receiver receiver;
    private Socket client;
    private YourEmoThread yourEmoThread;
    private OpponentEmoThread opponentEmoThread;
    public Shop shengwangShop, moneyShop;
    JTextArea textArea = new JTextArea("系统消息：\n");
    JScrollPane scrollPane;

    private void setRegisterDialog(){
        registerDialog.setLocationRelativeTo(null);
        registerDialog.setLayout(new GridLayout(4,2));

        confirmButton = setButton("确定", Constants.CONFIRM_BUTTON);
        applyButton = setButton("注册", Constants.APPLY_BUTTON);
        forgetButton = setButton("找密码",Constants.FORGET_BUTTON);
        setSignButton = setButton("换签名", Constants.SIGN_BUTTON);

        registerDialog.setSize(159,150);
        registerDialog.add(new JLabel("账号"));
        registerDialog.add(account);
        registerDialog.add(new JLabel("密码"));
        registerDialog.add(passward);
        registerDialog.add(confirmButton);
        registerDialog.add(applyButton);
        registerDialog.add(forgetButton);
        registerDialog.add(setSignButton);

    }

    @Override
    public int reverseX(int x){
        return 7-x;
    }


    public WebPanel(JLabel hint, String IP, int port){
        shengwangShop = new ShengwangShop(this);
        moneyShop = new MoneyShop(this);
        type = PanelType.Web;
        textArea.setFont(Constants.LITTLE_BLACK);
        scrollPane  = new JScrollPane(textArea);
        disconnectBut = setButtons("断开连接", DISCONNECT_BUTTON);
        this.playerHint = hint;
        init();
        this.setFocusable(true);
        this.addMouseListener(this);//自行在构造器中添加！
        try {
            client = new Socket(IP, port);
        } catch (IOException e) {
            System.out.println("webPanel_Constructure_ck: 没有连接到服务器");
            JOptionPane.showMessageDialog(null, "没有连接到服务器", "错误", JOptionPane.ERROR_MESSAGE);
        }
        sender = new Sender(client);
        receiver = new Receiver(client, this);
        yourEmoThread = new YourEmoThread(this);
        opponentEmoThread = new OpponentEmoThread(this);

        new Thread(yourEmoThread).start();
        new Thread(opponentEmoThread).start();
        new Thread(sender).start();
        new Thread(receiver).start();

        prepareButton = setButtons("准备", Constants.PREPARE_BUTTON);
        clorButton = setButtons("交换先后手", COLOR_BUTTON);
        this.emoButton = setButtons("发表情", Constants.YOUR_EMO);
        setBounds(0,0,Constants.PANEL_WEIGHT, Constants.FRAME_HEIGHT);
        textArea.setColumns(15);
        repaint();
        setRegisterDialog();
    }

    public WebPanel(JLabel hint) {
        this(hint, Constants.HOST, Constants.PORT);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);//clear

        scrollPane.setBounds(540,350,205,240);
        add(scrollPane);
        this.setBounds(0, 0, Constants.PANEL_WEIGHT, Constants.FRAME_HEIGHT);
//        g.drawImage(Toolkit.getDefaultToolkit().getImage(ImagePath.BACKGROUND),
//                0, 0, BOARD_WEIGHT, BOARD_HEIGHT, this);
        g.drawImage(Toolkit.getDefaultToolkit().getImage(ImagePath.BACKGROUND),
                -42, -42, BOARD_WEIGHT+40, BOARD_HEIGHT+40, this);
        //你的头像
        g.drawImage(Toolkit.getDefaultToolkit().getImage(Constants.HEAD_PATH + File.separator + yourHead),
                545, 180, 58, 58, this);
        //对手头像
        g.drawImage(Toolkit.getDefaultToolkit().getImage(Constants.HEAD_PATH + File.separator + opponentHead),
                545, 240, 58, 58, this);

        prepareButton.setBounds(600,80,120,40);
        disconnectBut.setBounds(600,130,120,40);
        clorButton.setBounds(540,310,100,40);
        emoButton.setBounds(631, 310,100,40);

        drawChess(g);
        if (selectedChess != null) {
            selectedChess.drawSelectedChess(g);
        }
        textArea.setBackground(new Color(20,244,244));
        this.add(prepareButton);
        add(disconnectBut);
        add(clorButton);
        add(emoButton);
        g.drawRect(Chess.MARGIN + Chess.SPACE * oOX,Chess.MARGIN + Chess.SPACE * oOY,Chess.SIZE,Chess.SIZE);
        g.drawRect(Chess.MARGIN + Chess.SPACE * oNX,Chess.MARGIN + Chess.SPACE * oNY,Chess.SIZE,Chess.SIZE);
        g.setFont(Constants.LITTLE_BLACK);
        g.drawString(yourID + "(你)", 606,205);
        g.drawString(yourName, 606,225);
        g.drawString(opponentID + "(对手)", 606,265);
        g.drawString(opponentName, 606,285);
        g.drawString("你的颜色：" + (yourColor == 1? "红":"黑"),580,65);
        g.setFont(Constants.LARGE_BLACK);
        g.drawString(currentPlayer == 1? "红":"黑", 580,40);
        if (isOver && isStart)//绘制结束图像
            switch (judger) {
                case 1:
                    g.drawString("red win！！", 100, 200);
                    break;
                case -1:
                    g.drawString("black win！！", 100, 200);
                    break;
                case 2:
                    g.drawString("和局", 100, 200);
                    break;
            }
        if(yourEmo != null) g.drawImage(Toolkit.getDefaultToolkit().getImage(Constants.EMO_PATH + File.separator + yourEmo),
                    672, 165, 71, 71, this);
        if(opponentEmo != null)g.drawImage(Toolkit.getDefaultToolkit().getImage(Constants.EMO_PATH + File.separator + opponentEmo),
                672, 225, 71,71, this);
    }

    @Override
    public void init() {
        isOpponentPrepare = false;
        isYouPrepare = false;
        oOY = -1;
        oOX = -1;
        oNX = -1;
        oNY = -1;
        super.init();
        if(yourColor != currentPlayer)
            for (Chess chess : chessList) {
                chess.setPoint(reverseX(chess.getPoint().x), chess.getPoint().y);
            }
        System.out.println("weboanel_init_ck: finished");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd){
            case Constants.CONFIRM_BUTTON:
                if(!isConnect) {
                    send(ChessChannel.REGISTER + LINE_SPLIT + account.getText() + SIGN_SPLIT + passward.getText());
                    registerDialog.setVisible(false);
                }else JOptionPane.showMessageDialog(null,"请先与对手断开连接再尝试登陆。");
                break;
            case Constants.APPLY_BUTTON:
                String question = (String)JOptionPane.showInputDialog(null, "请输入你的密保问题，不能包含”#““&”","密保问题");
                if(question != null && !question.isEmpty() && !question.contains(LINE_SPLIT) && !question.contains(SIGN_SPLIT)){
                    String key = (String) JOptionPane.showInputDialog(null, "请输入密保答案", "设置密保答案，不能包含”#““&”", JOptionPane.INFORMATION_MESSAGE);
                    if(key != null && !key.isEmpty() && !key.contains(LINE_SPLIT) && !key.contains(SIGN_SPLIT)){
                        send(ChessChannel.APPLY + LINE_SPLIT + question +SIGN_SPLIT_CHAR + key);
                    }
                }
                break;
            case Constants.PREPARE_BUTTON:
                if(isConnect && !(isStart && !isOver)) {
                    isYouPrepare = true;
                    if (isOpponentPrepare) {
                        init();
                        creatChess(yourColor);
                        if(yourColor != 1)
                            for (Chess chess : chessList) chess.setPoint(new Point(reverseX(chess.getPoint().x), chess.getPoint().y));
                        isYou = yourColor == 1;
                        isStart = true;
                        isOver = false;
                    }
                    send(PREPARE);
                    System.out.println("webpanel_actionperform_ck actionPerform 你已准备");
                    textArea.append("你已准备\n");
                    repaint();
                }else JOptionPane.showMessageDialog(null,"您还未结束当前游戏，无法准备。");
                break;
            case COLOR_BUTTON:
                if((!isStart || (isStart && isOver)) && isConnect) send(APPLY_SWAP);
                System.out.println("webpanel_actionpetform ck: actionPerform 改颜色了！");
                break;
            case DISCONNECT_BUTTON:
                if(isConnect) {
                    JOptionPane.showMessageDialog(null, "您已断开连接", "提示", JOptionPane.WARNING_MESSAGE);
                    send(DISCONNECT);
                    disconnect();
                    repaint();
                }
                break;
            case Constants.FORGET_BUTTON:
                String var = JOptionPane.showInputDialog(null,"请输入要找回密码的账号");
                if(var != null && !var.isEmpty()) send(ChessChannel.FIND_PASSWARD + LINE_SPLIT + var);
                break;
            case Constants.SIGN_BUTTON:
                signature(12);
                break;
            case Constants.YOUR_EMO:
                String emoN = (String) JOptionPane.showInputDialog(null, "向对方发送一个表情", "发送表情", JOptionPane.QUESTION_MESSAGE, null, Constants.EMOS, Constants.EMOS[0]);
                if(emoN != null && !emoN.isEmpty())yourEmoThread.continuing(emoN);
                break;
        }
    }

    public void signature(int len){
        if(isRegiserd()) {
            String var2 = null;
//                    var2 = JOptionPane.showInputDialog(null, "请输入新的个性签名。\n不得包含#或&，长度不得超过"+len);
//            if(var2 != null && !var2.contains(SIGN_SPLIT) && !var2.contains(LINE_SPLIT) && var2.length()<=len) {
//                send(var2, Integer.toString(ChessChannel.SET_SIGN));
//                textArea.append("签名设置成功！\n");
//            }
            while(var2 == null){
                var2 = JOptionPane.showInputDialog(null, "请输入新的个性签名。\n不得包含#或&，长度不得超过"+len);
                if(len < 13) break;
            }
            if(var2 != null) {
                send(var2, Integer.toString(ChessChannel.SET_SIGN));
                textArea.append("签名设置成功！\n");
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int xClick = e.getX();
        int yClick = e.getY();
        if(xClick > 550 && xClick < 730 && yClick > 180 && yClick < 300){
            if(yClick > 258 && opponentID != null && opponentID.length()>4) send(opponentID,Integer.toString(ChessChannel.FIND_OPPONENT));
            else if(yClick < 259 && yourID.length() > 4) send("",Integer.toString(ChessChannel.FIND_YOU));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(isYou) {
            xOld = e.getX();
            yOld = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (Math.abs(e.getX() - xOld) < MOUSE_DISTANCE_LIMIT && Math.abs(e.getY() - yOld) < MOUSE_DISTANCE_LIMIT) {
            isPress = true;
        }
        mouseEvent(e, this);
    }

    @Override
    public void mouseEvent(MouseEvent e, PanelBase panel) {
        super.mouseEvent(e, panel);
        System.out.println("webpanel_mouseEvent ck: " + yourID +"的list：" + recordList);
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * 发送消息
     *
     * @param message  信息
     * @param targetID 目标ID
     */
    void send(String message, String targetID) {
        send(targetID + LINE_SPLIT + message);
    }

    void send(Chess c1, Point p1, Chess c2, Point p2){
        send(String.format("%c%d&%d%d&%d&%d%d",POSITION, c1.getID(), p1.x, p1.y, c2==null? 0:c2.getID(), p2==null? 9:p2.x, p2==null? 9:p2.y), opponentID);
    }

    /**
     * 最底层的发送消息，消息会被发送到服务器。
     *
     * @param message 消息内容（String类型）
     */
    public void send(String message) {
        sender.sendData(message);
    }

    public void send(char ch) {
        send(Character.toString(ch), opponentID);
    }


    /**
     * 发送坐标信息到对手的方法。
     *
     * @param x
     * @param y
     * @param ID
     */
    private void send(int x, int y, int ID) {
        x = reverseX(x);
        y = reverseY(y);
        send(String.format("%c%d%d%d",POSITION, x, y, ID), opponentID);
    }

    private void send(StringBuilder sb){
        send(sb.toString(),opponentID);
    }

    /**
     * 将整数转化为编码字符，并组合成字符串。
     *
     * @param info 需要转换为Character类型String的整数信息
     * @return 由证书编码组合成的字符串
    */
    @Deprecated
    private String getStringFormat(int... info) {
        int len = info.length;
        char[] ches = new char[len];
        for (int i = 0; i < len; i++) {
            ches[i] = (char) info[i];
        }
        return new String(ches);
    }

    public void opponentAction(char beginner, String message) {

        System.out.println("webPanel_opponentAction_ck: web message" + message);
        beforeError(beginner, message);

        if (!isConnect) {
            switch (beginner) {
                case FIND_ONLINE:
                    String[] options = message.split(SIGN_SPLIT);
                    String str1 = (String) JOptionPane.showInputDialog(null,
                            "当前在线：",
                            "提示",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);
                    if (str1 != null && !str1.isEmpty()) {
                        connect(str1.substring(0, str1.indexOf(LINE_SPLIT)));

                    }
                    break;
                case REJECT_CONNECT:
                    JOptionPane.showMessageDialog(null, "对方拒绝了您的邀请，可能对方正在游戏。", "tips", JOptionPane.WARNING_MESSAGE);
                    break;
                case ACCEPT_CONNECT://成功连接到对手
                    //格式：对手ID&对手名&头像名
                    int p = message.indexOf(SIGN_SPLIT_CHAR), p2 = message.indexOf(SIGN_SPLIT_CHAR, 1+p);
                    this.opponentID = message.substring(0, p);
                    this.opponentName = message.substring(++p, p2++);
                    this.opponentHead = message.substring(p2);
                    //todo
                    //playerHint.setIcon(xxx);
                    isConnect = true;
                    isStart = false;
                    isOver = false;
                    send(ACCEPT_CONNECT + yourID + SIGN_SPLIT_CHAR + yourName + SIGN_SPLIT_CHAR + yourHead, opponentID);
                    JOptionPane.showMessageDialog(null, "成功连接到【" + opponentName + "】\n(" + opponentID + ")");
                    break;
                case INVITE_CONNECT:
                    String[] strs = message.split(SIGN_SPLIT);

                    if (JOptionPane.showConfirmDialog(null, strs[1] + "(ID: " + strs[0] + ")想要与你建立连接，\n是否同意？", "test", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE) == 0){
                        yourColor = -1;
                        send(ACCEPT_CONNECT + yourID + SIGN_SPLIT + yourName + SIGN_SPLIT_CHAR+ yourHead, strs[0]);
                    }

                    break;
                case NO_PERSON_FOUND:
                    JOptionPane.showMessageDialog(null, "账号输入错误。","error",0);
                    break;
                case WRONG_PASSWARD:
                    JOptionPane.showMessageDialog(null, "密码错误","Error",0);
                    break;
                case REGIST:
                    String strs2[] = message.split(LINE_SPLIT);
                    yourID = strs2[0];
                    yourName = strs2[1];
                    yourHead = strs2[2];
                    JOptionPane.showMessageDialog(null,"欢迎" + yourName +"上线！","登陆成功",1);
                    break;

            }
        } else {
            switch (beginner) {
                case POSITION://移动棋子或者吃棋子
                    position(message);
                    System.out.println("接收成功！");
                    break;
                case SHENGBIAN:
                    String[] sss = message.split(Character.toString(POSITION));
                    this.shengweiChessName = sss[0];
                    position(sss[1]);
                    break;
                case LONG_CHANGE:
                    Chess che2 = getChessFromPoint(new Point(7,0)), king2 = getChessFromPoint(new Point(3,0));
                    recordList.add(new Record(king2, che2, king2.getPoint(), che2.getPoint()));
                    Point cheP2 = new Point(4,0), kingP2 = new Point(5,0);
                    che2.setPoint(cheP2);//che
                    king2.setPoint(kingP2);//wang
                    this.swap();
                    oOX = cheP2.x;oOY = cheP2.y;
                    oNX = kingP2.x; oNY = kingP2.y;

                    break;
                case EMO_MES:
                    opponentEmoThread.continuing(message);
                    break;
                case APPLY_REGRET:
                    //TODO
                    int info = JOptionPane.showConfirmDialog(null, "对方请求悔棋，是否同意？", "悔棋请求", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if(info == 0) {
                        regret();
                        System.out.println("ck: 本方同意悔棋，要悔棋的棋子是：");
                        textArea.append("您同意了本次悔棋\n");
                        send(ACCEPT_REGRET);
                        repaint();
                    }
                    else send(REJECT_REGRET);
                    break;
                case CHANGE_HEAD:
                    opponentHead = message;
                    break;
                case CHANGE_NAME:
                    opponentName = message;
                    break;
                case REJECT_REGRET:
                    //TODO
                    JOptionPane.showMessageDialog(null, "对方拒绝了您的悔棋申请。", "拒绝悔棋", JOptionPane.WARNING_MESSAGE);
                    break;
                case INVITE_CONNECT:
                    send(message + LINE_SPLIT + REJECT_CONNECT);
                    break;
                case ACCEPT_REGRET:
                    //todo
                    regret();
                    textArea.append("对方同意了您的悔棋申请。\n");
                    repaint();
                    break;
                case SURREND:
                    judger = yourColor;
                    isOver = true;
                    break;
                case PREPARE:
                    isOpponentPrepare = true;
                    if(isYouPrepare){
                        init();
                        creatChess(yourColor);
                        if(yourColor != 1)
                            for (Chess chess : chessList) chess.setPoint(new Point(reverseX(chess.getPoint().x), chess.getPoint().y));
                        isYou = yourColor == 1;
                        isStart = true;
                        isOver = false;
                    }else JOptionPane.showMessageDialog(null, "对方已准备。\n点击准备按钮以开始游戏。", "提示", JOptionPane.INFORMATION_MESSAGE);

                    break;
                case APPLY_PEACE:
                    if(JOptionPane.showConfirmDialog(null, "对方申请求和，是否同意？", "求和申请", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==0) {
                        send(AGREE_PEACE);
                        isOver = true;
                        judger = 2;
                    }
                    else send(REJECT_PEACE);
                    break;
                case AGREE_PEACE:
                    JOptionPane.showMessageDialog(null, "求和成功。","求和", 1);
                    isOver = true;
                    judger = 2;
//                    send("0");//发出和棋计算量
                    //todo
                    break;
                case REJECT_PEACE:
                    JOptionPane.showMessageDialog(null, "对方拒绝了您的求和申请。","求和", 1);
                    break;
                case SEND_MESSAGE:
                    textArea.append("【对手】: " + message + "\n");
                    break;
                case DISCONNECT:
                    disconnect();
                    JOptionPane.showMessageDialog(null,"对方断开了连接","提示",2);
                    break;
                case APPLY_SWAP:
                    if(JOptionPane.showConfirmDialog(null, "对方申请交换颜色，是否同意？", "先后手交换申请", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)==0) {
                        send(AGREE_SWAP);
                        yourColor = -yourColor;
                    }else send(REJECT_SWAP);
                    break;
                case AGREE_SWAP:
                    JOptionPane.showMessageDialog(null,"对方同意了先后手交换申请。");
                    yourColor = -yourColor;
                    break;
                case REJECT_SWAP:
                    JOptionPane.showMessageDialog(null,"对方拒绝了您的先后手交换申请。");
                    break;
            }
        }
        repaint();
    }

    private void disconnect(){
        isConnect = false;
        opponentName = DEFAULT_NAME;
        opponentID = null;
        opponentHead = DEFAULT_HEAD;
        isYouPrepare = false;
        isOpponentPrepare = false;
    }

    private void beforeError(char beginner, String message) {
        switch (beginner) {
            case SUCCESS_SET_PASSWARD:
                JOptionPane.showMessageDialog(null, "成功修改了密码", "提示", JOptionPane.INFORMATION_MESSAGE);
                break;
            case SYSTEM_WINDOWS://系统提示
                JOptionPane.showMessageDialog(null, printArray(message.split(SIGN_SPLIT)), "系统提示", JOptionPane.WARNING_MESSAGE);
                break;
            case SERVER_CLOSE:
                JOptionPane.showMessageDialog(null, "服务器即将关闭，请赶快退出！","警告", JOptionPane.WARNING_MESSAGE);
                break;
            case GET_ID:
                this.yourID = message;
                break;
            case ERROR:
                JOptionPane.showMessageDialog(null, "很抱歉，我们遇到了未知错误，\n请检查您的操作，或者重新启动游戏。",
                        "错误", JOptionPane.ERROR_MESSAGE);
                break;
            case PLAYER_INFO:
                String[] strs = message.split(SIGN_SPLIT);
                StringBuilder var = new StringBuilder("ID: " + strs[0] + "\n昵称: " + strs[1]);
                for (int i = 2; i < strs.length; i++) {
                    var.append("\n").append(strs[i]);
                }
                JOptionPane.showMessageDialog(null, var.toString(), "玩家信息", JOptionPane.INFORMATION_MESSAGE);
                break;
            case WebPanel.RESET_PASSWARD:
                String[] newPassward = message.split(SIGN_SPLIT);
                String ans = JOptionPane.showInputDialog(null, "回答你的密保问题：\n"+newPassward[0],"找回密码",1);
                if(ans != null){
                    if(ans.equals(newPassward[1])){
                        String newA = JOptionPane.showInputDialog(null,"请输入新密码。密码长度不能超过6，不得包含‘#’”&‘。","重设密码",1);
                        if(newA != null && !newA.isEmpty()) send(newA, Integer.toString(ChessChannel.RESET_PASSWARD));
                    }else{
                        JOptionPane.showMessageDialog(null,"密保问题回答错误！","错误",2);
                    }
                }
                break;
            case APPLY_ACCOUNT:
                JOptionPane.showMessageDialog(null,"成功申请到账号：" + message + "。\n初始密码为123456。请尽快登录。\n祝你好运。");
                break;
            case GET_MONEY:
                moneyShop.showThis(Integer.parseInt(message));
                break;
            case TO_SHENGWANG:
                shengwangShop.showThis(Integer.parseInt(message));
//                JOptionPane.showOptionDialog();
                break;
        }
    }

    public void resetStyle(Style style){
        Chess.style = style;
        for(int i = 0; i < chessList.size();i++){
            chessList.get(i).resetPath();
        }
        System.out.println(Chess.style);
//        System.out.println(chessList.get(0).path);
        repaint();
    }

    /**
     * 打印String类型数组的自定义方法。每个元素会提行，<b>但是结尾不会换行。</b>
     *
     * @param strs 要打印的String类型数组
     * @return 字符串
     * @throws IndexOutOfBoundsException String类型数组必须非空或者无长度
     * @throws NullPointerException      String类型数组没有分配地址。
     */
    private String printArray(String[] strs) {
        if (strs == null) throw new NullPointerException("数组不存在。");
        if (strs.length < 1) throw new IndexOutOfBoundsException("数组长度不合法。");
        String s = strs[0];
        int len = strs.length;
        for (int i = 1; i < len; i++) {
            s = s + "\n" + strs[i];//2022/2/11/20:17,紧急。
        }
        return s;
    }

    private void position(String message){
        System.out.println("webPanel_opponentAction_ck: 移动开始");
        oNX = message.charAt(0)-'0';oNY = message.charAt(1)-'0';
        selectedChess = getChessFromID(Integer.parseInt(message.substring(2)));
        oOX = selectedChess.getPoint().x;
        oOY = selectedChess.getPoint().y;
        Point point = new Point(oNX,oNY);
        webProcessXY(point);
        if(judger == -yourColor) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            send("-1");
        }
        repaint();
    }

    @Deprecated
    private void removeChess(Chess eatenChess) {
//        Chess chess = getChessFromPoint(new Point(x, y));
        if ( eatenChess != null) {
            if(eatenChess.getID() == 9 || eatenChess.getID() == 10){
                isOver = true;
                judger = -eatenChess.getColor();
            }
            chessList.remove(eatenChess);
        }
    }

    public boolean isYou(){
        return isYou;
    }

    public void setYourName(String name) {
        this.yourName = name;
        repaint();
        send(10 + LINE_SPLIT + name);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(isConnect) send(CHANGE_NAME + name, opponentID);
    }

    public void connect() {
        send("98");//查询有没有人，其实这个方法应该叫做“查询在线人数”
    }

    private void connect(String targetID) {
        opponentID = targetID;
        yourColor = 1;
        send(INVITE_CONNECT + yourID + SIGN_SPLIT_CHAR + yourName, targetID);//真实邀请进行连接
    }

    @Deprecated
    public void rejectConnect(String message) {
        send(REJECT_CONNECT + "", message);
    }

    public void surrend() {
        if(isStart && !isOver) {
            send(SURREND);
            judger = -yourColor;
            isOver = true;
            repaint();
        }
    }

    @Override
    protected void moveChess(Point point) {
        try {
            super.moveChess(point);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        messageBuilder.append(POSITION).append(reverseX(point.x)).append(reverseY(point.y)).append(selectedChess.getID());
        send(messageBuilder);
        messageBuilder.setLength(0);
//        send(point.x, point.y, selectedChess.getID());
//        flagEvent(point.x, point.y);
        swap();//交换对手
        System.out.println("ck moveChess");
    }

    @Override
    protected void shengweiMessage(){
        while (shengweiChessName == null) {
            shengweiChessName = (String) JOptionPane.showInputDialog(null, "请选择升位棋子", "兵升变", JOptionPane.QUESTION_MESSAGE, null, shengweis, shengweis[0]);
        }
        recordList.get(recordList.size()-1).setShengweiInfo(shengweiChessName.equals(shengweis[0]) ? 0 : (shengweiChessName.equals(shengweis[1]) ? 1 : (shengweiChessName.equals(shengweis[2]) ? 2 : 3)));
        selectedChess.shengWei(shengweiChessName);
        System.out.println("升位检测-before：" + shengweiChessName);
        if(isYou) messageBuilder.append(SHENGBIAN).append(shengweiChessName);
        shengweiChessName = null;
        System.out.println("升位检测-after：" + shengweiChessName);
    }

    @Override
    protected void flagEvent(int x, int y){
        System.out.println("start this method");
        if(selectedChess == null) System.out.println("selectedChess is null!");
//        if(point == null) System.out.println("the point is null");
        if(!flag) {
            send(selectedChess, new Point(x,y), null, null);
            System.out.println("send successfully!!!");
        }
//        else{
//            Chess chess = recordList.get(recordList.size()-1).getCurrentChess();
//            Chess eChess = recordList.get(recordList.size()-1).getEatenChess();
//            chess.setPoint(aCurrentPoint);
//            if(eChess == null) {
//                recordList.get(recordList.size() - 1).setEatenChess(targetChess);
//                chessList.remove(targetChess);
//            }
//            this.flag = false;
//            send(String.format("%c%d&%d%d&%d&%d%d",EAT_PAWN,chess.getID(),chess.getPoint().x,chess.getPoint().y,targetChess.getID(),targetChess.getPoint().x, targetChess.getPoint().y), opponentID);
//        }
        System.out.println("OverThisMethod");
    }

    private void webProcessXY(Point point){
        Chess c = getChessFromPoint(point);
        if (c != null) super.eatChess(point, c);
        else super.moveChess(point);
        swap();
    }



    @Override
    protected void eatChess(Point point, Chess eatenChess) {

//        send(point.x, point.y, selectedChess.getID());
        super.eatChess(point, eatenChess);

        messageBuilder.append(POSITION).append(reverseX(point.x)).append(reverseY(point.y)).append(selectedChess.getID());
        send(messageBuilder);
        messageBuilder.setLength(0);

        System.out.println("ck eatChess");
        swap();//交换对手
    }

    @Override
    public void swap() {
        System.out.println(this.yourID + "ck swap(): " + (currentPlayer == 1? "red":"black"));
        super.swap();
        isYou = !isYou;
    }
//    @Override
//    public void keyTyped(KeyEvent e) {
//
//    }
//
//    @Override
//    public void keyPressed(KeyEvent e) {
//        //大招E和A技能
//    }
//
//    @Override
//    public void keyReleased(KeyEvent e) {
//
//    }

    @Deprecated
    @Override
    public void shortExchange(Point point, Chess c){
        Point cheP = new Point(4,7), kingP = new Point(6,7);
        if(selectedChess != null && selectedChess.canMove(point, this)){
            //todo
            recordList.add(new Record(selectedChess, c, selectedChess.getPoint(), c.getPoint()));
            c.setPoint(cheP);//che
            selectedChess.setPoint(kingP);//wang
            swap();
            send(SHENGBIAN);
        }
    }

    @Deprecated
    private void myShortChange(Point cp, Point kp, Chess c){
        c.setPoint(cp);//che
        selectedChess.setPoint(kp);//wang
        swap();
    }

    @Deprecated
    @Override
    public void longExchange(Point point, Chess c){
        Point cheP = new Point(3,7), kingP = new Point(1,7);
        if(selectedChess != null && selectedChess.canMove(point, this)){
            //todo
            recordList.add(new Record(selectedChess, c, selectedChess.getPoint(), c.getPoint()));
            c.setPoint(cheP);//che
            selectedChess.setPoint(kingP);//wang
            send(LONG_CHANGE);
            swap();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private JButton setButtons(String name, String cmd){
        JButton button = new JButton(name);
        button.addActionListener(this);
        button.setActionCommand(cmd);
        this.add(button);
        return button;
    }

    public boolean isRegiserd(){
        return yourID.length() > 4;
    }

    public void setYourHead(String head){
        this.yourHead = head;
        if(isConnect) send(CHANGE_HEAD + head, opponentID);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();
    }

    public void sendMessage(String mess){
        send(SEND_MESSAGE + mess, opponentID);
        textArea.append("【你】: "+ mess + "\n");
    }

    private JButton setButton(String name, String cmd){
        JButton button = new JButton(name);
        button.addActionListener(this);
        button.setActionCommand(cmd);
        return button;
    }

    public void startDrawYourEmo(String var){
        if(isConnect) send(EMO_MES + var, opponentID);
        yourEmo = var;
        repaint();
    }

    public void startDrawOpponentEmo(String var){
        opponentEmo = var;
        repaint();
    }

    public void endDrawYourEmo(){
        yourEmo = null;
        repaint();
    }

    public void endDrawOpponentEmo(){
        opponentEmo = null;
        repaint();
    }

    @Override
    public void exchange(Point point, Chess c){
        recordList.add(new Record(selectedChess, c, selectedChess.getPoint(), c.getPoint(), point.x, point.y));
        c.setPoint(new Point((selectedChess.getPoint().x+point.x)/2, point.y));
//        selectedChess.setPoint(point);

    }
}
