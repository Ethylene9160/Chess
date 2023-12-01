package chess.pieces;

import chess.panels.PanelBase;

import java.awt.*;

/**
 * 猪脚类，对应中国象棋： 象。
 * <p>
 *     rules: 和中国象棋的象一样
 * </p>
 * @author ethy9160
 */
public class Bishop extends Chess{
    public Bishop(String name, Point point, int player) {
        super(name, point, player);
    }

    @Override//已完成！-2022-4-14
    public boolean canMove(Point point, PanelBase panel) {
        //45°斜线且中途不能有棋子阻挡。
        return whichLine(point) == 1 && pieceCount(1, point, panel) == 0;
    }

    @Deprecated
    @Override
    public void moveProcess(PanelBase panel){
        super.moveProcess(panel);
    }
}
