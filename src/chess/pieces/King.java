package chess.pieces;

import chess.panels.PanelBase;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.awt.*;

/**
 * 王类<p>
 *     rules: 横、直、斜都可以走，但每次限走一步。王是不可以送吃的，即任何被敌方控制的格子，己方王都不能走进去。否则，算“送王”犯规，三次就要判负。
 *<p>
 * （1）除易位时外，王可走到不被对方棋子攻击的任何相邻格子，而且只能走一步（着）。
 * <p>（2）易位是由王和己方任何一个车一起进行的仍被视作王的一步（着）的走法。
 * </p>
 */
public class King extends Chess{
    public King(String name, Point point, int player) {
        super(name, point, player);
    }
    public static final int SHORT_VI = 8, LONG_VI = 9;
    @Override
    public boolean canMove(Point point, PanelBase panel) {

        int step = getStep(point);
//        if(point.x > 7) {
//            if(moved) {
//                System.out.println("has moved!");
//                return false;
//            }
//            switch (point.x) {
//                //现在只假设只有isUp情况
//                case SHORT_VI://短换位,00,01,02,03,车到04.
//                    System.out.println("short change");
//                    Point p00 = new Point(0,0), p20 = new Point(2,0), p10 = new Point(1,0);
//                    if(!isUp()) {
//                        PanelBase.reversePoint(p00);
//                        PanelBase.reversePoint(p10);
//                        PanelBase.reversePoint(p20);
//                    }
//                    if (panel.getChessFromPoint(p00) == null || panel.getChessFromPoint(p00).moved) {
//                        System.out.println("condition 1");
//                        return false;
//                    }
//                    if (panel.getChessFromPoint(p10) != null || panel.getChessFromPoint(p20) != null) {
//                        System.out.println("condition 2");
//                        return false;
//                    }
//                    if (isAttacked(panel) || isThisAttacked(panel, p20) || isThisAttacked(panel, p10)) return false;
//                    System.out.println("短换位置");
//                    return true;
//                case LONG_VI://长换位,短换位07,06,05,04,03,车到04.
//                    System.out.println("long change");
//                    Point p70 = new Point(7,0), p60 = new Point(6,0), p50 = new Point(5,0), p40 = new Point(4,0);
//                    if(!isUp()) {
//                        PanelBase.reversePoint(p70);
//                        PanelBase.reversePoint(p60);
//                        PanelBase.reversePoint(p50);
//                        PanelBase.reversePoint(p40);
//                    }
//                    if (panel.getChessFromPoint(p70) == null || panel.getChessFromPoint(p70).moved) {
//                        System.out.println("condition 1");
//                        return false;
//                    }
//                    if (panel.getChessFromPoint(p40) != null || panel.getChessFromPoint(p50) != null || panel.getChessFromPoint(p60) != null) {
//                        System.out.println("condition 2");
//                        return false;
//                    }
//                    if (isAttacked(panel) || isThisAttacked(panel, p40) || isThisAttacked(panel, p50)) return false;
//                    System.out.println("chang换位置");
//                    return true;
//            }
//        }

        //判断王车易位
        if(step == 2 && point.y == this.point.y){
            if(this.moved) return false;
            Chess che;
            if(this.point.x + point.x > 7) che = panel.getChessFromPoint(new Point(7, this.point.y));
            else che = panel.getChessFromPoint(new Point(0, this.point.y));

            if(che == null || che.moved) return false;//车被移动了
            if(pieceCount(3, che.getPoint(), panel) != 0) return false;//中间有棋子阻挡

            if(isAttacked(panel)) return false;//当前位置王被攻击
            if(isThisAttacked(panel, point))return false;//目标位置王被攻击
            if(isThisAttacked(panel, new Point((this.point.x + point.x)/2, point.y)))return false;//中间那个点王被攻击

            System.out.println("可以王车易位");
            return true;

        }
        //其他情况，只能走一步
        return step == 1;
    }

//    @Deprecated
//    private boolean canChangePosition(){
//        return false;
//    }
//
//    @Deprecated
//    @Override
//    public void moveProcess(PanelBase panel) {
//        panel.drawHint(new Point(this.point.x+1, this.point.y+1));
//        panel.drawHint(new Point(this.point.x+1, this.point.y));
//        panel.drawHint(new Point(this.point.x+1, this.point.y-1));
//        panel.drawHint(new Point(this.point.x, this.point.y+1));
//        panel.drawHint(new Point(this.point.x, this.point.y-1));
//        panel.drawHint(new Point(this.point.x-1, this.point.y+1));
//        panel.drawHint(new Point(this.point.x-1, this.point.y));
//        panel.drawHint(new Point(this.point.x-1, this.point.y-1));
//    }

    @Override
    public boolean isAttacked(PanelBase panel){
        for(Chess c:panel.chessList){
            if(c.getColor() != this.color && c.canMove(this.point, panel)){
                System.out.println("被将军了！！");
                return true;
            }
        }
        return false;
    }

    private boolean isThisAttacked(PanelBase panel, Point point){
        for(Chess c:panel.chessList){
            if(c.getColor() != this.color && c.canMove(point, panel)){
                System.out.println("被将军了！！");
                return true;
            }
        }
        return false;
    }

    @Deprecated
    private void wangCheYiWei(PanelBase panel){
        if(moved || isAttacked(panel)) return;
        //短：
        Chess cDuan = panel.getChessFromID(this.color == Chess.FIRST_COLOR ? 0:1);
        Chess cChang = panel.getChessFromID(this.color == Chess.FIRST_COLOR ? 2:3);
        boolean duan, chang;
        if(this.color == Chess.FIRST_COLOR){
            //1. 没有移动过，且中间不能被棋子挡住
            duan = cDuan != null && !cDuan.moved && panel.getChessFromPoint(new Point(1,this.point.y)) == null && panel.getChessFromPoint(new Point(2,this.point.y)) == null;
            chang = cChang != null && !cChang.moved && panel.getChessFromPoint(new Point(4,this.point.y)) == null && panel.getChessFromPoint(new Point(5,this.point.y)) == null  && panel.getChessFromPoint(new Point(6,this.point.y)) == null;

            //2. 移动后不能被将军
            for(Chess c : panel.chessList){
                if(duan && c.canMove(cDuan.getPoint(), panel)) duan = false;
                if(chang && c.canMove(cChang.getPoint(), panel)) chang = false;
            }
        }else{
            duan = cDuan != null && !cDuan.moved && panel.getChessFromPoint(new Point(5,this.point.y)) == null && panel.getChessFromPoint(new Point(6,this.point.y)) == null;
            chang = cChang != null && !cChang.moved && panel.getChessFromPoint(new Point(1,this.point.y)) == null && panel.getChessFromPoint(new Point(2,this.point.y)) == null  && panel.getChessFromPoint(new Point(3,this.point.y)) == null;

            //2. 移动后不能被将军
            for(Chess c : panel.chessList){
                if(duan && c.canMove(cDuan.getPoint(), panel)) duan = false;
                if(chang && c.canMove(cChang.getPoint(), panel)) chang = false;
            }
        }
    }
}
