package chess.panels;

import chess.pieces.Chess;
import chess.recorder.Loader;
import chess.recorder.Record;
import chess.recorder.Saver;
import chess.util.Constants;
import chess.util.ImagePath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_E;

public class GamePanel extends PanelBase implements ActionListener, MouseListener, KeyListener {
    @Override
    public void init() {
        super.init();
        creatChess(Chess.FIRST_COLOR);
    }

    //    private int[] xs, ys;
    public GamePanel(JLabel hint) {
        type = PanelType.Local;
        this.playerHint = hint;
        init();
        this.setFocusable(true);
        this.addMouseListener(this);//自行在构造器中添加！
        addKeyListener(this);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);//clear
        this.setBounds(0, 0, Constants.FRAME_LENGTH - 200, Constants.FRAME_HEIGHT);
        g.drawImage(Toolkit.getDefaultToolkit().getImage(ImagePath.BACKGROUND),
                -42, -42, BOARD_WEIGHT+40, BOARD_HEIGHT+40, this);
        drawChess(g);
        if (selectedChess != null) {
            //draw hint
            selectedChess.moveProcess(this);
            drawHint(g);
            selectedChess.drawSelectedChess(g);
        }
        g.setFont(new Font("微软雅黑", Font.BOLD, 45));
        switch (judger) {
            case 1:
                g.drawString("red win！！", 100, 200);
                break;
            case -1:
                g.drawString("black win！！", 100, 200);
                break;
        }
        g.setColor(Color.green);
        g.setFont(new Font("微软雅黑", Font.BOLD, 25));
        if(ax>-1) g.drawString("A", Chess.SPACE * ax + Chess.MARGIN, Chess.SPACE * ay + Chess.MARGIN);
        if(bx>-1) g.drawString("B", Chess.SPACE * bx + Chess.MARGIN, Chess.SPACE * by + Chess.MARGIN);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        xOld = e.getX();
        yOld = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (Math.abs(e.getX() - xOld) < MOUSE_DISTANCE_LIMIT && Math.abs(e.getY() - yOld) < MOUSE_DISTANCE_LIMIT) isPress = true;
        mouseEvent(e, this);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void restart() {
        super.restart();
        repaint();
    }

    @Override
    public void regret() {
        if (!isOver) {
            super.regret();
            repaint();
        }
    }

    @Override
    protected void moveChess(Point point) {
        //判断吃过路兵
//        if(selectedChess.getName().equals(Chess.PAWN) && !selectedChess.moved && Math.abs(selectedChess.getPoint().y-point.y) == 2){
//            Chess c1, c2;
//            if((c1=getChessFromPoint(new Point(point.x-1, point.y))) != null || (c2=getChessFromPoint(new Point(point.x-1, point.y))) != null){
//                int targetX = point.x, targetY = selectedChess.isUp()? point.y-1:point.y+1;
//                super.moveChess(point);
//                System.out.println("吃过路兵");
//
//                if(c1 != null) ;//todo
//                if(c2 != null) ;//todo
//
//                return;
//            }
//        }
        super.moveChess(point);
        swap();


    }

    @Override
    protected void eatChess(Point point, Chess eatenChess) {
        super.eatChess(point, eatenChess);
        swap();
    }

    @Override
    protected void mouseEvent(MouseEvent e, PanelBase panel) {
        super.mouseEvent(e, panel);
        repaint();
//        panel.setB(null);
    }

    public void save() {
        if (!isOver && recordList.size()>0) Saver.saveC(new File(Constants.LIBRARY_PATH).list().length, recordList, chessList);
        else JOptionPane.showMessageDialog(null,"您现在不能保存棋谱。");
    }

    public void load() {
        int max;
        File file = new File(Constants.LIBRARY_PATH);
        max = file.list().length;
        int c = 0;
        String[] options = new String[max];
        for (String name : file.list()) {
            options[c] = name;
            c++;
        }
        String str1 = (String) JOptionPane.showInputDialog(null, "当前共有" + max + "项存档，请选择：", "提示", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        System.out.println("Str1:" + str1);
        if (str1 != null) {
            String s = Loader.getStringInfo(str1);
            if(s == null) return;
            restart();
            System.out.println("info:    "+s);
            String[] strs = s.split(";");
            int len = strs.length;
            for (int i = 0; i < len; i++) {
                System.out.println(strs[i]);
//                if(strs[i].charAt(2) - '0' != 1) this.shengweiChessName = shengweis[strs[i].charAt(2) - '0'];
                if(strs[i].charAt(2) - '0' != 4) this.shengweiChessName = shengweis[strs[i].charAt(2) - '0'];
                this.mouseEvent(new Point(strs[i].charAt(0) - '0', strs[i].charAt(1) - '0'), this);
            }



//            Loader loader = new Loader(str1);
//            System.out.println(loader);
//            this.recordList.clear();
//            ArrayList<Record> records = loader.getRecordList();
//            for (Record r : records) recordList.add(r);
//            this.chessList.clear();
//            this.chessList = loader.getChessList();
//            this.currentPlayer = loader.getCurrentPlayer();
//            playerHint.setText(currentPlayer == 1 ? FIRST : LATER);
//            Chess.setIDBase(0);
            repaint();
        }
    }

    public ArrayList<Record> getRecordList() {
        return this.recordList;
    }

    @Deprecated
    @Override
    public void shortExchange(Point point, Chess c){
//        processXY(new Point(8,8), this);
//        this.repaint();
//        c = getChessFromID(currentPlayer == 1? 2:1);
        Point cheP = new Point(3,0), kingP = new Point(1,0);
        if(currentPlayer == 1){
            reversePoint(cheP);
            reversePoint(kingP);
        }
        if(selectedChess != null && selectedChess.canMove(point, this)){
            //todo
            recordList.add(new Record(selectedChess, c, selectedChess.getPoint(), c.getPoint(), point.x, point.y));
            c.setPoint(cheP);//che
            selectedChess.setPoint(kingP);//wang
            swap();
        }
    }

    @Deprecated
    @Override
    public void longExchange(Point point, Chess c){
        Point cheP = new Point(4,0), kingP = new Point(5,0);
        if(currentPlayer == 1){
            reversePoint(cheP);
            reversePoint(kingP);
        }
        if(selectedChess != null && selectedChess.canMove(point, this)){
            //todo
            recordList.add(new Record(selectedChess, c, selectedChess.getPoint(), c.getPoint(), point.x, point.y));
            c.setPoint(cheP);//che
            selectedChess.setPoint(kingP);//wang
            swap();
        }
    }

    @Override
    public void exchange(Point point, Chess c){
        recordList.add(new Record(selectedChess, c, selectedChess.getPoint(), c.getPoint(), point.x, point.y));
        c.setPoint(new Point((selectedChess.getPoint().x+point.x)/2, point.y));
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
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
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
