package chess.shop;

import chess.panels.WebPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class Shop extends JFrame {
    public final static  int WEIGHT = 300;
    protected int wallet;
    protected WebPanel panel;
    protected ArrayList<Product> products;
    protected JScrollPane scrollPane;
    protected JPanel mainPanel;
    protected String title;

    public Shop(WebPanel panel) throws HeadlessException {
        this.panel = panel;
        mainPanel = new JPanel();
        this.scrollPane = new JScrollPane(mainPanel);
        this.products = new ArrayList<>();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public void showThis(int wallet){
        setVisible(true);
        this.wallet = wallet;
        setTitle("余额:"+wallet);
    }


    protected abstract JButton setMyButton(int i);
}
