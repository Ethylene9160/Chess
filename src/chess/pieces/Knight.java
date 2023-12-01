package chess.pieces;

import chess.panels.PanelBase;

import java.awt.*;

/**
 * 骑士类（马）
 * <p>
 *     rules: 每步棋先横走或直走一格，然后再往外斜走一格；或者先斜走一格，最后再往外横走或竖走一格（即走“日”字）。可以越子，没有中国象棋中的“蹩马腿”限制。
 * </p>
 * @author ethy9160
 */
public class Knight extends Chess{
    public Knight(String name, Point point, int player) {
        super(name, point, player);
    }

    @Override
    public boolean canMove(Point point, PanelBase panel) {
        //走日字
        return whichLine(point) < -1;
    }

    @Deprecated
    @Override
    public void moveProcess(PanelBase panel){
        panel.drawHint(new Point(this.point.x - 2, this.point.y -1));
        panel.drawHint(new Point(this.point.x - 2, this.point.y +1));
        panel.drawHint(new Point(this.point.x + 2, this.point.y -1));
        panel.drawHint(new Point(this.point.x + 2, this.point.y +1));

        panel.drawHint(new Point(this.point.x + 1, this.point.y -2));
        panel.drawHint(new Point(this.point.x + 1, this.point.y + 2));
        panel.drawHint(new Point(this.point.x - 1, this.point.y + 2));
        panel.drawHint(new Point(this.point.x - 1, this.point.y -2));
    }
}
