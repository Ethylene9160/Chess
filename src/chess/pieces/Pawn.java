package chess.pieces;

import chess.panels.PanelBase;
import chess.style.Style;
import chess.util.Constants;

import java.awt.*;
import java.io.File;

/**
 * 兵类<p>
 *     rules: 只能向前直走，每次只能走一格。但走第一步时，可以走一格或两格。兵的吃子方法与行棋方向不一样，它是直走斜吃，即如果兵的斜进一格内有对方棋子，就可以吃掉它而占据该格。
 * </p>
 * @author ethy9160
 */
public class Pawn extends Chess{
    public Pawn(String name, Point point, int player) {
        super(name, point, player);
    }
    private boolean doubleStep;

    @Override
    public boolean canMove(Point point, PanelBase panel) {
        if(!changed) {
            if(!this.name.equals(Chess.PAWN)) refactor(Chess.PAWN);
            Chess c = panel.getChessFromPoint(new Point(point.x, point.y+(isUp()?-1:1)));
            if(!moved) {
                return
                        point.x == this.point.x && isForward(point) && getStep(point) == 1 &&
                                panel.getChessFromPoint(point) == null //一步
                                ||
                                point.x== this.point.x && isForward(point) && getStep(point) == 2 &&
                                        panel.getChessFromPoint(point) == null && c==null//两步
                                ||
                        Math.abs(point.x - this.point.x) == 1 && Math.abs((point.y - this.point.y))==1 && panel.getChessFromPoint(point) != null ;//斜着走
//                                || isForward(point);

//                                        && c.isLastMove() && c.getName().equals(Chess.PAWN) && c.getColor() != this.color;


//                                ||
//                        Math.abs(point.x - this.point.x) == 1 && Math.abs(point.y-this.point.y) == 3 && panel.getChessFromPoint(point) == null
//                                && pieceCount(2,point, panel) == 0
//                                && c!= null && c.getName().equals(Chess.PAWN);   //过河兵
            }
//            if(!moved && getStep(point)==2 && whichLine(point) == 2){
//                if(pieceCount(2,point,panel)==1) return false;
//                boolean flags[] = {false,false};
//                Chess[] c = {panel.getChessFromPoint(new Point(point.x-1, point.y)), panel.getChessFromPoint(new Point(point.x+1, point.y))};
//                for (int i = 0; i < 2; i++) {
//                    if(c[i] != null && c[i].getColor() != this.color && c[i].getName().equals(Chess.PAWN)){
//                        if (i == 0) panel.setA(c[i].getPoint());
//                        else panel.setB(c[i].getPoint());
//                        flags[i] = true;
//                    }
//                }
//                if(flags[1]||flags[0]){
//                    panel.repaint();
//                    String[] fruits={"吃A","吃B","取消",};
//                    int x;
//                    while(true) {
//                        x = JOptionPane.showOptionDialog(null, "是否发动技能？", "吃过路兵",JOptionPane.YES_NO_CANCEL_OPTION ,JOptionPane.QUESTION_MESSAGE,null, fruits, fruits[0]);
//                        if(x>-1&&x<2&&flags[x]) {
////                            panel.setOldCurrentPoints(0,0,c[x].point.x,c[x].point.y+(this.isUp()?1:-1), c[x].point.x,c[x].point.y,c[x].point.x,c[x].point.y);
//                            point.x = c[x].point.x;
//                            point.y = c[x].point.y+(this.isUp()?1:-1);
//                            panel.setOldCurrentPoints(new Point(c[x].point.x,c[x].point.y+(this.isUp()?1:-1)), c[x]);
//                            break;
//                        }
//                        if(x==2)break;
//                    }
//                    panel.initAB();
//                }
//                return true;
//            }
            return isForward(point)

                    &&(

                    ((whichLine(point) == 2 && panel.getChessFromPoint(point) == null &&
                            //
                            /*((!moved && getStep(point) < 3) ||(moved &&*/ getStep(point) == 1))
                    ||
                            //
//                            (whichLine(point) == 1 && getStep(point) == 1 && panel.getChessFromPoint(point) != null)
//                    ||
//                        this.point.y==(isUp()? 4:3) && Math.abs(this.point.x - point.x )==1 && Math.abs(this.point.y - point.y )==1
//                                && c != null
//                                && c.getName().equals(Chess.PAWN) && c.getColor() != this.color
//                                && panel.getChessFromPoint(point) == null
//                                && panel.recordList.get(panel.recordList.size()-1).getCurrentChess() == c
//                                && c.isDoubleMove()//吃过路兵

                            whichLine(point) == 1 && getStep(point) == 1 &&
                                    (panel.getChessFromPoint(point) != null//吃子
                                            ||
                                            this.point.y==(isUp()? 4:3) && c != null
                                    && c.getName().equals(Chess.PAWN) && c.getColor() != this.color
                                    && panel.getChessFromPoint(point) == null
                                    && panel.recordList.get(panel.recordList.size()-1).getCurrentChess() == c
                                    && c.isDoubleMove()//吃过路兵
                                    )

                    );
        } else return updateChess.canMove(point, panel);
    }


