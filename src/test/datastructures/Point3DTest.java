package datastructures;

import datastructures.Point3D;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point3DTest {
    
    private final boolean customSeed = true;
    private final long seed = 0;
    Random rand = new Random(customSeed ? seed : System.currentTimeMillis());
    private final int sampleSize = 10;
    
    Point3D p1, p2, p3;
    
    @BeforeEach
    void setUp() {
        p1 = new Point3D(1, 2, 3);
        p2 = new Point3D(4, 5, 6);
        p3 = new Point3D(7, 8, 9);
    }
    
    @Test
    void testConstructor() {
        assertEquals(1, p1.getX());
        assertEquals(2, p1.getY());
        assertEquals(3, p1.getZ());
        p1 = new Point3D(1, 2);
        assertEquals(1, p1.getX());
        assertEquals(2, p1.getY());
        assertEquals(0, p1.getZ());
        p1 = new Point3D(1);
        assertEquals(1, p1.getX());
        assertEquals(0, p1.getY());
        assertEquals(0, p1.getZ());
        p1 = new Point3D();
        assertEquals(0, p1.getX());
        assertEquals(0, p1.getY());
        assertEquals(0, p1.getZ());
        p1 = new Point3D(p2);
        assertEquals(4, p1.getX());
        assertEquals(5, p1.getY());
        assertEquals(6, p1.getZ());
        
        double x, y, z;
        Point3D p;
        for (int i = 0; i < this.sampleSize; i++) {
            x = rand.nextDouble();
            y = rand.nextDouble();
            z = rand.nextDouble();
            p = new Point3D(x, y, z);
            assertEquals(0, Double.compare(x, p.getX()));
            assertEquals(0, Double.compare(y, p.getY()));
            assertEquals(0, Double.compare(z, p.getZ()));
            p = new Point3D(x, y);
            assertEquals(0, Double.compare(x, p.getX()));
            assertEquals(0, Double.compare(y, p.getY()));
            assertEquals(0, Double.compare(0.0, p.getZ()));
            p = new Point3D(x);
            assertEquals(0, Double.compare(x, p.getX()));
            assertEquals(0, Double.compare(0.0, p.getY()));
            assertEquals(0, Double.compare(0.0, p.getZ()));
            Point3D p1 = new Point3D(x, y, z);
            p = new Point3D(p1);
            assertEquals(0, Double.compare(p1.getX(), p.getX()));
            assertEquals(0, Double.compare(p1.getY(), p.getY()));
            assertEquals(0, Double.compare(p1.getZ(), p.getZ()));
            assertNotSame(p, p1);
        }
        p = new Point3D();
        assertEquals(0, Double.compare(0.0, p.getX()));
        assertEquals(0, Double.compare(0.0, p.getY()));
        assertEquals(0, Double.compare(0.0, p.getZ()));
    }
    
    @Test
    void getX() {
        assertEquals(1, p1.getX());
        assertEquals(4, p2.getX());
        assertEquals(7, p3.getX());
        double x;
        Point3D p;
        for (int i = 0; i < this.sampleSize; i++) {
            x = rand.nextDouble();
            p = new Point3D(x, 0, 0);
            assertEquals(0, Double.compare(x, p.getX()));
        }
    }
    
    @Test
    void getY() {
        assertEquals(2, p1.getY());
        assertEquals(5, p2.getY());
        assertEquals(8, p3.getY());
        double y;
        Point3D p;
        for (int i = 0; i < this.sampleSize; i++) {
            y = rand.nextDouble();
            p = new Point3D(0, y, 0);
            assertEquals(0, Double.compare(y, p.getY()));
        }
    }
    
    @Test
    void getZ() {
        assertEquals(3, p1.getZ());
        assertEquals(6, p2.getZ());
        assertEquals(9, p3.getZ());
        int z;
        Point3D p;
        for (int i = 0; i < this.sampleSize; i++) {
            z = rand.nextInt();
            p = new Point3D(0, 0, z);
            assertEquals(0, Double.compare(z, p.getZ()));
        }
    }
    
    @Test
    void setX() {
        p1.setX(10);
        assertEquals(10, p1.getX());
        p2.setX(11);
        assertEquals(11, p2.getX());
        p3.setX(12);
        assertEquals(12, p3.getX());
        double x;
        Point3D p;
        for (int i = 0; i < this.sampleSize; i++) {
            x = rand.nextDouble();
            p = new Point3D(x, 0, 0);
            p.setX(x);
            assertEquals(0, Double.compare(x, p.getX()));
        }
    }
    
    @Test
    void setY() {
        p1.setY(10);
        assertEquals(10, p1.getY());
        p2.setY(11);
        assertEquals(11, p2.getY());
        p3.setY(12);
        assertEquals(12, p3.getY());
        double y;
        Point3D p;
        for (int i = 0; i < this.sampleSize; i++) {
            y = rand.nextDouble();
            p = new Point3D(0, y, 0);
            p.setY(y);
            assertEquals(0, Double.compare(y, p.getY()));
        }
    }
    
    @Test
    void setZ() {
        p1.setZ(10);
        assertEquals(10, p1.getZ());
        p2.setZ(11);
        assertEquals(11, p2.getZ());
        p3.setZ(12);
        assertEquals(12, p3.getZ());
        double z;
        Point3D p;
        for (int i = 0; i < this.sampleSize; i++) {
            z = rand.nextDouble();
            p = new Point3D(0, 0, z);
            p.setZ(z);
            assertEquals(0, Double.compare(z, p.getZ()));
        }
    }
    
    @Test
    void x() {
        assertEquals(1, p1.x());
        assertEquals(4, p2.x());
        assertEquals(7, p3.x());
        double x;
        Point3D p;
        for (int i = 0; i < this.sampleSize; i++) {
            x = rand.nextDouble();
            p = new Point3D(x, 0, 0);
            assertEquals(0, Double.compare(x, p.x()));
        }
    }
    
    @Test
    void y() {
        assertEquals(2, p1.y());
        assertEquals(5, p2.y());
        assertEquals(8, p3.y());
        double y;
        Point3D p;
        for (int i = 0; i < this.sampleSize; i++) {
            y = rand.nextDouble();
            p = new Point3D(0, y, 0);
            assertEquals(0, Double.compare(y, p.y()));
        }
    }
    
    @Test
    void z() {
        assertEquals(3, p1.z());
        assertEquals(6, p2.z());
        assertEquals(9, p3.z());
        double z;
        Point3D p;
        for (int i = 0; i < this.sampleSize; i++) {
            z = rand.nextDouble();
            p = new Point3D(0, 0, z);
            assertEquals(0, Double.compare(z, p.z()));
        }
    }
    
    @Test
    void distance() {
        final double sq3 = Math.sqrt(3);
        assertEquals(0.0, p1.distance(p1));
        assertEquals(3 * sq3, p1.distance(p2));
        assertEquals(6 * sq3, p1.distance(p3));
        assertEquals(3 * sq3, p2.distance(p1));
        assertEquals(0.0, p2.distance(p2));
        assertEquals(3 * sq3, p2.distance(p3));
        assertEquals(6 * sq3, p3.distance(p1));
        assertEquals(3 * sq3, p3.distance(p2));
        assertEquals(0.0, p3.distance(p3));
        for (int i = 0; i < this.sampleSize; i++) {
            double x1 = rand.nextDouble(), x2 = rand.nextDouble();
            double y1 = rand.nextDouble(), y2 = rand.nextDouble();
            double z1 = rand.nextDouble(), z2 = rand.nextDouble();
            Point3D p1 = new Point3D(x1, y1, z1);
            Point3D p2 = new Point3D(x2, y2, z2);
            assertEquals(Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1)), p1.distance(p2));
        }
    }
    
    @Test
    void testToString() {
        assertEquals("1.0, 2.0, 3.0", p1.toString());
        assertEquals("4.0, 5.0, 6.0", p2.toString());
        assertEquals("7.0, 8.0, 9.0", p3.toString());
        for (int i = 0; i < this.sampleSize; i++) {
            double x = rand.nextDouble();
            double y = rand.nextDouble();
            double z = rand.nextDouble();
            Point3D p = new Point3D(x, y, z);
            assertEquals(x + ", " + y + ", " + z, p.toString());
        }
    }
    
    @Test
    void testEquals() {
        
        assertTrue(p1.equals(p1)); // reflexive
        assertTrue(p1.equals(p2) == p2.equals(p1)); // symmetric
        assertTrue(!(p1.equals(p3) && p2.equals(p3)) || p1.equals(p3)); // transitive
        final double chanceOfBeingEqual = 0.1;
        for (int i = 0; i < this.sampleSize; i++) {
            boolean equal2 = rand.nextDouble() < chanceOfBeingEqual;
            boolean equal3 = rand.nextDouble() < chanceOfBeingEqual;
            double x1 = rand.nextDouble(), x2 = equal2 ? x1 : rand.nextDouble(), x3 = equal3 ? x1 : rand.nextDouble();
            double y1 = rand.nextDouble(), y2 = equal2 ? y1 : rand.nextDouble(), y3 = equal3 ? y1 : rand.nextDouble();
            double z1 = rand.nextDouble(), z2 = equal2 ? z1 : rand.nextDouble(), z3 = equal3 ? z1 : rand.nextDouble();
            Point3D p1 = new Point3D(x1, y1, z1);
            Point3D p2 = new Point3D(x2, y2, z2);
            Point3D p3 = new Point3D(x3, y3, z3);
            assertEquals(Double.compare(x1, x2) == 0 && Double.compare(y1, y2) == 0 && Double.compare(z1, z2) == 0, p1.equals(p2));
            assertEquals(Double.compare(x1, x3) == 0 && Double.compare(y1, y3) == 0 && Double.compare(z1, z3) == 0, p1.equals(p3));
            assertEquals(Double.compare(x2, x3) == 0 && Double.compare(y2, y3) == 0 && Double.compare(z2, z3) == 0, p2.equals(p3));
            assertTrue(p1.equals(p1)); // reflexive
            assertTrue(p2.equals(p2)); // reflexive
            assertTrue(p3.equals(p3)); // reflexive
            assertTrue(p1.equals(p2) == p2.equals(p1)); // symmetric
            assertTrue(!(p1.equals(p3) && p2.equals(p3)) || p1.equals(p3)); // transitive
        }
    }
    
    @Test
    void testHashCode() {
        assertEquals(p1.hashCode(), p1.hashCode());
        assertEquals(p2.hashCode(), p2.hashCode());
        assertEquals(p3.hashCode(), p3.hashCode());
        assertNotEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
        assertNotEquals(p2.hashCode(), p3.hashCode());
        for (int i = 0; i < this.sampleSize; i++) {
            double x1 = rand.nextDouble();
            double y1 = rand.nextDouble();
            double z1 = rand.nextDouble();
            Point3D p1 = new Point3D(x1, y1, z1);
            Point3D p2 = new Point3D(x1, y1, z1);
            Point3D p3 = new Point3D(x1, y1, z1);
            assertEquals(p1.hashCode(), p2.hashCode());
            assertEquals(p1.hashCode(), p3.hashCode());
            assertEquals(p2.hashCode(), p3.hashCode());
        }
    }
}