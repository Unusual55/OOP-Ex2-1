package api.tests;
import api.Edge;
import api.Node;
import api.Point3D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;
import java.util.Objects;
import java.util.Random;

public class Edge_Test {
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
    @Test
    void Constructor_Test(){
        Edge e1=new Edge(1,3,1.555,"Happy Edge1", 1);
        Edge e2=new Edge(4,5,0.345);
        Assertions.assertEquals(true,e1 instanceof Edge);
        Assertions.assertEquals(true, e2 instanceof Edge);
        Edge ecopy=new Edge(e1);
        Assertions.assertEquals(true, ecopy instanceof Edge);
    }
    @Test
    void Getters_Test(){
        Edge e1=new Edge(1,3,1.555,"Happy Edge1", 1);
        Assertions.assertEquals(1,e1.getSrc());
        Assertions.assertEquals(3,e1.getDest());
        Assertions.assertEquals(1.555,e1.getWeight());
        Assertions.assertEquals("Happy Edge1", e1.getInfo());
        Assertions.assertEquals(1, e1.getTag());
    }
    @Test
    void Setters_Test(){
        Edge e1=new Edge(1,3,1.555,"Happy Edge1", 1);
        Assertions.assertEquals(1,e1.getSrc());
        Assertions.assertEquals(3,e1.getDest());
        Assertions.assertEquals(1.555,e1.getWeight());
        Assertions.assertEquals("Happy Edge1", e1.getInfo());
        Assertions.assertEquals(1, e1.getTag());
        e1.setTag(2);
        Assertions.assertEquals(2,e1.getTag());
        e1.setInfo("Unhappy Edge");
        Assertions.assertEquals("Unhappy Edge", e1.getInfo());
    }
    @Test
    void ToString_Test(){
        Edge[] edges = this.Generate_Edges();
        for (int i = 0; i < edges.length; i++) {
            String str= ""+edges[i].getSrc()+"->"+edges[i].getDest()+" ("+edges[i].getWeight()+")";
            Assertions.assertEquals(str,edges[i].toString());
        }
    }
    @Test
    void Equals_Test(){
        Edge[] edges=this.Generate_Edges();
        for (int i = 0; i <edges.length ; i++) {
            for (int j = i; j <edges.length ; j++) {
                if(i==j){
                    Assertions.assertEquals(true,edges[i].equals(edges[j]));
                    continue;
                }
                Assertions.assertEquals(false,edges[i].equals(edges[j]));
            }
        }
    }
    @Test
    void HashCode_Test(){
        Edge[] edges=this.Generate_Edges();
        for (int i = 0; i <edges.length ; i++) {
            int hashed= Objects.hash(edges[i].getSrc(),edges[i].getDest(),edges[i].getWeight(),edges[i].getInfo(),edges[i].getTag());
            Assertions.assertEquals(hashed, edges[i].hashCode());
        }
    }
}
