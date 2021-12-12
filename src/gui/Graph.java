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
    Graph(DirectedWeightedGraphAlgorithms algo) {
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
        if(e.getKeyChar()=='h'){
            new Help();
        }
        else if(e.getKeyCode()==KeyEvent.VK_UP){
            this.gd.Scale+=0.005;
            repaint();
        }
        else if(e.getKeyCode()==40){
            this.gd.Scale-=0.005;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}