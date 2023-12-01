package chess.recorder;

import chess.pieces.Chess;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Regreter {
    Record record;
    public Regreter(LinkedList<Record> recordList){
        this.record = recordList.pollLast();
    }
    public Regreter(ArrayList<Record> records){
        this.record = records.get(records.size()-1);
        records.remove(records.size()-1);
    }

    public Chess loadCurrentChess(){
        record.getCurrentChess().changed = record.getChanged();
        record.getCurrentChess().moved = record.getMoved();
        record.getCurrentChess().setDoubleMove(record.isFlag());
        if(!record.getCurrentChess().getName().equals(record.getName())) record.getCurrentChess().setName(record.getName());
        return record.getCurrentChess();
    }

//    public boolean loadFlag(){
//        return record.isFlag();
//    }

    public Chess loadEatenChess(){
        return record.getEatenChess();
    }

    public Point loadStartPoint(){
        return record.getStartPoint();
    }

    public Point loadEatenPoint(){
        return record.getEatenPoint();
    }
}
