package chess.pieces;

import chess.panels.PanelBase;
import chess.style.Style;
import chess.util.Constants;
import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 国际象棋抽象类<p>
 *     KING: 国王<p>
 *     QUEEN: 皇后<p>
 *     ROOK: 战车，和中国象棋一样<p>
 *     BISHOP: 主教，对应象，写法上和中国象棋基本一致，需要去掉不能过河这个判断<p>
 *     KNIGHT: 骑士，对应马<p>
 *         PAWN: 兵
 *
 *
 * </p>
 * 除了棋子的一般着法外，国际象棋中存在下面三种特殊着法：<p>
 * 吃过路兵：如果对方的兵第一次行棋且直进两格，刚好形成本方有兵与其横向紧贴并列，则本方的兵可以立即斜进，把对方的兵吃掉，并视为一步棋。这个动作必须立刻进行，缓着后无效。记录时记为 “en passant” 或 “en pt”， 法语中表示 “路过”。
 * <p>
 * 兵升变：本方任何一个兵直进达到对方底线时，即可升变为除“王”和“兵”以外的任何一种棋子，可升变为“后”、“车”、“马”、“象”，不能不变。这被视为一步棋。升变后按新棋子的规则走棋。
 * <p>
 * 王车易位：每局棋中，双方各有一次机会，让王朝车的方向移动两格，然后车越过王，放在与王紧邻的一格上，作为王执行的一步棋。王车易位根据左右分为"长易位"（后翼易位，记谱记为0-0-0）和"短易位"（王翼易位，记谱记为0-0）。王车易位是国际象棋中较为重要的一种战略，它涉及王、车两种棋子，是关键时刻扭转局势或解杀还杀的手段。
 * <p>
 * 王车易位有较为严格的规则限制，当且仅当以下6个条件同时成立时，方可进行王车易位：
 * 1.王与用来易位的车均从未被移动过（即王和车处在棋局开始的原始位置，王在e1或e8，车在a1、a8、h1或h8。但如果王或用来易位的车之前曾经移动过，后来又返回了原始位置，则不能进行“王车易位”，因为不符合“从未被移动过”）；
 * 2.王与用来易位的车之间没有其他棋子阻隔；
 * 3.王不能正被对方“将军”（即“王车易位”不能作为“应将”的手段）；
 * 4.王所经过的格子不能在对方棋子的攻击范围之内；
 * 5.王所到达的格子不能被对方“将军”（即王不可以送吃）；
 * 6.王和对应的车必须处在同一横行（即通过兵的升变得到的车不能用来进行“王车易位”）。【注：此项规则为国际棋联在1972年所添加，目的是为防备这种情况：设想一局棋，假定白方的王从开局起一直未移动过，处在e1位置，那么白方若将一个兵成功走至对方底线的e8位置并且升变为车（见兵的升变规则），与王处在同一直行，且就满足以上1~5的条件，此时也可进行王车易位（因为规则1规定为对应的车未被移动过，而升变后的车确实没有移动，之前做的走动为兵在执行）。Max Pam发现这一走法，而且被棋手在比赛中采用过。为了防止这种不被习惯赞同但确实符合规定的走法出现，国际棋联添加了此规则。】
 * <p>
 * 对于王车易位的规则有一些误解，特此说明，一切以上述6点为准，在符合上述规则且有下列情况出现时，是允许王车易位的：
 * 1.王未正被“将军”，但之前被“将军”过；
 * 2.用来易位的车正受对方攻击；
 * 3.在长易位中，车所经过的格子在对方的攻击范围之内。
 * <p>
 * 上述三点不影响王车易位的进行。【注意：在比赛走子时，必须先移动王，再移动车，否则被判为车的一步棋，王车易位失效。】
 */
public abstract class Chess {
    public final static String KING = "king", ROOK = "rook", QUEEN = "queen",
            KNIGHT = "knight", BISHOP = "bishop", PAWN = "pawn";
    public static Style style = Style.DEFAULT;
    public final static String[] PIECES = { KING, ROOK, QUEEN, KNIGHT, BISHOP, PAWN};

    public static final int FIRST_COLOR = 1, LATER_COLOR = -1;
    //棋子大小
    public static final int SIZE = 60;
    //棋子距边缘距离
    public static final int MARGIN = 20;
    //棋子间距
    public static final int SPACE = 60;
    //棋子名称
    protected String name;
    public String path;
    //棋子颜色、实际坐标、下标
    private static int IDBase = 0;
    public static void setIDBase(int var){
        IDBase = var;
    }
    protected int color, x, y, ID;
    public boolean moved, changed;


