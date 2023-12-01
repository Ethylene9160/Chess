package chess.web;

import chess.panels.WebPanel;
import chess.util.CloseUtil;
import chess.util.Constants;
import chess.util.RegisterUtil;

import javax.swing.text.html.CSS;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import static chess.panels.WebPanel.*;

/**
 * 每一个客户都是一条路
 * 输入流
 * 输出流
 * 接收数据
 * 发送数据
 * <p>
 *     通过“#”来分割消息内容。
 */
public class ChessChannel implements Runnable{
    public static final int APPLY = 13, REGISTER = 12, CHANGE_NAME = 10, CHANGE_PASSWARD = 16,
                    FIND_YOU = 17, FIND_OPPONENT = 18, CHANGE_HEAD = 19, FIND_PASSWARD = 20, RESET_PASSWARD = 21,
                    SET_SIGN = 22, SHENGWANG_SHOP=23, MONEY = 24,
                    UPDATE_VIP = -1, DECREASE_MONEY = -2
    ;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean flag = true, isRegistered = false;
    private Socket clientSocekt;
    private String name = "default", password;
    private int allTimes, winTimes;
    private double winRate;
    public int ownID, targetID, findID;
    public int getOwnID(){
        return this.ownID;
    }
    public int getWinTime(){
        return this.winTimes;
    }

    public int getAllTimes(){
        return this.allTimes;
    }

    public String getName(){
        return this.name;
    }


    public String getAllInfoOfAll(){
        StringBuilder str = new StringBuilder();
        for(Integer key : ChessServer.listMap.keySet()){
            if(key != ownID) str.append(ChessServer.listMap.get(key).ownID).append("#").append(ChessServer.listMap.get(key).getName()).append("&");
        }
        return str.toString();
    }

    public ChessChannel(Socket clientSocket, int owmID) {
        this.clientSocekt = clientSocket;
        try{
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            this.ownID = owmID;
        }catch (IOException e){
            flag = false;
            CloseUtil.closeAll(inputStream, outputStream);
        }
    }

    //接收数据
    protected String receive(){
        String str = "";
        try{
            str = inputStream.readUTF();
            System.out.println("接收到："+str);
        } catch (IOException e) {
            flag = false;
            CloseUtil.closeAll(outputStream,inputStream);
            try {
                ChessServer.listMap.get(targetID).send(Character.toString(NO_PERSON_FOUND));
            }catch (Exception e1){
                System.out.println("中转服务器断开");
            }
            ChessServer.listMap.remove(ownID);
        }
        return str;
    }

    //发送数据
    protected void send(String str){
        if(str != null && str.length() != 0){
            try{
                outputStream.writeUTF(str);
                outputStream.flush();
            }catch (IOException e){
                flag = false;
                CloseUtil.closeAll(outputStream);
                ChessServer.listMap.remove(ownID);
            }
        }
    }


