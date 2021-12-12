package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MyGUI extends JFrame {
    private MenuBar menuBar;
    private Canvas canvas;
    
    public MyGUI() {
        this.setTitle("Graph");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.menuBar = new MenuBar();
        this.canvas = new Canvas(this);
        
        
        this.addMouseListener(this.canvas);
        this.addMouseMotionListener(this.canvas);
        this.addMouseWheelListener(this.canvas);
        this.addKeyListener(this.canvas);
        
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
//        Border blackline = BorderFactory.createLineBorder(Color.black, 3);
//        this.canvas.setBorder(blackline);
        this.canvas.setPreferredSize(new Dimension(800, 600));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(this.menuBar, c);
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        this.add(this.canvas, c);
        this.setJMenuBar(this.menuBar);
        this.setResizable(true);
        this.pack();
        this.setVisible(true);
    }
}
