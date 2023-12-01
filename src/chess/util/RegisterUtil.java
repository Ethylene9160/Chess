package chess.util;

import chess.web.Registers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class RegisterUtil {
    public static HashMap<Integer, Registers> getRegisters(String fileName){
        HashMap<Integer, Registers> registers = new HashMap<>();
        File file = new File(fileName);
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
//        String sb = "";
        try{
            Scanner input = new Scanner(new File(fileName));
            while(input.hasNext()){
                sb.append(input.next());
            }
            sb.setLength(sb.length()-1);
//            sb.deleteCharAt(0);
            //获取每一个用户的信息
            String[] infos = sb.toString().split("#");
            for(String info : infos){
                String baseInfo[] = info.split("&");
//                System.out.println(Arrays.toString(baseInfo));
                registers.put(Integer.parseInt(baseInfo[0]),
                        new Registers(
                                Integer.parseInt(baseInfo[0]),//ID
                                Integer.parseInt(baseInfo[1]),//all times
                                Integer.parseInt(baseInfo[2]),//win times
                                baseInfo[3], baseInfo[4], baseInfo[5], baseInfo[6], baseInfo[7],baseInfo[8],
                                Integer.parseInt(baseInfo[9]), Integer.parseInt(baseInfo[10]))
                );
            }
        }catch(Exception e){
            System.out.println("ERROR!");
            e.printStackTrace();
            return null;
        }
        System.out.println(registers);
        return registers;
    }

    public static void writeRegisters(String fileName, Map<Integer, Registers> registers){
        File file = new File(fileName + "1");
        String registerInfos = "";
        System.out.println(registers);
        for(Integer key : registers.keySet()){
            registerInfos = registerInfos + registers.get(key).toString();
        }
        System.out.println(registerInfos);
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName+ "1"));
            writer.write(registerInfos);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
