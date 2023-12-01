package chess.panels;

import chess.util.Constants;
import chess.web.ChessChannel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

public class WebButtons extends JPanel implements ActionListener, KeyListener {
    private JButton connectButton, registerButton, regretButton, surrenedButton,
            peaceButton, nameButton, passwardButton, setHeadButton, sendButton,
            settingButton, styleButton, moneyButton;
    public static final String CONNECT = "CN", REGISTER = "RGS", REGRET = "RG",
            SURRENED = "SR", PEACE = "P", NAME = "N", PASSWARD = "PW", SET_HEAD = "SH", SEND = "Sd",
            SETTING = "SETS", STYLE_BUT = "STLE", MONEY = "moN";

    private JButton buttons[];
    private WebPanel  webPanel;
    private ChangePassWard passWard;
    private SettingFrame settingFrame;
    private JTextField textField = new JTextField(10);

    public WebButtons(WebPanel panel) {
        this();
        this.webPanel = panel;
        textField.addKeyListener(this);
        passWard = new ChangePassWard(panel);
    }

    public WebButtons() {
        setBackground(Color.blue);
        setSize(Constants.FRAME_LENGTH - PanelBase.BOARD_WEIGHT,Constants.FRAME_HEIGHT);
        passWard = new ChangePassWard();
        setLayout(new GridLayout(5,2));
        connectButton = setBut("连接", CONNECT);
        registerButton = setBut("登录", REGISTER);
        regretButton = setBut("悔棋", REGRET);
        surrenedButton = setBut("认输", SURRENED);
        peaceButton = setBut("求和", PEACE);
        settingButton = setBut("设置", SETTING);

        styleButton = setBut("声望超市", STYLE_BUT);
        moneyButton = setBut("金币商城",MONEY);
        nameButton = setBut("修改昵称", NAME);
        passwardButton = setBut("修改密码", PASSWARD);
        setHeadButton = setBut("修改头像", SET_HEAD);


        sendButton = setBut("发送", SEND);
        buttons = new JButton[]{connectButton, registerButton, regretButton, surrenedButton,
                peaceButton, /*nameButton, passwardButton, setHeadButton*/ settingButton};
        for (int i = 0; i < buttons.length; ) {
            this.add(buttons[i++]);
        }


        this.settingFrame = new SettingFrame(nameButton, passwardButton, setHeadButton, styleButton, moneyButton);
        add(textField);
        add(sendButton);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        frame.setSize(200, 200);
        frame.add(new WebButtons());
        frame.setVisible(true);
    }

    private void init() {
        this.setLayout(new GridLayout(6, 2));

    }

