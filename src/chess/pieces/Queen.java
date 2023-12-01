package chess.pieces;

import chess.panels.PanelBase;

import java.awt.*;

/**
 * 皇后类<p>
 *     rules: 横、直、斜都可以走，步数不受限制，但不能越子
 * </p>
 * @author ethy9160
 */
public class Queen extends Chess{
    public Queen(String name, Point point, int player) {
        super(name, point, player);
    }

    @Override
    public boolean canMove(Point point, PanelBase panel) {
        //45°斜线且中途不能有棋子阻挡, 或者直线且中途不能有阻挡。
        return (whichLine(point) == 1 && pieceCount(1, point, panel) == 0)
                || (whichLine(point) > 1 && pieceCount(point, panel) == 0);
    }
}
