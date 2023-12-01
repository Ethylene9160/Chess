package chess.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConnectFrame extends JFrame {
    public static void main(String[] args) {
        new ConnectFrame();
    }
    public ConnectFrame() throws HeadlessException {
        setTitle("游戏大厅");
        setLocationRelativeTo(null);
        setSize(600,400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        String[] labels = {
                "A","B","C"
        };
        final DefaultListModel model = new DefaultListModel();
        for(int i = 0; i < labels.length; i++){
            model.addElement(labels[i]);
        }
        JList list = new JList(model);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount() == 2){
                    System.out.println("shuangji" + list.getSelectedIndex());
                }
            }
        });
        list.setFont(new Font("楷体", Font.BOLD, 20));
        JScrollPane scrollPane = new JScrollPane(list);
        this.add(scrollPane);

        setVisible(true);
    }
}
