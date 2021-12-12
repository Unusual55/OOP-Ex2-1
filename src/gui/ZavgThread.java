package gui;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
import datastructures.DWGraph;
import datastructures.DWGraphAlgorithms;
import datastructures.Node;

import java.util.Iterator;

public class ZavgThread extends Thread{
    private DirectedWeightedGraph g;
    double Zsum;
    int Zcount;
    public ZavgThread(DirectedWeightedGraph graph){
        this.g=graph;
        this.Zcount=0;
        this.Zsum=0;
    }
    @Override
    public void run(){
        Iterator<NodeData> nodeiter=g.nodeIter();
        while(nodeiter.hasNext()){
            NodeData curr=nodeiter.next();
            Zcount++;
            Zsum+=curr.getLocation().z();
//            System.out.println("Zcount: "+Zcount+", Zsum: "+ Zsum);
        }
        System.out.println("Zavg: " + Zsum/Zcount);
    }

    public static void main(String[] args) {
        DirectedWeightedGraphAlgorithms galgo=new DWGraphAlgorithms();
        galgo.init(new DWGraph());
        galgo.load("C:\\Users\\ofrit\\IdeaProjects\\GUI2\\Data\\10000Nodes.json");
        ZavgThread zavgThread=new ZavgThread(galgo.getGraph());
        long starttime=System.currentTimeMillis();
        zavgThread.run();
        while(zavgThread.isAlive()){
            System.out.println(zavgThread.getZavg());
        }
        long endtime=System.currentTimeMillis();
        System.out.println(endtime-starttime);
        System.out.println(zavgThread.getZavg());
    }
    public synchronized double getZavg(){
        return (this.Zsum/this.Zcount);
    }
}
