package chess.pieces;

import chess.panels.PanelBase;

import java.awt.*;

/**
 * 战车类<p>
 *     rules: 横、竖均可以走，步数不受限制，不能斜走。除王车易位外不能越子。
 * </p>
 * @author ethy9160
 */
public class Rook extends Chess{
    public Rook(String name, Point point, int player) {
        super(name, point, player);
    }

    @Override//已完成！2022-4-14
    public boolean canMove(Point point, PanelBase panel) {
        //直走且中途不能被挡住
        return whichLine(point) > 1 && pieceCount(point, panel) == 0;
    }

    @Deprecated
    @Override
    public void moveProcess(PanelBase panel){
        super.moveProcess(panel);
    }
}
