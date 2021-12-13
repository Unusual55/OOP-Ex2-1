package gui;

import datastructures.DWGraph;
import datastructures.DWGraphAlgorithms;
import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This panel is the main panel in the gui, it's the main screen.
 * It will contain only the Graph and the menu
 */
public class Graph extends JFrame implements KeyListener {
    GMenuBar mb;
    DirectedWeightedGraphAlgorithms graphalgo;
    DirectedWeightedGraph g;
    GraphDisplay gd;
    public Graph(DirectedWeightedGraphAlgorithms algo) {
        Dimension dimension=Toolkit.getDefaultToolkit().getScreenSize();
        double width=dimension.getWidth();
        double height=dimension.getHeight();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        graphalgo = algo;
        this.g = graphalgo.getGraph();
        this.setSize(800, 600);
        this.setBounds(0,0,800, 600);
        this.setLayout(null);
        setLocationRelativeTo(null);
        gd = new GraphDisplay(graphalgo,this);
        mb = new GMenuBar(graphalgo, gd, this);
        this.setJMenuBar(mb);
        this.add(gd);
        this.addKeyListener(this);
        this.addMouseListener(gd);
        this.addMouseMotionListener(gd);
        this.addMouseWheelListener(gd);
        this.setVisible(true);
        this.setResizable(true);
        new Help();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        /**
         * If the user clicked on 'h', the help panel will open
         */
        if(e.getKeyChar()=='h'){
            new Help();
        }
        /**
         * If the user clicked on Up Arrow and there are more than one node, it will zoom into the graph
         */
        else if(e.getKeyCode()== KeyEvent.VK_UP&&this.g.nodeSize()>0){
            this.gd.Scale+=0.005;
            repaint();
        }
        /**
         * If the user clicked on Down Arrow and there are more than one node, it will zoom out of the graph.
         */
        else if(e.getKeyCode()==40&&this.g.nodeSize()>0){
            if(this.gd.Scale-0.005>0.01) {
                this.gd.Scale -= 0.005;
                repaint();
            }
            else{
                JOptionPane.showMessageDialog(this, "You can't zoom out more than this");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
