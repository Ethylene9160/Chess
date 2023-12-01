package chess.shop;

import chess.panels.WebPanel;
import chess.style.Style;
import chess.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class ShengwangShop extends Shop {
    public static final Product default_style = new Product("默认", 0, Style.DEFAULT),
                                blue_style = new Product("深邃蓝", 20, Style.DEEP_BLUE),
                                black_style = new Product("典雅黑",0,Style.DEEP_BLACK);

    public ShengwangShop(WebPanel panel) throws HeadlessException {
        super(panel);
        title = "声望";
        this.products.add(default_style);
        this.products.add(blue_style);
        this.products.add(blue_style);
        int len = products.size();
        JButton[] buttons = new JButton[len];
        this.setSize(WEIGHT, 80*len+80);
        this.setLayout(new GridLayout(len+1,3));
        add(new JLabel("风格"));
        add(new JLabel("声望"));
        add(new JLabel("确认"));
        for (int i = 0; i < len; i++) {
            add(new JLabel(new ImageIcon(Constants.STYLE_PATH + File.separator + Style.getStyle(products.get(i).style) + File.separator + "queen-1.png")));
//            add();
            add(new JLabel("\t"+products.get(i).getPrice()));
            buttons[i] = setMyButton(i);
            add(buttons[i]);
        }
    }

    @Override
    protected JButton setMyButton(int i){
        JButton b = new JButton("使用");
        b.addActionListener(e -> {
            //todo
            if(products.get(i).canBuy(wallet)){
                panel.resetStyle(products.get(i).style);
                JOptionPane.showMessageDialog(null, "成功设置为" + products.get(i).getName() + "风格！");
            }else{
                JOptionPane.showMessageDialog(null, "声望不足。","提示", JOptionPane.WARNING_MESSAGE);
            }
        });
        return b;
    }
}
