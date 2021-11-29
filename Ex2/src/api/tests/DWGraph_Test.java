package api.tests;
import api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Random;

public class DWGraph_Test {
    DWGraph Generate_Graph() {
        Random rnd = new Random();
        int minC = 10, maxC = 100;
        double minV = 0, maxV = 1000.0;
        int len = rnd.nextInt(maxC - minC) + minC;
        DWGraph g = new DWGraph();
        for (int i = 0; i < len; i++) {
            double x = rnd.nextDouble() * len;
            double y = rnd.nextDouble() * len;
            double z = rnd.nextDouble() * len;
            int id = i;
            Point3D p = new Point3D(x, y, z);
            Node n = new Node(i, p, 0, "", -1);
            g.addNode(n);
        }
        int maxdeg=20;
        int[] edgecount=new int[g.nodeSize()];
        for (int i = 0; i < g.nodeSize(); i++) {
            int remain=maxdeg-edgecount[i];
            if(remain<=0){
                continue;
            }
            int out= rnd.nextInt(remain);
            int in=remain-out;
            while(out>0||in>0){
                if(out>0){
                    int dest=rnd.nextInt(len-0)+0;
                    if(dest!=i&&g.getEdge(i,dest)==null){
                        out--;
                        double w = g.getNode(i).getLocation().distance(g.getNode(dest).getLocation());
                        g.connect(i,dest,w);
                        edgecount[i]++;
                        edgecount[dest]++;
                    }
                    continue;
                }
                if(in>0){
                    int src=rnd.nextInt(len-0)+0;
                    if(src!=i&&g.getEdge(src, i)==null){
                        in--;
                        double w = g.getNode(i).getLocation().distance(g.getNode(src).getLocation());
                        g.connect(src, i, w);
                        edgecount[i]++;
                        edgecount[src]++;
                    }
                    continue;
                }
            }
        }
        return g;
    }
    DWGraph Generate_Graph(int count) {
        Random rnd = new Random();
        double minV = 0, maxV = 1000.0;
        int len = count;
        DWGraph g = new DWGraph();
        for (int i = 0; i < len; i++) {
            double x = rnd.nextDouble() * len;
            double y = rnd.nextDouble() * len;
            double z = rnd.nextDouble() * len;
            int id = i;
            Point3D p = new Point3D(x, y, z);
            Node n = new Node(i, p, 0, "", -1);
            g.addNode(n);
        }
        int maxdeg=20;
        int[] edgecount=new int[g.nodeSize()];
        for (int i = 0; i < g.nodeSize(); i++) {
            int remain=maxdeg-edgecount[i];
            if(remain<=0){
                continue;
            }
            int out= rnd.nextInt(remain);
            int in=remain-out;
            int tries=0;
            while(out>0||in>0){
                if(tries>=0.5*g.nodeSize()){
                    break;
                }
                if(out>0){
                    int dest=rnd.nextInt(len-0)+0;
                    if(dest!=i&&g.getEdge(i,dest)==null){
                        out--;
                        double w = g.getNode(i).getLocation().distance(g.getNode(dest).getLocation());
                        g.connect(i,dest,w);
                        edgecount[i]++;
                        edgecount[dest]++;
                    }
                    else {
                        tries++;
                    }
                    continue;
                }
                if(in>0){
                    int src=rnd.nextInt(len-0)+0;
                    if(src!=i&&g.getEdge(src, i)==null){
                        in--;
                        double w = g.getNode(i).getLocation().distance(g.getNode(src).getLocation());
                        g.connect(src, i, w);
                        edgecount[i]++;
                        edgecount[src]++;
                    }
                    else{
                        tries++;
                    }
                    continue;
                }
            }
        }
        return g;
    }
    Node[] Generate_Nodes(){
        Node[] nodes;
        Random rnd= new Random();
        int minC =10, maxC=100;
        double minV=0, maxV=100.0;
        int len= rnd.nextInt(maxC-minC)+minC;
        nodes=new Node[len];
        for (int i = 0; i < len; i++) {
            double x=rnd.nextDouble()*len;
            double y=rnd.nextDouble()*len;
            double z=rnd.nextDouble()*len;
            int id=i;
            Point3D p=new Point3D(x,y,z);
            nodes[i]=new Node(i,p,0,"",1);
        }
        return nodes;
    }
    Node[] Generate_Nodes(int count, int startfrom){
        Node[] nodes;
        Random rnd= new Random();
        double minV=0, maxV=100.0;
        int len= count;
        nodes=new Node[len];
        for (int i = 0; i < count; i++) {
            double x=rnd.nextDouble()*len;
            double y=rnd.nextDouble()*len;
            double z=rnd.nextDouble()*len;
            int id=i;
            Point3D p=new Point3D(x,y,z);
            nodes[i]=new Node(startfrom+1, p,0,"",1);
        }
        return nodes;
    }
    @Test
    void Constructors_Test(){
        DWGraph g= this.Generate_Graph();
        DWGraph gcopy=new DWGraph(g);
        Assertions.assertEquals(true, g instanceof DWGraph);
        Assertions.assertEquals(true, gcopy instanceof DWGraph);
    }
    @Test
    void Getters_Test(){
        DWGraph g=this.Generate_Graph();
        Iterator<NodeData> nodes=g.nodeIter();
        int node=0, edge=0;
        while(nodes.hasNext()){
            node++;
            Iterator<EdgeData> edges=g.edgeIter(node-1);
            while(edges.hasNext()){
                edge++;
                Assertions.assertEquals(true, (Edge)edges.next() instanceof Edge);
            }
            Assertions.assertEquals(true, nodes.next() instanceof Node);
        }
        Assertions.assertEquals(node, g.nodeSize());
        Assertions.assertEquals(edge, g.edgeSize());
        Assertions.assertEquals(node+edge, g.getMC());
    }
    @Test
    void Add_Node_Test(){
        DWGraph g=this.Generate_Graph(20);
        Node[] extra= this.Generate_Nodes(5,20);
        for (int i = 0; i <5 ; i++) {
            Assertions.assertEquals(true, extra[i] instanceof Node);
        }
    }
}
