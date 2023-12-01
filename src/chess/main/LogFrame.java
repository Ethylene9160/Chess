package chess.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class LogFrame extends JFrame implements ActionListener {
    public static final String REGISTER_BUTTON = "Reg", LOG_IN_BUTTON = "Log", FORGET_BUTTON = "Forget",
            WEB_SERVER = "127.0.0.1";
    public static final int WEB_PORT = 8888;
    private JTextField accountField, passwordField;
    private Socket clientSocket;
    public LogFrame() throws HeadlessException {
        setTitle("中国象棋-登录");
        setLayout(null);
        setSize(350,450);
        setResizable(false) ;
        JLabel accountLabel = creatLabel(25,50,50,40,"账号");
        JLabel passwordLabel = creatLabel(25,100,50,40,"密码");
        accountField = creatField(80,50,180,40,"请输入你的账号");
        passwordField = creatField(80,100,180,40,"请输入密码");
        JButton logButton = creatButton(50,150,120,40,"登录", LOG_IN_BUTTON);
        JButton registerButton = creatButton(200,150,120,40,"注册", REGISTER_BUTTON);
        JButton forgetButton = creatButton(50,200,120,40,"忘记密码", FORGET_BUTTON);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);
    }

    private JTextField creatField(int x, int y, int weight, int height, String str){
        JTextField var = new JTextField(str);
        var.setFont(new Font("宋体", Font.BOLD, 18));
        var.setBounds(x,y,weight,height);
        this.add(var);
        return var;
    }

    private JLabel creatLabel(int x, int y, int weight, int height, String str){
        JLabel var = new JLabel(str);
        var.setBounds(x,y,weight,height);
        var.setFont(new Font("宋体", Font.BOLD, 18));
        this.add(var);
        return var;
    }

    private JButton creatButton(int x, int y, int weight, int height, String str, String command){
        JButton var = new JButton(str);
        var.setBounds(x,y,weight,height);
        var.setFont(new Font("宋体", Font.BOLD, 18));
        var.addActionListener(this);
        var.setActionCommand(command);
        this.add(var);
        return var;
    }


    public static void main(String[] args) {
        new LogFrame();
    }
    public void regist(){

    }

    public void forget(){
        String account;
    }
    public void logIn() throws IOException {
        String account = accountField.getText();
        String password = passwordField.getText();
//        User user = new User(account, password);
        if(clientSocket == null){
            clientSocket = new Socket(WEB_SERVER, WEB_PORT);
        }
        //发送登录请求
//        SocketUtil.send(clientSocket, user);

        //接受服务器登陆成功信息
//        String receive = (String)SocketUtil.receive(clientSocket);
//        System.out.println(receive);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command){
            case LOG_IN_BUTTON:
                try {
                    logIn();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,"服务器不存在","警告",3);
                }
                break;
            case REGISTER_BUTTON:
                regist();
                break;
            case FORGET_BUTTON:
                forget();
                break;
        }
    }
}