    //棋子的网格坐标
    protected Point point, initPoint;

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.ID;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
        this.path = Constants.STYLE_PATH + File.separator + Style.getStyle(style) + File.separator + name + color+".png";
    }

    public void setID(int ID){
        this.ID = ID;
    }

    public void setPoint(Point point) {
        this.point = (Point) point.clone();
        if (initPoint == null) {
            initPoint = this.point;
        }
        calculatePosition();
    }

    public Chess(String name, Point point, int player) {
        this.name = name;
        this.color = player;

        this.path = Constants.STYLE_PATH + File.separator +Style.getStyle(style) + File.separator + name + color + ".png";
        System.out.println(path);
        this.point = new Point(point);
        this.initPoint = new Point(point);
        this.calculatePosition();
        this.ID = ++IDBase;
    }

    public void setPoint(int x, int y) {
        if (x >= 0 && x < 8 && y > -1 && y < 8) {
            this.point.x = x;
            this.point.y = y;
            calculatePosition();
        }
    }


    public Point getPoint() {
        return this.point;
    }

    /**
     * 绘制棋子的方法
     *
     * @param g     棋子画笔
     * @param panel 棋盘所在的panel
     */
    public void draw(Graphics g, PanelBase panel) {
        g.drawImage(Toolkit.getDefaultToolkit().getImage(path),
                MARGIN+SPACE * point.x, MARGIN+SPACE * point.y, SIZE, SIZE, panel);
    }

    public void drawSelectedChess(Graphics g) {
        g.drawRect(this.x, this.y, SIZE, SIZE);
    }

    public void calculatePosition() {
//        this.x = MARGIN - SIZE / 2 + SPACE * (point.x);
//        this.y = MARGIN - SIZE / 2 + SPACE * (point.y);
        this.x=MARGIN+SPACE * point.x;
        this.y=MARGIN+SPACE * point.y;
    }

    /**
     * 根据x，y坐标计算网格坐标
     *
     * @param x
     * @param y
     */
    public static Point getNewPoint(int x, int y) {
        //todo: 添加判定方法！判断x，y是否合法。
//        x = (x - MARGIN + SIZE / 2) / SPACE;
//        y = (y - MARGIN + SIZE / 2) / SPACE;
        x = (x-MARGIN)/SPACE;
        y = (y-MARGIN)/SPACE;
        return new Point(x, y);
    }

    public void reverse() {
        point.y = 7 - point.y;
        initPoint = point;
        calculatePosition();
    }

    /*
    下棋逻辑判断部分。
     */


    /**
     * 是否蹩脚。国际象棋里没有用。
     *
     * @param point
     * @param gamePanel
     * @return boolean
     */
    @Deprecated
    public boolean isPrevented(Point point, PanelBase gamePanel) {
        Point center = new Point();
        if (Chess.BISHOP.equals(name)) {
            center.x = (point.x + this.point.x) / 2;
            center.y = (point.y + this.point.y) / 2;
            return gamePanel.getChessFromPoint(center) != null;
        } else if (Chess.KNIGHT.equals(name)) {
            int line = whichLine(point);
            if (line == -2) {
                //x轴日字蹩脚：
                center.x = (point.x + this.point.x) / 2;
                center.y = this.point.y;
            } else if (line == -3) {
                //y轴日字蹩脚
                center.x = this.point.x;
                center.y = (point.y + this.point.y)/2;
            }
            return gamePanel.getChessFromPoint(center) != null;
        }
        return false;
    }

    /**
     * 判断棋子最初在河对岸还是我方。例如，y < 5, 在棋盘上方。
     *
     * @return
     */
    public boolean isUp() {
        return initPoint.y < 5;
    }

    /**
     * @param point 坐标
     * @return 3：沿x方向直线<p>
     * 2：沿着y方向直线<p>
     * 1：45°斜线<p>
     * -1：都不是<p>
     * -2: x轴日<p>
     * -3：y轴日
     */
    public int whichLine(Point point) {
        if (point.y == this.point.y) return 3;
        else if (point.x == this.point.x) return 2;
        else if (Math.abs(point.x - this.point.x) == Math.abs(point.y - this.point.y)) return 1;
        else if (Math.abs(this.point.x - point.x) == 2 && Math.abs(this.point.y - point.y) == 1) return -2;
        else if (Math.abs(this.point.y - point.y) == 2 && Math.abs(this.point.x - point.x) == 1) return -3;
        else return -1;
    }

    /**
     * 走的直角边数, 例如，马任走一次会走3个直角边，车从（2，1）走到（4，1）会走过2个直角边。
     *
     * @param point 目标点位置
     * @return 经过的小方格边数。
     * */
    public int getStep(Point point) {
//        int line = whichLine(point);
//        if (line == 3) return Math.abs(point.x - this.point.x);
//        else if (line == 2 || line == 1) return Math.abs(point.y - this.point.y);
//        else return line;
        return getStep(point, whichLine(point));
    }

    public int getStep(Point point, int line) {
        if (line == 3) return Math.abs(point.x - this.point.x);
        else if (line == 2 || line == 1) return Math.abs(point.y - this.point.y);
        else return line;
    }

    /**
     * 计算起点到终点之间的直线上有多少棋子。<b>不包括起点、终点</b>。
     * @param point 目标坐标
     * @return 棋子数量
     */
    @SuppressWarnings("can be update")
    public int pieceCount(Point point, PanelBase gamePanel){
        return pieceCount(whichLine(point), point, gamePanel);
    }

    /**
     * 计算起点到终点之间的直线上有多少棋子。<b>不包括起点、终点</b>。这个方法可以被精简。
     * @param line 走棋方向，如果不是直线或者对角线，会直接返回line值。
     * @param point 目标坐标
     * @param gamePanel 游戏面板
     * @return int
     */
    @SuppressWarnings("can be update")
    public int pieceCount(int line, Point point, PanelBase gamePanel){
        int start, end, cou = 0;
        Point tempP = new Point();
        if(line == 2){//沿y
            tempP.x = this.point.x;
            start = Math.min(point.y, this.point.y)+1;
            end = Math.max(point.y, this.point.y);

            for(int i = start ; i < end; i++){
                tempP.y = i;
                if(gamePanel.getChessFromPoint(tempP) != null) cou++;
            }
        }else if(line == 3){//沿x
            tempP.y = this.point.y;
            start = Math.min(point.x, this.point.x)+1;
            end = Math.max(point.x, this.point.x);
            for(int i = start ; i < end; i++){
                tempP.x = i;
                if(gamePanel.getChessFromPoint(tempP) != null) cou++;
            }
        }else if(line == 1){
            //todo: 完成象中途不能被挡住
            int startX = point.x, startY = point.y, endX = this.point.x;
            int deltaX = startX < endX? 1:-1, deltaY = startY < this.point.y? 1:-1;
            for(startX += deltaX, startY+=deltaY; startX != endX; startX+=deltaX, startY+=deltaY){
                tempP.x = startX; tempP.y = startY;
                if(gamePanel.getChessFromPoint(tempP) != null) {
                    cou++;
                }
            }
        }
        return cou;
    }

    /**
     * 判断是否前进
     * @param point 目标坐标
     * @return 是否前进
     */
    public boolean isForward(Point point){
        if(isUp()){//如果本身在上面，那么只能往下走
            return point.y > this.point.y;
        }else{
            return point.y < this.point.y;
        }
    }

    /**
     * 判断王车换位能否成立。
     * @return 能为真，不能为假
     */
    @Deprecated
    public boolean canWangCheYiWei(){
        //todo: 判断王车易位能否成立
        return false;
    }

    /**
     * 兵的特殊走法：能否换成新的棋子。例如走到对方的皇位置，变成皇后。
     * @return
     */
    @Deprecated
    public boolean canUpdatePawn(){
        return false;
    }

    @Override
    public String toString() {
        return String.format("棋子名称：%s, ID:%d, 横坐标：%d, 纵坐标：%d, \n属于【%d】阵营。", name, this.ID, point.x, point.y, color);
    }

    public boolean equals(Chess chess){
        return chess.getID() == this.ID;
    }//这不是equal方法。

    public void moveProcess(PanelBase panel){
        Point tp = new Point(0,0);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {//遍历棋盘每一个点
                tp.x = i; tp.y = j;
                if(canMove(tp, panel) ) {//如果可以走，就绘制提示。
                    if(panel.getChessFromPoint(tp) != null && panel.getChessFromPoint(tp).getColor() == color) continue;
                    panel.drawHint(tp);
                    System.out.println(i + " " + j + "可以走");
                }
            }
        }
    }

    @Deprecated
    public void shengWei(Point point){}

    public void shengWei(String name){}

    public boolean isAttacked(PanelBase panel){
        return false;
    }

    public void refactor(String s){}


    /**
     * 判断能否移动到Point处
     *
     * @param point 想要移动到的点位
     * @return 能否移动
     */
    public abstract boolean canMove(Point point, PanelBase panel);

    public void resetPath(){
        this.path = Constants.STYLE_PATH + File.separator + Style.getStyle(style) + File.separator + name + color + ".png";
    }

    @Deprecated
    public static boolean canMove(Point oldPoint, Point targetPoint, PanelBase panel){
        return false;
    };

    public boolean isDoubleMove(){
        return false;
    }

    public void setDoubleMove(boolean flag){

    }
}
