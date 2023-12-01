package chess.web;

import chess.util.CloseUtil;
import chess.util.Constants;
import chess.util.RegisterUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 * 每一个客户都是一条路
 * 输入流
 * 输出流
 * 接收数据
 * 发送数据
 * <p>
 *     通过“#”来分割消息内容。
 */
public class MoveChannel implements Runnable{
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean flag = true, isRegistered = false;
    private Socket clientSocekt;
    private String name = "default", password;
    private int allTimes, winTimes;
    private double winRate;
    public int ownID, targetID;
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


    public static String getAllInfoOfAll(){
        String str = "";
        for(Integer key : MoveServer.listMap.keySet()){
            str = str + MoveServer.listMap.get(key).ownID + "#" + MoveServer.listMap.get(key).getName() + "&";
        }
        return str;
    }

    public MoveChannel(Socket clientSocket, int owmID) {
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
                MoveServer.listMap.get(targetID).send("96");
            }catch (Exception e1){
                System.out.println("中转服务器断开");
            }
            MoveServer.listMap.remove(ownID);
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
                MoveServer.listMap.remove(ownID);
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
                if(str.equals("1")){
                    System.out.println(ownID + "获胜");
                    setWinCondition(ownID,targetID);
                }else if(str.equals("0")){
                    System.out.println(ownID +","+targetID + "平局");
                    if(ownID>10000)
                        MoveServer.registers.get(ownID).loser();
                    if(targetID>10000)
                        MoveServer.registers.get(targetID).loser();
                }else if(str.equals("98")) MoveServer.listMap.get(ownID).send("65#" + getAllInfoOfAll());
            }else {
                targetID = Integer.parseInt(str.substring(0, p1));
                System.out.println(targetID);
                String position = str.substring(p1 + 1);
                //遍历集合
                if (targetID == 11) {
                    for (Integer key : MoveServer.listMap.keySet()) MoveServer.listMap.get(key).send("9");
                } else if(targetID == 10){
                    //改名
                    this.name = position;
                    if(isRegistered){
                        MoveServer.registers.get(ownID).setName(this.name);
                    }
                } else if(targetID == 12){
                    //登录
                    isRegistered = true;
                    String[] strs = position.split("&");
                    int newID = Integer.parseInt(strs[0]);
                    if(MoveServer.isContainsRegisterID(newID) && !MoveServer.listMap.containsKey(newID)){
                        Registers register = MoveServer.registers.get(newID);
                        if(!strs[1].equals(register.getPassword())) {
                            System.out.println("mimacuowu");
//                            MoveServer.listMap.get(ownID).send("21");//密码错误
                            MoveServer.listMap.get(ownID).send("cw");//密码错误
                        }
                        else {
                            this.name = register.getName();
                            this.winTimes = register.getWinTime();
                            this.allTimes = register.getAllTime();
                            //需要把新信息发给旧的地方
                            MoveServer.listMap.get(ownID).send("20#" + newID + "#" + name);
                            System.out.println("20#" + newID + "#" + name);
                            MoveServer.listMap.put(newID, this);
                            MoveServer.listMap.remove(ownID);
                            ownID = newID;
                            System.out.println("登陆成功！");
                            System.out.println(MoveServer.listMap);
                        }
                    }else{
                        MoveServer.listMap.get(ownID).send("98");//查无此人
                    }
                } else if(targetID == 13){
                    //注册
                    int newID = MoveServer.registers.size() + 10001;
                    MoveServer.listMap.get(ownID).send(29 + "#" + newID);
                    MoveServer.registers.put(newID, new Registers(newID));
//                    RegisterUtil.writeRegisters(Constants.REGISTER_PATH, MoveServer.registers);
                }else if(targetID == 14){//退出登录
                    MoveServer.listMap.remove(ownID);
                    System.out.println("remove");
                }else if(targetID == 15){
                    System.out.println("准备写入");
                    RegisterUtil.writeRegisters(Constants.REGISTER_PATH, MoveServer.registers);
                    System.out.println("写入档案");
                }else if(targetID == 16){
                    //改密码
                    System.out.println(name + "【改密码】");
                    MoveServer.registers.get(ownID).setPassword(position);
                    MoveServer.listMap.get(ownID).send("gm");
                }else if(targetID == 17){//查询自己
                    MoveServer.listMap.get(ownID).send("28#"+MoveServer.registers.get(ownID).getPlayerInfo());
                }else if(targetID==18){//查询对手信息
                    MoveServer.listMap.get(ownID).send("28#"+MoveServer.registers.get(Integer.parseInt(position)).getPlayerInfo());
                }
                else {
                    //兼容旧版
//                    if(position.equals("93")){
//                        position = this.ownID + "#93";
//                    }else if(position.equals("92")){
//                        position = this.ownID + "#92";
//                    }

                    if(position.equals("99")){
                        System.out.println(ownID + "超时");
                        setWinCondition(targetID,ownID);
                    }else if(position.equals("89")){
                        System.out.println(ownID + "认输");
                        setWinCondition(targetID,ownID);
                    }
                    System.out.println("坐标："+position);
                    Map<Integer, MoveChannel> listMap = MoveServer.listMap;
                    listMap.get(targetID).send(position);
                }
            }
        }catch(IndexOutOfBoundsException e){
            System.out.println("ERROR!");
            try {
                MoveServer.listMap.get(ownID).send("94");
            }catch (Exception e1){
                System.out.println("中转服务器断开");
            }
        }catch(NullPointerException e){
            System.out.println("Error!");
            try {
                MoveServer.listMap.get(ownID).send("98");
            }catch (Exception e1){
                System.out.println("中转服务器断开");
            }
        }catch(Exception e){
            System.out.println("Error");
            try {
                MoveServer.listMap.get(ownID).send("94");
            }catch (Exception e1){
                System.out.println("中转服务器断开");
            }
        }
    }

    private void setWinCondition(int winner, int loser){
        if(winner > 10000)
            MoveServer.registers.get(winner).winner();
        if(loser > 10000)
            MoveServer.registers.get(loser).loser();

    }

    @Override
    public void run() {
        while(flag){
            transmitSingle();
        }
    }
}