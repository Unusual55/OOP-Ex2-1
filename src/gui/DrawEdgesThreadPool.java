package gui;

import api.DirectedWeightedGraph;

import java.awt.*;
import java.util.HashMap;

public class DrawEdgesThreadPool {
    DirectedWeightedGraph graph;
    GraphDisplay graphDisplay;
    HashMap<Integer, DrawMyEdgesThread> drawthreads;
    Graphics g;
    public DrawEdgesThreadPool(GraphDisplay gd, Graphics g){
        this.graph=gd.graph;
        this.g=g;
        this.drawthreads=new HashMap<>();
    }
}
