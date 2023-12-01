package chess.shop;

import chess.panels.WebPanel;
import chess.style.Style;
import chess.util.Constants;
import chess.web.ChessChannel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class MoneyShop extends Shop {
    public static final Product VIP1 = new Product("VIP升级", 198, null),
            reSignature = new Product("修改个签至20字", 18, null);
    private int[] infos = {ChessChannel.UPDATE_VIP, ChessChannel.DECREASE_MONEY,
            ChessChannel.DECREASE_MONEY, ChessChannel.DECREASE_MONEY, ChessChannel.DECREASE_MONEY};

    private JPanel jPanel = new JPanel();
    public MoneyShop(WebPanel panel) throws HeadlessException {
        super(panel);
        title = "金币";
        products.add(VIP1);
        products.add(reSignature);
        products.add(new Product("测试1", 0,null));
        products.add(new Product("测试2", 0,null));
        products.add(new Product("测试3", 0,null));
        int len = products.size();
        JButton[] buttons = new JButton[len];
        setSize(WEIGHT,60*len+60);
        mainPanel.setSize(WEIGHT,60*len + 60);
        add(scrollPane);

        mainPanel.setLayout(new GridLayout(len+1,3));
        mainPanel.add(new JLabel("商品名"));
        mainPanel.add(new JLabel("金币"));
        mainPanel.add(new JLabel("确认"));
        for (int i = 0; i < len; i++) {
            mainPanel.add(new JLabel(products.get(i).getName()));
            mainPanel.add(new JLabel("\t"+products.get(i).getPrice()));
            buttons[i] = setMyButton(i);
            mainPanel.add(buttons[i]);
        }
    }

    @Override
    protected JButton setMyButton(int i){
        JButton b = new JButton("购买");
        b.setSize(WEIGHT/3, 70);
        b.addActionListener(e -> {
            if(products.get(i).canBuy(wallet)){
                panel.send(infos[i] + "#" + products.get(i).getPrice());
                if(i==1) panel.signature(20);
                JOptionPane.showMessageDialog(null, "成功购买" + products.get(i).getName() + "，花费" + products.get(i).getPrice());
                this.wallet -= products.get(i).getPrice();
                setTitle("余额："+wallet);
            }else JOptionPane.showMessageDialog(null, "金币不足。","提示", JOptionPane.WARNING_MESSAGE);
        });
        return b;
    }
}
