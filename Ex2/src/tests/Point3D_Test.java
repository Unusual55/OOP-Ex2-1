package tests;
import DataStructures.Point3D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Random;

public class Point3D_Test {
    Point3D[] Generate_Points(){
        Point3D[] points;
        Random rnd= new Random();
        int minC =10, maxC=100;
        double minV=0, maxV=100.0;
        int len= rnd.nextInt(maxC-minC)+minC;
        points=new Point3D[len];
        for (int i = 0; i < len; i++) {
            double x=rnd.nextDouble()*len;
            double y=rnd.nextDouble()*len;
            double z=rnd.nextDouble()*len;
            points[i]=new Point3D(x,y,z);
        }
        return points;
    }
    @Test
    void Constructors_test(){
        Point3D[] pointarray= new Point3D[4];
        pointarray[0]= new Point3D(1,2,3);
        pointarray[1]= new Point3D(4,5);
        pointarray[2]=new Point3D(18);
        pointarray[3]=new Point3D();
        for (int i = 0; i <4 ; i++) {
            Assertions.assertEquals(true, pointarray[i] instanceof Point3D);
        }
        Point3D pcopy=new Point3D(pointarray[0]);
        Assertions.assertEquals(true,pcopy instanceof Point3D);
    }
     @Test
    void Getters_test(){
         Point3D[] pointarray= new Point3D[4];
         pointarray[0]= new Point3D(1,2,3);
         pointarray[1]= new Point3D(4,5);
         pointarray[2]=new Point3D(18);
         pointarray[3]=new Point3D();
         Assertions.assertEquals(1,pointarray[0].x());
         Assertions.assertEquals(4,pointarray[1].x());
         Assertions.assertEquals(18,pointarray[2].x());
         Assertions.assertEquals(0,pointarray[3].x());
         Assertions.assertEquals(2,pointarray[0].y());
         Assertions.assertEquals(5,pointarray[1].y());
         Assertions.assertEquals(0,pointarray[2].y());
         Assertions.assertEquals(0,pointarray[3].y());
         Assertions.assertEquals(3,pointarray[0].z());
         Assertions.assertEquals(0,pointarray[1].z());
         Assertions.assertEquals(0,pointarray[2].z());
         Assertions.assertEquals(0,pointarray[3].z());
     }

     @Test
    void Setters_test(){
         Point3D p1 = new Point3D(1,2,3);
         p1.setX(16);
         Assertions.assertEquals(16,p1.x());
         p1.setY(7);
         Assertions.assertEquals(7,p1.y());
         p1.setZ(19);
         Assertions.assertEquals(19,p1.z());
    }
    @Test
    void Equals_test(){
        Point3D[] pointarray= new Point3D[5];
        pointarray[0]= new Point3D(1,2,3);
        pointarray[1]= new Point3D(4,5);
        pointarray[2]=new Point3D(18);
        pointarray[3]=new Point3D();
        pointarray[4]=new Point3D(pointarray[0]);
        for (int i = 0; i < 4; i++) {
            for (int j = i+1; j < 5; j++) {
                if(i==0&&j==4){
                    Assertions.assertEquals(true, pointarray[i].equals(pointarray[j]));
                    continue;
                }
                Assertions.assertEquals(false, pointarray[i].equals(pointarray[j]));
            }
        }
    }
    @Test
    void ToString_test(){
        Point3D[] pointarray= new Point3D[5];
        pointarray[0]= new Point3D(1,2,3);
        pointarray[1]= new Point3D(4,5);
        pointarray[2]=new Point3D(18);
        pointarray[3]=new Point3D();
        pointarray[4]=new Point3D(pointarray[0]);
        for (int i = 0; i < 5; i++) {
            String str= ""+ pointarray[i].x()+", "+pointarray[i].y()+", "+pointarray[i].z();
            Assertions.assertEquals(str,pointarray[i].toString());
        }
    }
    @Test
    void Distance_test(){
        Point3D[] points=this.Generate_Points();
        for (int i = 0; i <points.length ; i++) {
            for (int j = 0; j < points.length; j++) {
                Point3D p1=points[i];
                Point3D p2=points[j];
                double dx=Math.pow(p1.x()-p2.x(),2);
                double dy=Math.pow(p1.y()-p2.y(),2);
                double dz=Math.pow(p1.z()-p2.z(),2);
                double dist=+Math.sqrt(dx+dy+dz);
                Assertions.assertEquals(dist,p1.distance(p2));
            }
        }
    }
    @Test
    void Hashcode_test(){
        Point3D[] points=this.Generate_Points();
        for (int i = 0; i < points.length; i++) {
            double x=points[i].x();
            double y=points[i].y();
            double z=points[i].z();
            int hashed= Objects.hash(x,y,z);
            Assertions.assertEquals(hashed,points[i].hashCode());
        }
    }
}
