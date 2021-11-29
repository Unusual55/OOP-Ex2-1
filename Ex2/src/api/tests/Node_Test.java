package api.tests;
import api.Point3D;
import api.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.AbstractDocument;
import java.util.Objects;
import java.util.Random;

public class Node_Test {
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
    @Test
    void Constructor_test(){
        Node[] nodes=this.Generate_Nodes();
        Node[] copy=new Node[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            Assertions.assertEquals(true,nodes[i] instanceof Node);
            copy[i]=new Node(nodes[i]);
            Assertions.assertEquals(true,copy[i] instanceof Node);
        }
    }
    @Test
    void Getters_test(){
        Node[] nodes= new Node[3];
        nodes[0]=new Node(0,new Point3D(1,2,3),0,"",1);
        nodes[1]=new Node(1,new Point3D(4,5,6),0,"",2);
        nodes[2]=new Node(2,new Point3D(7,8,9),0,"", 3);
        Assertions.assertEquals(0,nodes[0].getKey());
        Assertions.assertEquals(new Point3D(1,2,3),nodes[0].getLocation());
        Assertions.assertEquals(1,nodes[0].getTag());
        Assertions.assertEquals(1,nodes[1].getKey());
        Assertions.assertEquals(new Point3D(4,5,6),nodes[1].getLocation());
        Assertions.assertEquals(2,nodes[1].getTag());
        Assertions.assertEquals(2,nodes[2].getKey());
        Assertions.assertEquals(new Point3D(7,8,9),nodes[2].getLocation());
        Assertions.assertEquals(3,nodes[2].getTag());
    }
    @Test
    void Setters_Test(){
        Node n=new Node(0,new Point3D(1,2,3),0,"",1);
        n.setLocation(new Point3D(1,2,2));
        Assertions.assertEquals(new Point3D(1,2,2),n.getLocation());
        n.setTag(2);
        Assertions.assertEquals(2,n.getTag());
        n.setInfo("Happy Test");
        Assertions.assertEquals("Happy Test", n.getInfo());
        n.setWeight(5.3);
        Assertions.assertEquals(5.3,n.getWeight());
    }
    @Test
    void ToString_test(){
        Node[] nodes=this.Generate_Nodes();
        for (int i = 0; i < nodes.length; i++) {
            String expected = "("+nodes[i].getKey()+")";
            Assertions.assertEquals(expected,nodes[i].toString());
        }
    }
    @Test
    void Equals_test(){
        Node[] nodes=this.Generate_Nodes();
        for (int i = 0; i < nodes.length; i++) {
            for (int j = i; j < nodes.length; j++) {
                if(i==j){
                    Assertions.assertEquals(true,nodes[i].equals(nodes[j]));
                    continue;
                }
                Assertions.assertEquals(false,nodes[i].equals(nodes[j]));
            }
        }
    }
    @Test
    void HashCode_Test(){
        Node[] nodes=this.Generate_Nodes();
        for (int i = 0; i < nodes.length; i++) {
            int hashed= Objects.hash(nodes[i].getKey(), nodes[i].getLocation(), nodes[i].getWeight(), nodes[i].getInfo(), nodes[i].getTag());
            Assertions.assertEquals(hashed, nodes[i].hashCode());
        }
    }
}