    private JButton setBut(String name, String cmd) {
        JButton button = new JButton(name);
        button.setFocusPainted(false);
        button.setActionCommand(cmd);
        button.addActionListener(this);
        return button;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case REGISTER:
                System.out.println("登录");
                webPanel.registerDialog.setVisible(true);
                break;
            case REGRET:
                System.out.println("悔棋");
                if(!webPanel.isYou())
                    webPanel.send(WebPanel.APPLY_REGRET);
                break;
            case CONNECT:
                System.out.println("连接");
                webPanel.connect();
                break;
            case SURRENED:
                System.out.println("认输");
                webPanel.surrend();
                break;
            case PEACE:
                System.out.println("求和");
                webPanel.send(WebPanel.APPLY_PEACE);
                break;
            case MONEY:
                if(webPanel.isRegiserd()) webPanel.send(ChessChannel.MONEY+"#");
//                webPanel.send(ChessChannel.SHENGWANG_SHOP + "#");
                else JOptionPane.showMessageDialog(null, "登录以使用【金币商城】");
                break;
            case NAME:
                System.out.println("更名");
                String str = JOptionPane.showInputDialog(null, "请输入新名字。\n名字长度不应大于6，不得包含“#”或者“&”。", "更名", JOptionPane.INFORMATION_MESSAGE);
                if (str != null && !str.isEmpty())
                    if (str.contains("#") || str.contains("&") || str.length() > 6)
                        JOptionPane.showMessageDialog(null, "输入不合法，请检查：\n名字长度不应大于6，不得包含“#”或者“&”。", "错误", JOptionPane.ERROR_MESSAGE);
                    else webPanel.setYourName(str);
                break;
            case PASSWARD:
                System.out.println("改密码");
                if(webPanel.isRegiserd()) passWard.setVisible(true);
                break;
            case SET_HEAD:
                System.out.println("改头像");

                int info = -1;
                String header=null;
                while(info != 0) {
                    header = (String) JOptionPane.showInputDialog(null, "请选择您的头像", "更改头像", JOptionPane.QUESTION_MESSAGE, null, Constants.HEADERS, Constants.HEADERS[0]);
                    if (header != null) info = JOptionPane.showConfirmDialog(null, "你的头像将被修改成这样。", "预览", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(Constants.HEAD_PATH + File.separator + header));
                    else return;
                }

                JOptionPane.showMessageDialog(null, "您的头像已更改。","更改头像",JOptionPane.ERROR_MESSAGE,new ImageIcon(Constants.HEAD_PATH + File.separator + header));
                webPanel.setYourHead(header);
                if (webPanel.isRegiserd()) webPanel.send(header, Integer.toString(ChessChannel.CHANGE_HEAD));

                break;
            case SEND:
                System.out.println("sendMessage");
                webPanel.sendMessage(textField.getText());
                textField.setText("");
                break;
            case SETTING:
                settingFrame.setVisible(true);
                break;
            case STYLE_BUT:
                if(webPanel.isRegiserd()){
                    webPanel.send(ChessChannel.SHENGWANG_SHOP + "#");
//                    webPanel.resetStyle(Chess.style == Style.DEEP_BLUE? Style.DEFAULT:Style.DEEP_BLUE);
                }else JOptionPane.showMessageDialog(null, "登录以使用【声望超市】");
                //todo
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("sendMessage - keyBoard");
            webPanel.sendMessage(textField.getText());
            textField.setText("");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

class ChangePassWard extends JFrame{
    private JTextField oldT, newT;
    private JButton confirm = new JButton("确定"), quxiao = new JButton("取消");

    public ChangePassWard(){
        oldT = new JTextField();
        newT = new JTextField();
        setSize(180,135);
        setResizable(false);
        setLocationRelativeTo(null);
        this.setLayout(new GridLayout(3, 2));

        this.add(new JLabel("  旧密码："));
        this.add(oldT);
        this.add(new JLabel("  新密码："));
        this.add(newT);
        this.add(confirm);
        this.add(quxiao);

        quxiao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
    }

    public ChangePassWard(WebPanel panel) {
        this();
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(newT.getText().contains("&") || newT.getText().contains("#")){
                    JOptionPane.showMessageDialog(null, "密码含有非法字符","ERROR", JOptionPane.ERROR_MESSAGE);
                }else{
                    panel.send("16" + "#" + oldT.getText() +'&' +  newT.getText());
                    setVisible(false);
                }
            }
        });
        setVisible(false);
    }

}

class SettingFrame extends JFrame{
    private JButton passwardButton, nameButton, headButton,styleButton;
    private JButton[] buttons;

    @Deprecated
    public SettingFrame(JButton passwardButton, JButton nameButton, JButton headButton, JButton styleButton) throws HeadlessException {
        this.passwardButton = passwardButton;
        this.nameButton = nameButton;
        this.headButton = headButton;
        this.styleButton = styleButton;
        setTitle("设置");

        this.setLayout(new GridLayout(3,2));
        this.add(new JLabel("                               设"));
        this.add(new JLabel(" 置"));
        add(nameButton);
        add(headButton);
        add(styleButton);
        add(passwardButton);

        setLocationRelativeTo(null);
        setResizable(false);
        setSize(250,250);
    }

    public SettingFrame(JButton... buttons){
        this.buttons = buttons;
        setTitle("设置");
        int row = buttons.length/2 + buttons.length%2 + 1;
        this.setLayout(new GridLayout(row,2));
        this.add(new JLabel("                               设"));
        this.add(new JLabel(" 置"));
        for (JButton button : buttons) {
            add(button);
        }
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(240,65*row);
    }
}