    /** 转发（单独发送）
     * 最近修改时间：2021年12月18日0：30
     * @author Ethylene9160
     */
    protected void transmitSingle(){
        try {
            String str = receive();
            int p1 = str.indexOf("#");
            if(str.length() < 3){
                if(str.equals("-1")){
                    System.out.println(ownID + "输了");
                    setWinCondition(targetID,ownID);
                }
                else if(str.equals("98")) ChessServer.listMap.get(ownID).send(FIND_ONLINE + getAllInfoOfAll());

            }else {
                targetID = Integer.parseInt(str.substring(0, p1));
                System.out.println(targetID);
                String position = str.substring(p1 + 1);
                //遍历集合
                if(targetID < 0){
                    switch (targetID){
                        case -1:
                            ChessServer.registers.get(ownID).uVIP();
                            break;
                        case -2:
                            ChessServer.registers.get(ownID).decreaseMoney(Integer.parseInt(position));
                            break;
                    }
                }
                else if (targetID == 11) {
                    for (Integer key : ChessServer.listMap.keySet()) ChessServer.listMap.get(key).send(SERVER_CLOSE + "");
                } else if(targetID == CHANGE_NAME){
                    //改名
                    this.name = position;
                    if(isRegistered){
                        ChessServer.registers.get(ownID).setName(this.name);
                    }
                } else if(targetID == REGISTER){
                    //登录
                    isRegistered = true;
                    String[] strs = position.split("&");
                    int newID = Integer.parseInt(strs[0]);
                    if(ChessServer.isContainsRegisterID(newID) && !ChessServer.listMap.containsKey(newID)){
                        Registers register = ChessServer.registers.get(newID);
                        if(!strs[1].equals(register.getPassword())) {
                            System.out.println("密码错误");
                            ChessServer.listMap.get(ownID).send(Character.toString(WRONG_PASSWARD));//密码错误
                        }
                        else {
                            this.name = register.getName();
                            this.winTimes = register.getWinTime();
                            this.allTimes = register.getAllTime();
                            //需要把新信息发给旧的地方
                            ChessServer.listMap.get(ownID).send(Character.toString(REGIST) + newID + "#" + name +'#' + register.getHead());
                            ChessServer.listMap.put(newID, this);
                            ChessServer.listMap.remove(ownID);
                            ownID = newID;
                            System.out.println("登陆成功！");
                            System.out.println(ChessServer.listMap);
                        }
                    }else{
                        ChessServer.listMap.get(ownID).send(Character.toString(NO_PERSON_FOUND));//查无此人
                    }
                } else if(targetID == APPLY){
                    //注册
                    System.out.println("apply-1");
                    System.out.println(ChessServer.registers);
                    int newID = ChessServer.registers.size() + 10001;
                    System.out.println("apply-2");
                    int p = position.indexOf("&");
                    ChessServer.registers.put(newID, new Registers(newID, position.substring(0,p++), position.substring(p)));
                    ChessServer.listMap.get(ownID).send( APPLY_ACCOUNT +"" + newID);
                    System.out.println("apply-3");
                }else if(targetID == 14){//退出登录
                    ChessServer.listMap.remove(ownID);
                    System.out.println("remove");
                }else if(targetID == 15){//写入档案
                    System.out.println("准备写入");
                    System.out.println(ChessServer.registers);
                    RegisterUtil.writeRegisters(Constants.CHESS_REGIST, ChessServer.registers);
                    System.out.println("写入档案");
                }else if(targetID == CHANGE_PASSWARD){
                    //改密码
                    System.out.println(name + "【改密码】");
                    String strs[] = position.split("&");
                    Registers r = ChessServer.registers.get(ownID);
                    if(strs[0].equals(r.getPassword())){
                        r.setPassword(strs[1]);
                        ChessServer.listMap.get(ownID).send(Character.toString(SUCCESS_SET_PASSWARD));
                    }else ChessServer.listMap.get(ownID).send(Character.toString(WRONG_PASSWARD));
                }else if(targetID == FIND_YOU){//查询自己
                    ChessServer.listMap.get(ownID).send(PLAYER_INFO +ChessServer.registers.get(ownID).getPlayerInfo());
                }else if(targetID==FIND_OPPONENT){//查询对手信息
                    ChessServer.listMap.get(ownID).send(PLAYER_INFO +ChessServer.registers.get(Integer.parseInt(position)).getPlayerInfo());
                }else if(targetID == CHANGE_HEAD){//改头像
                    if(isRegistered) ChessServer.registers.get(ownID).setHead(position);
                    System.out.println("改头像");
                }else if(targetID == -1){//群发消息
                    for (Integer key : ChessServer.listMap.keySet()) ChessServer.listMap.get(key).send(position);
                }else if(targetID == FIND_PASSWARD){
                    findID = Integer.parseInt(position);
                    if(ChessServer.isContainsRegisterID(findID)){
                        ChessServer.listMap.get(ownID).send(WebPanel.RESET_PASSWARD + ChessServer.registers.get(findID).getSecurityQuestions());
                    }
                }else if(targetID == ChessChannel.RESET_PASSWARD){
                    ChessServer.registers.get(findID).setPassword(position);
                    send(Character.toString(SUCCESS_SET_PASSWARD));
                }else if(targetID == SET_SIGN){
                    ChessServer.registers.get(ownID).setSignature(position);
                }else if(targetID == SHENGWANG_SHOP){
                    send(TO_SHENGWANG + "" + ChessServer.registers.get(ownID).getAllTime());
                }else if(targetID == MONEY){
                    send(GET_MONEY + "" + ChessServer.registers.get(ownID).getMoney());
                }

                else {
                    if(position.equals(Character.toString(SURREND))){
                        System.out.println(ownID + "认输");
                        setWinCondition(targetID,ownID);
                    }else if(position.equals(Character.toString(AGREE_PEACE))){
                        if(targetID > 10000) ChessServer.registers.get(targetID).loser();
                        if(ownID > 10000) ChessServer.registers.get(ownID).loser();
                    }
                    System.out.println("坐标："+position);
                    Map<Integer, ChessChannel> listMap = ChessServer.listMap;
                    listMap.get(targetID).send(position);
                }
            }
        }catch(IndexOutOfBoundsException e){
            System.out.println("ERROR!");
            e.printStackTrace();
            try {
                ChessServer.listMap.get(ownID).send(Character.toString(ERROR));
            }catch (Exception e1){
                System.out.println("中转服务器断开-IOOBE");
            }
        }
        catch(NullPointerException e){
            System.out.println("Error!");
            e.printStackTrace();
            try {
                ChessServer.listMap.get(ownID).send("b");
            }catch (Exception e1){
                System.out.println("中转服务器断开-noPointer");
                e1.printStackTrace();
            }
        }catch(Exception e){
            System.out.println("Error");
            e.printStackTrace();
            try {
                ChessServer.listMap.get(ownID).send(Character.toString(ERROR));
            }catch (Exception e1){
                System.out.println("中转服务器断开-Exception");
            }
        }
    }

    private void setWinCondition(int winner, int loser){
        if(winner > 10000)
            ChessServer.registers.get(winner).winner();
        if(loser > 10000)
            ChessServer.registers.get(loser).loser();

    }

    @Override
    public void run() {
        while(flag){
            transmitSingle();
        }
    }
}