package chess.pieces;

import chess.panels.PanelBase;

import java.awt.*;

public class ChessFactory {
    private ChessFactory(){

    }


    @Deprecated
    public static Chess creatFromID(int ID, Point point){
        Chess chess = creat(PanelBase.CHESS_TYPES[(ID-1)/2], (ID%2 == 0? 1: -1), point);
        chess.setID(ID);
        System.out.println("Creat From ID :" + chess);
        return chess;
    }

    /**
     *
     * @param name
     * @param color
     * @param point
     * @return Chess
     */
    @SuppressWarnings("can be update to eumn class")
    public static Chess creat(String name, int color, Point point){
        if (Chess.KING.equals(name))        return new King(name, point, color);
        else if (Chess.BISHOP.equals(name)) return new Bishop(name, point, color);
        else if (Chess.KNIGHT.equals(name)) return new Knight(name, point, color);
        else if (Chess.ROOK.equals(name))   return new Rook(name, point, color);
        else if(Chess.PAWN.equals(name))    return new Pawn(name, point, color);
        else if(Chess.QUEEN.equals(name))   return new Queen(name, point, color);
        return null;
    }

}
