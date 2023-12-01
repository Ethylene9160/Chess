package chess.recorder;

import chess.pieces.Chess;
import chess.pieces.ChessFactory;

import java.awt.*;

public class Record {
    private Chess currentChess, eatenChess;
    private Point startPoint, eatenPoint;
    private int x, y;
    private boolean moved, changed;
    private String name;
    private int shengweiInfo;
    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getShengweiInfo() {
        return shengweiInfo;
    }

    public void setShengweiInfo(int shengweiInfo) {
        this.shengweiInfo = shengweiInfo;
    }

    @Deprecated
    public Record(int infos[]) {
        this(
                ChessFactory.creatFromID(infos[0], new Point(infos[1], infos[2])),
                ChessFactory.creatFromID(infos[3], new Point(infos[4], infos[5])),
                new Point(infos[6],infos[7]),
                new Point(infos[8],infos[9])
        );
    }

    public Record(Chess currentChess, Chess eatenChess, Point startPoint, Point eatenPoint, int x, int y){
        this.currentChess = currentChess;
        this.eatenChess = eatenChess;
        this.startPoint = startPoint;
        this.eatenPoint = eatenPoint;
        this.x = x;
        this.y = y;
        this.moved = currentChess.moved;
        this.changed = currentChess.changed;
        this.name = currentChess.getName();
        this.shengweiInfo = 4;
    }

    public Record(Chess currentChess, Chess eatenChess, Point startPoint, Point eatenPoint) {
//        this.currentChess = currentChess;
//        this.eatenChess = eatenChess;
//        this.startPoint = startPoint;
//        this.endPoint = endPoint;
//
//        this.moved = currentChess.moved;
//        this.changed = currentChess.changed;
//        this.name = currentChess.getName();
        this(currentChess, eatenChess, startPoint, eatenPoint, currentChess.moved, currentChess.changed, currentChess.getName());
    }

    public Record(Chess currentChess, Chess eatenChess, Point startPoint, Point eatenPoint, boolean moved, boolean changed, String name) {
        this.currentChess = currentChess;
        this.eatenChess = eatenChess;
        this.startPoint = startPoint;
        this.eatenPoint = eatenPoint;
        this.x = eatenPoint.x;
        this.y = eatenPoint.y;
        this.moved = moved;
        this.changed = changed;
        this.name = name;
        this.shengweiInfo = 4;
    }

    public Chess getCurrentChess() {
        return currentChess;
    }

    public String getName(){
        return this.name;
    }

    public boolean getMoved(){
        return this.moved;
    }

    public void setCurrentChess(Chess currentChess) {
        this.currentChess = currentChess;
    }

    public Chess getEatenChess() {
        return eatenChess;
    }

    public void setEatenChess(Chess eatenChess) {
        this.eatenChess = eatenChess;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEatenPoint() {
        return eatenPoint;
    }

    public boolean getChanged(){
        return changed;
    }

    public void setEatenPoint(Point eatenPoint) {
        this.eatenPoint = eatenPoint;
    }

    private String getPoint(Point point) {
        return String.format("%d,%d", point.x, point.y);
    }

    private String getPoint(Chess c) {
        if (c == null) return String.format("%d,%d", -1, -1);
        return this.getPoint(c.getPoint());
    }

    private String getChessInfo(Chess eatenChess) {
        if (eatenChess == null) return "0,0,0";
        return eatenChess.getID() + "," + this.getPoint(eatenChess.getPoint());
    }

    private int getPieceType(){
        for (int i = 0; i < 6; i++) {
            if(Chess.PIECES[i].equals(name)) return i;
        }
        return -1;

    }

    private String getWesterStepInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append(startPoint.x).append(',').append(startPoint.y).append(',');
        sb.append(eatenPoint.x).append(',').append(eatenPoint.y).append(',');
        sb.append(0).append(";\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("%d%d%d%c%d%d%d;", this.startPoint.x, this.startPoint.y,4,';',this.x,this.y,this.shengweiInfo);
    }

    public String getChessInfo(){
        return "Current: " + currentChess +"\nEaten: " + eatenChess;
    }
}
