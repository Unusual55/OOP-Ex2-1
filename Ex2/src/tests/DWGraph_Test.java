package tests;
import DataStructures.*;
import api.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

public class DWGraph_Test {
    DWGraph Generate_Graph() {
        Random rnd = new Random();
        int minC = 1000000, maxC = 2000000;
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
    Edge[] Generate_Edges(){
        Edge[] edges;
        Random rnd= new Random();
        int minC =10, maxC=100;
        double minV=0, maxV=100.0;
        int len= rnd.nextInt(maxC-minC)+minC;
        edges=new Edge[len];
        for (int i = 0; i < len; i++) {
            int src= rnd.nextInt(len-1-0)+0;
            int dest=src;
            while(dest==src){
                dest=rnd.nextInt(len-1-0)+0;
            }
            double weight=rnd.nextDouble()*len;
            edges[i]=new Edge(src,dest,weight);
        }
        return edges;
    }
    Edge[] Generate_Edges(int count){
        Edge[] edges;
        Random rnd= new Random();
        int minC =10, maxC=100;
        double minV=0, maxV=100.0;
        int len= count;
        edges=new Edge[len];
        for (int i = 0; i < len; i++) {
            int src= rnd.nextInt(len-1-0)+0;
            int dest=src;
            while(dest==src){
                dest=rnd.nextInt(len-1-0)+0;
            }
            double weight=rnd.nextDouble()*len;
            edges[i]=new Edge(src,dest,weight);
        }
        return edges;
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
        DWGraph g=this.Generate_Graph(1000000);
        HashMap<Integer, NodeData> nodemap= g.getNodes();
        HashMap<Integer, HashMap<Integer, EdgeData>> edgemap=g.getEdges();
        Iterator<NodeData> nodes=g.nodeIter();
        int node=0, edge=0;
        while(nodes.hasNext()){
            node++;
            Iterator<EdgeData> edges=g.edgeIter(node-1);
            while(edges.hasNext()){
                edge++;
                Edge e=(Edge) edges.next();
                Assertions.assertEquals(true, e instanceof Edge);
                Assertions.assertEquals(true, e.equals(edgemap.get(e.getSrc()).get(e.getDest())));
            }
            Node n= (Node) nodes.next();
            Assertions.assertEquals(true, n.equals(nodemap.get(n.getKey())));
            Assertions.assertEquals(true, n instanceof Node);
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
            g.addNode(extra[i]);
            Assertions.assertEquals(true, extra[i] instanceof Node);
        }
        Assertions.assertEquals(false, 20==g.nodeSize());
    }
    @Test
    void Connect_Test(){
        DWGraph g=this.Generate_Graph(20);
        Edge[] extra=this.Generate_Edges(10);
        int precount=g.edgeSize(), premc=g.getMC();
        for (Edge e: extra) {
            g.connect(e.getSrc(),e.getDest(),e.getWeight());
            Assertions.assertEquals(true, e instanceof Edge);
        }
        Assertions.assertEquals(true, g.edgeSize()<=precount+10);
        Assertions.assertEquals(true,premc+10==g.getMC());
    }
   // @Test
    //void Remove_Node_Test() throws Exception {
//        DWGraph g=this.Generate_Graph(100);
//        Random rnd=new Random();
//        int remove=rnd.nextInt(99-0)+0;
//        Node removed= (Node) g.getNode(remove);
//        try {
//            int degree = g.EdgeFromNodeCount(remove) + g.EdgeToNodeCount(remove);
//            int premc=g.getMC();
//            int precount=g.edgeSize();
//            g.removeNode(remove);
//            Assertions.assertEquals(true, precount-degree==g.edgeSize());
//            Assertions.assertEquals(true, premc+degree+1==g.getMC());
//            Assertions.assertThrows(Exception.class, ()-> {g.EdgeFromNodeCount(remove);});
//        }
//        catch (Exception e){
//            System.out.println("There is no such node in the graph");
//        }
    //}

    @Test
    void Remove_Edge_Test(){
        DWGraph g=this.Generate_Graph(15);
        int pre=g.edgeSize();
        if(g.getEdge(0,3)==null){
            pre++;
        }
        g.connect(0,3,1.5);
        Assertions.assertEquals(pre, g.edgeSize());
        g.connect(0,3,2.6);
        Assertions.assertEquals(pre, g.edgeSize());
        g.removeEdge(0,3);
        Assertions.assertEquals(pre-1,g.edgeSize());
    }

    @Test
    void Equals_Test(){
        DWGraph g=this.Generate_Graph(30);
        DWGraph gcopy= new DWGraph(g);
        DWGraph other=this.Generate_Graph(32);
        Assertions.assertEquals(true, g.equals(gcopy));
        Assertions.assertEquals(false, g.equals(other));
    }

    @Test
    void HashCode_Test(){
        DWGraph[] graphs= new DWGraph[5];
        for (int i = 0; i < 5; i++) {
            graphs[i]=this.Generate_Graph(30);
            int hashed=Objects.hash(graphs[i].getNodes(),graphs[i].getEdges(),graphs[i].nodeSize(),graphs[i].edgeSize(),graphs[i].getMC());
            Assertions.assertEquals(hashed, graphs[i].hashCode());
        }
    }

    @Test
    void EdgeIter1_Test(){
        DWGraph g=this.Generate_Graph(20);
        g.connect(1,2,11);
        Iterator<EdgeData> edges= g.edgeIter();
        try {
            g.connect(1,2,11);
            edges.hasNext();
        }
        catch (RuntimeException e){
            Assertions.assertTrue(true);
            System.out.println("The exception was thrown as wanted");
        }
        try{
            edges.remove();
            edges.next();
        }
        catch (RuntimeException e){
            System.out.println("The exception was thrown unlike we expected");
        }
        String s = edges.next().toString();
        edges.forEachRemaining(System.out::println);
    }

    @Test
    void EdgeIter2_Test(){
        DWGraph g=this.Generate_Graph(20);
        g.connect(1,2,11);
        Iterator<EdgeData> edges= g.edgeIter(1);
//        try {
//            g.connect(1,2,121);
//            edges.hasNext();
//        }
//        catch (RuntimeException e){
//            Assertions.assertTrue(true);
//            System.out.println("The exception was thrown as wanted");
//        }
        try{
            edges.next();
            edges.remove();
            edges.next();
        }
        catch (RuntimeException e){
            System.out.println("The exception was thrown like we expected");
        }
        String s = edges.next().toString();
        edges.forEachRemaining(System.out::println);
    }
}