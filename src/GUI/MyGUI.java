package GUI;

import javax.swing.*;
import java.awt.*;

public class MyGUI extends JFrame {
    private MenuBar menuBar;
    private Canvas canvas;
    
    public MyGUI() {
        this.setTitle("Graph");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.menuBar = new MenuBar();
        this.canvas = new Canvas(this);
        
        this.setJMenuBar(this.menuBar);
        this.add(this.canvas);
        this.pack();
        
        this.addMouseListener(this.canvas);
        this.addMouseMotionListener(this.canvas);
        this.addMouseWheelListener(this.canvas);
        this.addKeyListener(this.canvas);
    
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        
        this.canvas.repaint();
        
        this.setVisible(true);
        this.setResizable(true);
//        this.frame = new JFrame();
//        this.canvas = new JPanel();
//        this.frame.setTitle("Graph");
//        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.frame.setSize(800, 600);
//        this.frame.setLocationRelativeTo(null);
////        this.frame.getContentPane().setBackground(Color.decode("#282C34"));
//        this.frame.setVisible(true);
    }
}
