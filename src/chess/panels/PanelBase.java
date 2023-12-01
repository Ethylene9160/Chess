package chess.panels;

import chess.pieces.Chess;
import chess.pieces.ChessFactory;
import chess.recorder.Record;
import chess.recorder.Regreter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class PanelBase extends JPanel {
    public PanelType type;
    protected int ax=-1,ay=-1,bx=-1,by=-1;
    protected int currentPlayer, xOld, yOld;

    protected Point aOldPoint = new Point(0,0), aCurrentPoint= new Point(0,0), bOldPoint = new Point(0,0), bCurrentPoint = new Point(0,0);
    protected String shengweiChessName;
    protected Point currentChessPoint = new Point(0,0), targetChessPoint = new Point(0,0);
    public final static int MOUSE_DISTANCE_LIMIT = 20, BOARD_WEIGHT = 20+362 * 3 / 2, BOARD_HEIGHT = BOARD_WEIGHT;
    protected JLabel playerHint;
    public static final String FIRST = "白方", LATER = "黑方";
    public boolean flag;
    final String[] shengweis={Chess.ROOK, Chess.BISHOP, Chess.KNIGHT, Chess.QUEEN};

    public PanelBase() {
        setBounds(0, 0, BOARD_WEIGHT, BOARD_HEIGHT);
        this.animation = new Animation(this);
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    protected Animation animation;

    protected boolean isStart, isOver, isRelease, isPress;
    protected int judger;
    public static final Point[] POINTS = new Point[]{
            new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0),
            new Point(4, 0),
            new Point(5, 0), new Point(6, 0), new Point(7, 0),
            new Point(0, 1), new Point(1, 1),new Point(2, 1),new Point(3, 1),
            new Point(4, 1), new Point(5, 1),new Point(6, 1),new Point(7, 1),
    };
    protected Chess selectedChess, targetChess;
    public ArrayList<Chess> chessList = new ArrayList<Chess>();
    public ArrayList<Record> recordList = new ArrayList<>();
    public static final String[] CHESS_TYPES = {
            Chess.ROOK, Chess.KNIGHT,Chess.BISHOP,Chess.QUEEN,Chess.KING,Chess.BISHOP, Chess.KNIGHT, Chess.ROOK,
            Chess.PAWN, Chess.PAWN, Chess.PAWN, Chess.PAWN, Chess.PAWN, Chess.PAWN, Chess.PAWN, Chess.PAWN
    };

    protected void creatChess(int yourColor) {
        int len = CHESS_TYPES.length;
        Chess.setIDBase(0);
        if (yourColor == Chess.FIRST_COLOR) {
            for (int i = 0; i < len; i++) {
                chessList.add(ChessFactory.creat(CHESS_TYPES[i], Chess.LATER_COLOR, POINTS[i]));
                Chess c = ChessFactory.creat(CHESS_TYPES[i], Chess.FIRST_COLOR, POINTS[i]);
                c.reverse();
                chessList.add(c);
            }
        } else if (yourColor == Chess.LATER_COLOR) {
            for (int i = 0; i < len; i++) {
                Chess c = ChessFactory.creat(CHESS_TYPES[i], Chess.LATER_COLOR, POINTS[i]);
                c.reverse();
                chessList.add(c);
                chessList.add(ChessFactory.creat(CHESS_TYPES[i], Chess.FIRST_COLOR, POINTS[i]));
            }
        }
    }

    protected void init() {
        ax=-1;ay=-1;bx=-1;by=-1;
        selectedChess = null;
        isOver = false;
        isStart = false;
        isPress = false;
        isRelease = false;
        judger = 0;
        currentPlayer = 1;
        Chess.setIDBase(0);
        chessList.clear();
        recordList.clear();
        System.out.println("panelBase_init_ck: finished");
    }

    protected void drawChess(Graphics g) {
        for (Chess chess : chessList) {
            chess.draw(g, this);
        }
    }

    public void regret() {
        try {
            Regreter regreter = new Regreter(recordList);
            for (Chess chess : chessList) {
                if(chess.equals(regreter.loadCurrentChess())){//找到更改的棋子
                    Point p = regreter.loadStartPoint();
                    chess = regreter.loadCurrentChess();
                    chess.setPoint(p);
                    break;
                }
            }
            if (regreter.loadEatenChess() != null) {
                if(chessList.contains(regreter.loadEatenChess())) regreter.loadEatenChess().setPoint(regreter.loadEatenPoint());
                else chessList.add(regreter.loadEatenChess());
            }
//            this.flag = regreter.loadFlag();
            System.out.println(flag);
            swap();
        }catch (IndexOutOfBoundsException E){
            JOptionPane.showMessageDialog(null, "悔棋悔到头啦，重新开始吧。","提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * 将棋盘按照对称轴反转
     *
     * @param x 横坐标
     * @return 翻折后的横坐标
     */
//    public static int reverseX(int x) {
//        return 7 - x;
//    }//完成！
    public int reverseX(int x) {
        return x;
    }//完成！

    /**
     * @param y 纵坐标
     * @return 翻折后的纵坐标
     */
    public static int reverseY(int y) {
        return 7 - y;
    }//完成！

    public void reversePoint(Point p){
        p.x = reverseX(p.x);
        p.y = reverseY(p.y);
    }

    /**
     * 通过Point来判定该处有没有chess
     *
     * @param p 鼠标点击某处之后生成的point
     * @return Chess
     */
    public Chess getChessFromPoint(Point p) {
        for (Chess chess : chessList) {
            if (chess.getPoint().equals(p))
                return chess;
        }
        return null;
    }

    /**
     * 交换对手
     */
    public void swap() {
        this.currentPlayer = -this.currentPlayer;
        selectedChess = null;
        playerHint.setText(currentPlayer == 1 ? FIRST : LATER);
    }

    /**
     * 重新开始
     */
    public void restart() {
        chessList.clear();
        recordList.clear();
        init();
        playerHint.setText(FIRST);
    }

    public Chess getChessFromID(int ID) {
        for (int i = 0; i < chessList.size(); i++) {
            if (chessList.get(i).getID() == ID) return chessList.get(i);
        }
        return null;
    }


    protected void moveChess(Point point) {
        System.out.println("just move");
        recordList.add(new Record(selectedChess, null, selectedChess.getPoint(), point));
        switch (selectedChess.getName()){
            case Chess.PAWN:
//                if(selectedChess.whichLine(point) == -1){
//                    Chess c = getChessFromPoint(new Point(point.x, point.y+(selectedChess.isUp()? -1:1)));
//                    recordList.get(recordList.size()-1).setEatenChess(c);
//                    chessList.remove(c);
//                }else if ((selectedChess.isUp() && point.y == 7) || (!selectedChess.isUp() && point.y == 0)) {
//                    shengweiMessage();
//                }
                if(selectedChess.whichLine(point) == 1 && getChessFromPoint(point) == null){
                    Chess c = getChessFromPoint(new Point(point.x, point.y+(selectedChess.isUp()? -1:1)));
                    recordList.get(recordList.size()-1).setEatenChess(c);
                    chessList.remove(c);
                }else if ((selectedChess.isUp() && point.y == 7) || (!selectedChess.isUp() && point.y == 0)) {
                    //兵升变
                    shengweiMessage();
                }else if(selectedChess.getStep(point, 2)==2){
                    selectedChess.setDoubleMove(true);
//                    flag = true;
//                    selectedChess.moved = true;
//                    recordList.get(recordList.size()-1).setFlag(flag);
//                    System.out.println(recordList.get(recordList.size() - 1).isFlag());
//                    selectedChess.setPoint(point);
//                    return;
                }
                break;
            case Chess.KING:
//                System.out.println("King!!!!!");
                if(selectedChess.getStep(point)==2){//进行换位
                    int x = point.x < selectedChess.getPoint().x? 0:7;
                    Chess che = getChessFromPoint(new Point(x, point.y));
                    che.setPoint(selectedChess.getPoint().x+x==7?1:-1,point.y);
                    exchange(point,che);
                }
                break;
        }

//        flag = false;
        selectedChess.moved = true;
        selectedChess.setPoint(point);
    }

    protected void shengweiMessage(){
        System.out.println("升级！！！");
        System.out.println("shengweiChessName!"+shengweiChessName);
        while (shengweiChessName == null) {
            shengweiChessName = (String) JOptionPane.showInputDialog(null, "请选择升位棋子", "兵升变", JOptionPane.QUESTION_MESSAGE, null, shengweis, shengweis[0]);
        }
        recordList.get(recordList.size()-1).setShengweiInfo(shengweiChessName.equals(shengweis[0]) ? 0 : (shengweiChessName.equals(shengweis[1]) ? 1 : (shengweiChessName.equals(shengweis[2]) ? 2 : 3)));
        selectedChess.shengWei(shengweiChessName);
        System.out.println("升位检测-before：" + shengweiChessName);
        shengweiChessName = null;
        System.out.println("升位检测-after：" + shengweiChessName);
    }

    protected void eatChess(Point point, Chess eatenChess) {
        //判断吃过河兵
//        if(selectedChess.getName().equals(Chess.PAWN) && eatenChess.getName().equals(Chess.PAWN) && Math.abs(selectedChess.getPoint().y - point.y)==2){
//            point.y += selectedChess.isUp()? 1:-1;
//        }
        recordList.add(new Record(selectedChess, eatenChess, selectedChess.getPoint(), point));


        selectedChess.moved = true;
        chessList.remove(eatenChess);
        if (eatenChess.getID() == 10) {//红帅被吃
            judger = Chess.LATER_COLOR;
            isOver = true;
        } else if (eatenChess.getID() == 9) {//黑将被吃
            judger = Chess.FIRST_COLOR;
            isOver = true;
        }
        selectedChess.setPoint(point);//修改坐标
        //兵升位
        if(selectedChess.getName().equals(Chess.PAWN))
            if((selectedChess.isUp() && point.y == 7) || (!selectedChess.isUp() && point.y == 0)) shengweiMessage();
    }

    protected void processXY(Point point, PanelBase panel){
        Chess c;
//        if(point.x == 8 ){
//            if(type == PanelType.Local) c = getChessFromID(currentPlayer == 1? 2:1);
//            else c = getChessFromPoint(new Point(7,7));
////            Point cheP = new Point(3,0), kingP = new Point(1,0);
////            if(currentPlayer == 1){
////                reversePoint(cheP);
////                reversePoint(kingP);
////            }
////            if(selectedChess != null && selectedChess.canMove(point, panel)){
////                //todo
////                c.setPoint(cheP);//che
////                selectedChess.setPoint(kingP);//wang
////                recordList.add(new Record(selectedChess, c, point, point));
////                swap();
////            }
//            shortExchange(point, c);
//            return;
//        }else if(point.x == 9){
//            if(type == PanelType.Local) c = getChessFromID(currentPlayer == 1? 16:15);
//            else c = getChessFromPoint(new Point(0,7));
//            longExchange(point,c);
//
//            return;
//        }else
            c = getChessFromPoint(point);
        if (c != null) {
            //点击的时候有棋子：判断吃子还是自己人（重新选择）。
            if (c.getColor() == selectedChess.getColor()) {
                System.out.println("panelbase_processXY_ck: the same, reselect");
                selectedChess = c;
            } else {
                System.out.println("panelbase_processXY_ck: opponent, let's eat!!");
                if (selectedChess.canMove(point, panel)) {
                    eatChess(point, c);
                    //swap();//交换对手
                }
            }
        } else {
            //没有棋子，可能是下棋
            if (selectedChess.canMove(point, panel)) {
                 moveChess(point);
                 /*
                 测试区
                  */

                /**/



                //swap();//交换对手
            }

        }
//        flagEvent(0,0);
    }

    @Deprecated
    protected void flagEvent(int a, int b){
        if(flag){
            Chess chess = recordList.get(recordList.size()-1).getCurrentChess();
            Chess eChess = recordList.get(recordList.size()-1).getEatenChess();

            chess.setPoint(aCurrentPoint);
            if(eChess == null) {
//                Chess var = getChessFromPoint(bOldPoint);
//                Record record = recordList.get(recordList.size() - 1);
//                var.setPoint(bCurrentPoint);
//                record.setEatenChess(var);
//                chessList.remove(var);
                recordList.get(recordList.size() - 1).setEatenChess(targetChess);
                chessList.remove(targetChess);
            }
            this.flag = false;
//            changeFlag();
        }
    }




    /**
     * 真正的下棋落子。
     *
     * @param e 鼠标监听事件
     */
    protected void mouseEvent(MouseEvent e, PanelBase panel) {
        if (!isOver && isPress) {
            Point point = Chess.getNewPoint(e.getX(), e.getY());
            if(point.y == 7 && (point.x == 8||point.x==9)){
                processXY(point,panel);
                isPress = false;
                return;
            }
            if(point.x > 7 || point.x < 0 || point.y > 7 || point.y < 0) return;//修复了一个重大bug，2022-4-15
            if (selectedChess == null) {
                selectedChess = getChessFromPoint(point);
                if (selectedChess != null && currentPlayer != selectedChess.getColor()) selectedChess = null;
            } else {
                processXY(point, panel);
            }
            System.out.println("PanleBase_mouseEvent_ck: selected info="+selectedChess);
            isPress = false;
        }
    }


    public void mouseEvent(Point point, PanelBase panel){
        if (selectedChess == null) {
            selectedChess = getChessFromPoint(point);
            if (selectedChess != null && currentPlayer != selectedChess.getColor()) selectedChess = null;
        } else {
            processXY(point, panel);
        }
        System.out.println("PanelBase_mouseEvent_ck: "+ selectedChess);
    }

    private ArrayList<Point> pList = new ArrayList<>();
    public void drawHint(Point p){
        pList.add((Point)p.clone());
    }

    public void drawHint(Graphics g){
        for(Point point : pList) g.drawOval(Chess.MARGIN+Chess.SPACE * point.x, Chess.MARGIN+Chess.SPACE * point.y, Chess.SIZE, Chess.SIZE);
        pList.clear();
    }

    @Deprecated
    public void bingShengWei(Chess pawn, Point point){
//        eatChess(point, pawn);
//        chessList.remove(pawn);

//        Chess c = ChessFactory.creat(CHESS_TYPES[pawn.isUp()?point.x : 7-point.x], pawn.getColor(),point);
//        c.setID(c.getColor() == 1? 9:10);//todo: 完成修改ID
//        recordList.get(recordList.size()-1).setCurrentChess(c);
//        chessList.remove(pawn);
//        chessList.add(c);
    }

    @Deprecated
    public void setJustMove(Point currentP, Point eatenP){
        Chess currentChess = recordList.get(recordList.size()-1).getCurrentChess();
        Chess eatenChess = recordList.get(recordList.size()-1).getEatenChess();
        currentChess.setPoint(currentP);
        if(eatenChess == null && eatenP != null) {
            eatenChess = getChessFromPoint(eatenP);
            chessList.remove(eatenChess);
        }
        repaint();
    }

    @Deprecated
    public void setJustMove(Point currentP){
        recordList.get(recordList.size()-1).getCurrentChess().setPoint(currentP);
        repaint();
    }

    @Deprecated
    public void setJustMove(Point currentP, Chess eatenChess){
        Chess currentChess = recordList.get(recordList.size()-1).getCurrentChess();
        Chess etc = recordList.get(recordList.size()-1).getEatenChess();
        if(etc == null) {
            etc = eatenChess;
            chessList.remove(etc);
            recordList.get(recordList.size()-1).setEatenChess(etc);
        }
        repaint();
    }

    @Deprecated
    public void setA(Point point){
        this.ax = point.x;
        this.ay = point.y;;
    }

    @Deprecated
    public void setB(Point point){
        this.bx = point.x;
        this.by = point.y;;
    }

    @Deprecated
    public void initAB(){
        ax = -1; bx = -1;ay=-1;by=-1;
    }

    @Deprecated
    public void setOldCurrentPoints(int oax, int oay, int cax, int cay, int obx, int oby, int cbx, int cby){
        flag = true;
        this.aOldPoint.x = oax; this.aOldPoint.y = oay;
        this.aCurrentPoint.x = cax; this.aCurrentPoint.y = cay;
        this.bOldPoint.x = obx; this.bOldPoint.y = oby;
        this.bCurrentPoint.x = cbx; this.bCurrentPoint.y = cby;
    }

    @Deprecated
    public void setOldCurrentPoints(Point oap, Point cap, Point obp, Point cbp){
        this.setOldCurrentPoints(oap.x,oap.y,cap.x,cap.y,obp.x,obp.y,cbp.x,cbp.y);
    }

    @Deprecated
    public void setOldCurrentPoints(Point old, Chess target){
        this.flag = true;
        this.aCurrentPoint.x = old.x;
        this.aCurrentPoint.y = old.y;
        this.targetChess = target;
    }

    List<String> list = new ArrayList<String>();
    public void test(List<String> list){
        for(String str : list){
            System.out.println(str.length());
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i).length());
        }
    }

    @Deprecated
    public abstract void shortExchange(Point p, Chess c);

    @Deprecated
    public abstract void longExchange(Point p, Chess c);


    public abstract void exchange(Point p, Chess c);

//    @Override
//    public void keyTyped(KeyEvent e) {
//
//    }
//
//    @Override
//    public void keyPressed(KeyEvent e) {
//        System.out.println(e.getKeyCode());
//        if(selectedChess != null && selectedChess.getName().equals(Chess.KING)) {
//            int kc = e.getKeyCode();
//            switch (kc) {
//                case VK_E:
//                    shortExchange();
//                    break;
//                case VK_A:
//                    longExchange();
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public void keyReleased(KeyEvent e) {
//
//    }


}
class Animation implements Runnable{
    private boolean isSuspended;
    private int x0, y0, x1, y1, dx, dy;
    private JPanel panel;
    private Graphics g;
    private Image img;

    public Animation(JPanel panel) {
        this.panel = panel;
    }

    synchronized void resume(){
        isSuspended = false;
        notify();
    }

    public void draw(Point start, Point end, Graphics g,Image img){
        this.x0 = start.x;
        this.y0 = start.y;
        this.x1 = end.x;
        this.y1 = end.y;
        dx = x1-x0;
        dy = y1-y0;
        this.g = g;
        this.img = img;
        resume();
        draw();
    }

    private void draw(){
        if(this.x0 >= this.x1 || this.y0 > this.y1) return;
        g.drawImage(img, this.x0+=dx, this.y0+=dy, panel);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        draw();
    }


    @Override
    public void run() {
        while(true){
            if(!isSuspended) {
                try {
                    draw();
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isSuspended = true;
        }
    }
}
