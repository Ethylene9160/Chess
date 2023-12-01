package chess.recorder;

import chess.pieces.Chess;
import chess.pieces.ChessFactory;
import chess.util.Constants;
import chess.util.Locker;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static chess.panels.PanelBase.CHESS_TYPES;

public class Loader {
    private ArrayList<Chess> chessList;
    private ArrayList<Record> recordList;
    //    private LinkedList<Record> recordList;
    private int currentPlayer;

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    File document;

    public Loader(String fileName) {
//        chessList = new ArrayList<>();
//        recordList = new ArrayList<>();
//        creatChess();
        document = new File(Constants.LIBRARY_PATH + File.separator + fileName);
        System.out.println("FileName：" + Constants.LIBRARY_PATH + File.separator + fileName);
        StringBuilder sb = new StringBuilder();
        Scanner input = null;
        try {
            input = new Scanner(document);
            while (input.hasNext()) {
                sb.append(input.next());
            }
            sb.setLength(sb.length() - 1);
            //开始拆分。
            String fn;
            fn = fileName.substring(fileName.indexOf(".")+1);
            System.out.println("fn:"+fn);

            String info;
            if(fn.equals("eh")||fn.equals("eH")||fn.equals("EH")||fn.equals("Eh")) {
                info = Locker.getLocker(sb.toString());
                String ss[] = info.split("\n");
                info = "";
                for (int i = 0; i < ss.length; i++) {
                    info = info + ss[i];
                }
            } else info = sb.toString();
            //当前颜色

            System.out.println("info:" + info);
            String ss[] = info.split(";");


//            int[] recordsInfos = toInteger(records[i].split(","));
            //JOptionPane.showMessageDialog(null, info);

//            String[] strs = info.split("&");
//            this.currentPlayer = Integer.parseInt(strs[0]);
//            //当前棋盘
//            String[] chessesInfo = strs[1].split(";");
//            int len = chessesInfo.length;
//            for (int i = 0; i < len; i++) {
//                String chesses[] = chessesInfo[i].split(",");
////                chessList.add(ChessFactory.creatFromID(Integer.parseInt(chesses[0]),
////                        new Point(Integer.parseInt(chesses[1]), Integer.parseInt(chesses[2]))));
//                getChessFromID(Integer.parseInt(chesses[0]),
//                        new Point(Integer.parseInt(chesses[1]), Integer.parseInt(chesses[2])));
//            }
//            //下棋记录
//            String[] records = strs[2].split(";");
//            len = records.length;
//            for (int i = 0; i < len; i++) {
//                int[] recordsInfos = toInteger(records[i].split(","));
//                recordList.add(new Record(
//                        getChessFromID(recordsInfos[0], new Point(recordsInfos[1], recordsInfos[2])),
//                        (recordsInfos[3] == 0 ? null : getEatenChess(recordsInfos[3], new Point(recordsInfos[4], recordsInfos[5]))),
//                        new Point(recordsInfos[6], recordsInfos[7]), new Point(recordsInfos[8], recordsInfos[9]),
//                        recordsInfos[10] == 1, recordsInfos[11] ==1, Chess.PIECES[recordsInfos[12]])
//                );
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"没有找到文件！","Error",0);
        } catch (StringIndexOutOfBoundsException e3){
            e3.printStackTrace();
            JOptionPane.showMessageDialog(null,"遇到未知错误 读取失败！","Error",0);
        } catch (Exception e2){
            e2.printStackTrace();
            JOptionPane.showMessageDialog(null,"遇到未知错误,读取失败！","Error",0);
        }
    }

    public static String getStringInfo(String fileName){
        File document = new File(Constants.LIBRARY_PATH + File.separator + fileName);
        System.out.println("FileName：" + Constants.LIBRARY_PATH + File.separator + fileName);
        StringBuilder sb = new StringBuilder();
        Scanner input = null;
        try {
            input = new Scanner(document);
            while (input.hasNext()) {
                sb.append(input.next());
            }
            sb.setLength(sb.length() - 1);
            //开始拆分。
            String fn;
            fn = fileName.substring(fileName.indexOf(".")+1);
            System.out.println("fn:"+fn);

            String info;
            if(fn.equals("eh")||fn.equals("eH")||fn.equals("EH")||fn.equals("Eh")) {
                info = Locker.getLocker(sb.toString());
//                String ss[] = info.split("\n");
//                info = "";
//                for (int i = 0; i < ss.length; i++) {
//                    info = info + ss[i];
//                }
            } else info = sb.toString();
            return info;
            //当前颜色

//            System.out.println("info:" + info);
//            String ss[] = info.split(";");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"没有找到文件！","Error",0);
        } catch (StringIndexOutOfBoundsException e3){
            e3.printStackTrace();
            JOptionPane.showMessageDialog(null,"遇到未知错误 读取失败！","Error",0);
        } catch (Exception e2){
            e2.printStackTrace();
            JOptionPane.showMessageDialog(null,"遇到未知错误,读取失败！","Error",0);
        }
        return null;
    }

    public ArrayList<Chess> getChessList() {
        return chessList;
    }

    public ArrayList<Record> getRecordList() {
        System.out.println("ck: getRecordList: " + recordList);
        return recordList;
    }

    public static int[] toInteger(String[] strs) {
        int len = strs.length;
        int[] ints = new int[len];
        for (int i = 0; i < len; i++) {
            ints[i] = Integer.parseInt(strs[i]);
        }
        return ints;
    }

    public void creatChess() {
        int len = CHESS_TYPES.length;
        Chess.setIDBase(0);
        for (int i = 0; i < len; i++) {
//            chessList.add(ChessFactory.creat(CHESS_TYPES[i], -1, POINTS[i]));
            chessList.add(ChessFactory.creat(CHESS_TYPES[i], -1, new Point(-1,-1)));
            Chess c = ChessFactory.creat(CHESS_TYPES[i], 1, new Point(-1,-1));
//            Chess c = ChessFactory.creat(CHESS_TYPES[i], 1, POINTS[i]);
            c.reverse();
            chessList.add(c);
        }
    }

    public Chess getChessFromID(int ID, Point point){
        for(Chess c : chessList){
            if(c.getID() == ID) {
                c.setPoint(point);
                return c;
            }
        }
        return null;
    }
    private Chess getEatenChess(int ID, Point point){
        Chess c = getChessFromID(ID, point);
        if(c != null)chessList.remove(c);
        return c;
    }
}
