package chess.recorder;

import chess.pieces.Chess;
import chess.util.Constants;
import chess.util.Locker;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Saver {

    private static String getListInfo(List<Record> recordList) {
        StringBuilder sb = new StringBuilder();
        for (Record r : recordList) sb.append(r);
//        for (Record r : recordList) sb.append(r).append("\n");
        return sb.toString();
    }

    public static void saveC(int currentMax, List<Record> recordList, ArrayList<Chess> chessList) {
//        String saveInfo = new Board(chessList).toString();
//        saveInfo = saveInfo + getListInfo(recordList);

        String saveInfo = getListInfo(recordList);
//
//        String[] ss = saveInfo.split("\n");
//        int p = ss.length;
//        StringBuilder saveInfoBuilder = new StringBuilder();
//        for(int i = 0; i < p; i++){
//            saveInfoBuilder.append(ss[i]);
//        }
//        saveInfo = saveInfoBuilder.toString();


//        String fileName = Constants.LIBRARY_PATH + "\\Document" + (++currentMax) + ".txt";
        String fileName = Constants.LIBRARY_PATH + "\\Document" + (++currentMax) + ".eh";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            saveInfo = Locker.setLocker(saveInfo);
            writer.write(saveInfo);
            writer.close();
            JOptionPane.showMessageDialog(null,"保存成功。文件保存在：\n【Document" + (currentMax) + ".eh】下。");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"遇到未知问题保存失败。","Error", JOptionPane.ERROR_MESSAGE);
        }
//        count();
    }
}

class Board {
    ArrayList<Chess> chessList;

    /**
     * 每一颗棋子，用“;”区分
     * 这个list后，用“&”分割。
     * @param chessList 当前棋子状况
     */
    public Board(ArrayList<Chess> chessList){
        this.chessList = chessList;
    }

    private String getListInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append(chessList.size()%2==0? "1":"-1").append('&');
        for(Chess chess : chessList){
            sb.append(chess.getID()).append(',');
//            sb.append(chess.getColor()).append(',');
            sb.append(chess.getPoint().x).append(',');
            sb.append(chess.getPoint().y).append(';');
        }
        return sb.toString();
    }

    @Override
    public String toString(){
        return String.format("%s%c\n", getListInfo(), '&');
    }

}