    @Deprecated
    private boolean canPawnMove(Point point, PanelBase panel){
        if(!changed)
        return isForward(point) &&
                ((whichLine(point) == 2 && panel.getChessFromPoint(point) == null &&
                //
                ((!moved && getStep(point) < 3 && pieceCount(2,point,panel)==0) || (moved && getStep(point)==1))) ||
                //
                (whichLine(point) == 1 && getStep(point) == 1 && panel.getChessFromPoint(point) != null));
        else return updateChess.canMove(point, panel);
    }

//    @Override
//    public void moveProcess(PanelBase panel){
//        Point tp = new Point(0,0);
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                tp.x = i; tp.y = j;
//                if(canPawnMove(tp, panel)) {
//                    panel.drawHint(tp);
//                    System.out.println(i + " " + j + "可以走");
//                }
//            }
//        }
//    }

    private Chess updateChess = null;

    @Override
    public boolean isDoubleMove(){
        return doubleStep;
    }

    @Override
    public void setDoubleMove(boolean flag){
        this.doubleStep = flag;
    }

    @Override
    public void shengWei(String name) {
        updateChess = ChessFactory.creat(name, this.color, this.point);
        System.out.println("升级！" + updateChess);
        if(updateChess.getName().equals(Chess.KING)) return;
        this.changed = true;
        refactor(updateChess.getName());
    }

    @Override
    public void refactor(String name){
        this.name = name;
        this.path = Constants.STYLE_PATH + File.separator + Style.getStyle(style) + File.separator + name + color +".png";
        System.out.println(path);
    }

    @Override
    public void setPoint(Point point){
        if(changed) updateChess.setPoint(point);
        super.setPoint(point);
    }

}

//class myOptionPane implements Runnable, ActionListener {
//
//    JDialog dialog = new JDialog();
//
//    public myOptionPane() {
//        dialog.setLayout(new GridLayout(4,1));
//        dialog.setSize(200,400);
//        dialog.setLocationRelativeTo(null);
//    }
//    Chess c1,c2,own;
//    PanelBase panel;
//
//    public myOptionPane(Chess c1, Chess c2, Chess own, PanelBase panel) {
//        this();
//        this.c1 = c1;
//        this.c2 = c2;
//        this.own = own;
//        this.panel = panel;
//    }
//
//    public void setMyOptionPane(Chess c1, Chess c2, Chess own, PanelBase panel) {
//        this.c1 = c1;
//        this.c2 = c2;
//        this.own = own;
//        this.panel = panel;
//    }
//
//    void show(){
//        dialog.setVisible(true);
//    }
//
//    @Override
//    public void run() {
//
//        dialog.add(new JLabel("是否使用技能-吃过路兵？"));
//        JButton confirmA = new JButton("吃掉A"),
//                confirmB = new JButton("吃掉B"),
//                dismiss = new JButton("否");
//        confirmA.addActionListener(e->{
//            if(c1 != null) {
//                panel.setJustMove(new Point(c1.point.x,c1.point.y+ (c1.isUp()?1:-1) ),c1);
//                dialog.setVisible(false);
//            }
//        });
//        confirmB.addActionListener(e->{
//            if(c2!=null){
//                panel.setJustMove(new Point(c2.point.x,c2.point.y+ (c2.isUp()?1:-1) ),c2);
//                dialog.setVisible(false);
//            };
//
//        });
//        dismiss.addActionListener(e->{
//
//            dialog.setVisible(false);
//        });
//
//        dialog.add(confirmA);
//        dialog.add(confirmB);
//        dialog.add(dismiss);
//        dialog.setVisible(true);
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//
//    }
//}