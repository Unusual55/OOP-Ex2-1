package datastructures;

import datastructures.Node;
import datastructures.Point3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    
    private final boolean customSeed = true;
    private final long seed = 0;
    Random rand = new Random(customSeed ? seed : System.currentTimeMillis());
    private final int sampleSize = 10;
    
    Node n1, n2, n3;
    
    @BeforeEach
    void setUp() {
        n1 = new Node(12, 3.14159265359, new Point3D(1, 2, 3), "n1,RED", -32);
        n2 = new Node(-23, 2.71828182846, new Point3D(-4.32, -5.43, -6.54), "n2,GREEN", 0);
        n3 = new Node(432, -1.61803398875, new Point3D(7.89, -8.90, 9.91), "n3,BLUE", 4);
    }
    
    @Test
    void testConstructor() {
        assertEquals(12, n1.getKey());
        assertEquals(0, Double.compare(3.14159265359, n1.getWeight()));
        assertEquals(new Point3D(1, 2, 3), n1.getLocation());
        assertEquals("n1,RED", n1.getInfo());
        assertEquals(-32, n1.getTag());
        n1 = new Node(342, 0.0, new Point3D(69, 420, 360), "n1,YELLOW");
        assertEquals(342, n1.getKey());
        assertEquals(0, Double.compare(0.0, n1.getWeight()));
        assertEquals(new Point3D(69, 420, 360), n1.getLocation());
        assertEquals("n1,YELLOW", n1.getInfo());
        assertEquals(0, n1.getTag());
        n1 = new Node(44, 993.3, new Point3D(69, 420, 360));
        assertEquals(44, n1.getKey());
        assertEquals(0, Double.compare(993.3, n1.getWeight()));
        assertEquals(new Point3D(69, 420, 360), n1.getLocation());
        assertEquals("", n1.getInfo());
        assertEquals(0, n1.getTag());
        n1 = new Node(0, 43);
        assertEquals(0, n1.getKey());
        assertEquals(0, Double.compare(43.0, n1.getWeight()));
        assertEquals(new Point3D(0, 0, 0), n1.getLocation());
        assertEquals("", n1.getInfo());
        assertEquals(0, n1.getTag());
        n1 = new Node(543);
        assertEquals(543, n1.getKey());
        assertEquals(0, Double.compare(0.0, n1.getWeight()));
        assertEquals(new Point3D(0, 0, 0), n1.getLocation());
        assertEquals("", n1.getInfo());
        assertEquals(0, n1.getTag());
        
        Node n;
        for (int i = 0; i < this.sampleSize; i++) {
            int key = rand.nextInt();
            double weight = rand.nextDouble() * 2.0 - 1.0;
            Point3D location = new Point3D(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0);
            String info = "At " + i;
            int tag = rand.nextInt();
            n = new Node(key, weight, location, info, tag);
            assertEquals(key, n.getKey());
            assertEquals(0, Double.compare(weight, n.getWeight()));
            assertEquals(location, n.getLocation());
            assertEquals(info, n.getInfo());
            assertEquals(tag, n.getTag());
            n = new Node(key, weight, location, info);
            assertEquals(key, n.getKey());
            assertEquals(0, Double.compare(weight, n.getWeight()));
            assertEquals(location, n.getLocation());
            assertEquals(info, n.getInfo());
            assertEquals(0, n.getTag());
            n = new Node(key, weight, location);
            assertEquals(key, n.getKey());
            assertEquals(0, Double.compare(weight, n.getWeight()));
            assertEquals(location, n.getLocation());
            assertEquals("", n.getInfo());
            assertEquals(0, n.getTag());
            n = new Node(key, weight);
            assertEquals(key, n.getKey());
            assertEquals(0, Double.compare(weight, n.getWeight()));
            assertEquals(new Point3D(0, 0, 0), n.getLocation());
            assertEquals("", n.getInfo());
            assertEquals(0, n.getTag());
            n = new Node(key);
            assertEquals(key, n.getKey());
            assertEquals(0, Double.compare(0.0, n.getWeight()));
            assertEquals(new Point3D(0, 0, 0), n.getLocation());
            assertEquals("", n.getInfo());
            assertEquals(0, n.getTag());
            Node n1 = new Node(key, weight, location, info, tag);
            n = new Node(n1);
            assertEquals(n1.getKey(), n.getKey());
            assertEquals(0, Double.compare(n1.getWeight(), n.getWeight()));
            assertEquals(n1.getLocation(), n.getLocation());
            assertEquals(n1.getInfo(), n.getInfo());
            assertEquals(n1.getTag(), n.getTag());
            assertNotSame(n1, n);
            
        }
    }
    
    @Test
    void getKey() {
        assertEquals(12, n1.getKey());
        assertEquals(-23, n2.getKey());
        assertEquals(432, n3.getKey());
        int key;
        Node n;
        for (int i = 0; i < this.sampleSize; i++) {
            key = rand.nextInt();
            n = new Node(key);
            assertEquals(key, n.getKey());
        }
    }
    
    @Test
    void setKey() {
        n1.setKey(1);
        assertEquals(1, n1.getKey());
        n2.setKey(-2);
        assertEquals(-2, n2.getKey());
        n3.setKey(3);
        assertEquals(3, n3.getKey());
        int key;
        Node n;
        for (int i = 0; i < this.sampleSize; i++) {
            key = rand.nextInt();
            n = new Node(-1);
            n.setKey(key);
            assertEquals(key, n.getKey());
        }
    }
    
    @Test
    void getLocation() {
        assertEquals(new Point3D(1, 2, 3), n1.getLocation());
        assertEquals(new Point3D(-4.32, -5.43, -6.54), n2.getLocation());
        assertEquals(new Point3D(7.89, -8.90, 9.91), n3.getLocation());
        Point3D location;
        Node n;
        for (int i = 0; i < this.sampleSize; i++) {
            location = new Point3D(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0);
            n = new Node(-1, -1.0, location);
            assertEquals(location, n.getLocation());
        }
    }
    
    @Test
    void setLocation() {
        Point3D location = new Point3D(36, 0.3, -99);
        n1.setLocation(location);
        assertEquals(location, n1.getLocation());
        location = new Point3D(6, -666, -66);
        n2.setLocation(location);
        assertEquals(location, n2.getLocation());
        location = new Point3D(36, -32, 1);
        n3.setLocation(location);
        assertEquals(location, n3.getLocation());
        Point3D loc;
        Node n;
        for (int i = 0; i < this.sampleSize; i++) {
            loc = new Point3D(rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0, rand.nextDouble() * 2.0 - 1.0);
            n = new Node(-1, -1.0, new Point3D(0, 0, 0));
            n.setLocation(loc);
            assertEquals(loc, n.getLocation());
        }
    }
    
    @Test
    void getWeight() {
        assertEquals(0, Double.compare(3.14159265359, n1.getWeight()));
        assertEquals(0, Double.compare(2.71828182846, n2.getWeight()));
        assertEquals(0, Double.compare(-1.61803398875, n3.getWeight()));
        double weight;
        Node n;
        for (int i = 0; i < this.sampleSize; i++) {
            weight = rand.nextDouble();
            n = new Node(-1, weight);
            assertEquals(0, Double.compare(weight, n.getWeight()));
        }
    }
    
    @Test
    void setWeight() {
        n1.setWeight(1.0);
        assertEquals(0, Double.compare(1.0, n1.getWeight()));
        n2.setWeight(2.0);
        assertEquals(0, Double.compare(2.0, n2.getWeight()));
        n3.setWeight(3.0);
        assertEquals(0, Double.compare(3.0, n3.getWeight()));
        double weight;
        Node n;
        for (int i = 0; i < this.sampleSize; i++) {
            weight = rand.nextDouble();
            n = new Node(-1, -1.0);
            n.setWeight(weight);
            assertEquals(0, Double.compare(weight, n.getWeight()));
        }
    }
    
    @Test
    void getInfo() {
        assertEquals("n1,RED", n1.getInfo());
        assertEquals("n2,GREEN", n2.getInfo());
        assertEquals("n3,BLUE", n3.getInfo());
        String info;
        Node n;
        for (int i = 0; i < this.sampleSize; i++) {
            info = "Node " + i;
            n = new Node(i, -1.0, new Point3D(0, 0, 0), info);
            assertEquals(info, n.getInfo());
        }
    }
    
    @Test
    void setInfo() {
        String info = "This is a test";
        n1.setInfo(info);
        assertEquals(info, n1.getInfo());
        info = "This is another test";
        n2.setInfo(info);
        assertEquals(info, n2.getInfo());
        info = "This is yet another test";
        n3.setInfo(info);
        assertEquals(info, n3.getInfo());
        String inf;
        Node n;
        for (int i = 0; i < this.sampleSize; i++) {
            inf = "Node " + i;
            n = new Node(-1, -1.0, new Point3D(0, 0, 0), "");
            n.setInfo(inf);
            assertEquals(inf, n.getInfo());
        }
    }
    
    @Test
    void getTag() {
        assertEquals(-32, n1.getTag());
        assertEquals(0, n2.getTag());
        assertEquals(4, n3.getTag());
        int tag;
        Node n;
        for (int i = 0; i < this.sampleSize; i++) {
            tag = rand.nextInt();
            n = new Node(-1, -1.0, new Point3D(0, 0, 0), "", tag);
            assertEquals(tag, n.getTag());
        }
    }
    
    @Test
    void setTag() {
        n1.setTag(1);
        assertEquals(1, n1.getTag());
        n2.setTag(2);
        assertEquals(2, n2.getTag());
        n3.setTag(3);
        assertEquals(3, n3.getTag());
        int tag;
        Node n;
        for (int i = 0; i < this.sampleSize; i++) {
            tag = rand.nextInt();
            n = new Node(-1, -1.0, new Point3D(0, 0, 0), "", -1);
            n.setTag(tag);
            assertEquals(tag, n.getTag());
        }
    }
    
    @Test
    void testToString() {
        assertEquals("(12)", n1.toString());
        assertEquals("(-23)", n2.toString());
        assertEquals("(432)", n3.toString());
        int key;
        Node n;
        for (int i = 0; i < this.sampleSize; i++) {
            key = rand.nextInt();
            n = new Node(key);
            assertEquals("(" + key + ")", n.toString());
        }
    }
    
    @Test
    void testEquals() {
        assertTrue(n1.equals(n1)); // reflexive
        assertTrue(n1.equals(n2) == n2.equals(n1)); // symmetric
        assertTrue(!(n1.equals(n3) && n2.equals(n3)) || n1.equals(n3)); // transitive
        final double chanceOfBeingEqual = 0.1;
        for (int i = 0; i < this.sampleSize; i++) {
            boolean equal2 = rand.nextDouble() < chanceOfBeingEqual;
            boolean equal3 = rand.nextDouble() < chanceOfBeingEqual;
            int key1 = rand.nextInt(), key2 = equal2 ? key1 : rand.nextInt(), key3 = equal3 ? key1 : rand.nextInt();
            double weight1 = rand.nextDouble(), weight2 = equal2 ? weight1 : rand.nextDouble(), weight3 = equal3 ? weight1 : rand.nextDouble();
            double x1 = rand.nextDouble(), x2 = equal2 ? x1 : rand.nextDouble(), x3 = equal3 ? x1 : rand.nextDouble();
            double y1 = rand.nextDouble(), y2 = equal2 ? y1 : rand.nextDouble(), y3 = equal3 ? y1 : rand.nextDouble();
            double z1 = rand.nextDouble(), z2 = equal2 ? z1 : rand.nextDouble(), z3 = equal3 ? z1 : rand.nextDouble();
            String info1 = "" + 3 * i, info2 = equal2 ? info1 : "" + 3 * i + 1, info3 = equal3 ? info1 : "" + 3 * i + 2;
            int tag1 = rand.nextInt(), tag2 = equal2 ? tag1 : rand.nextInt(), tag3 = equal3 ? tag1 : rand.nextInt();
            Point3D p1 = new Point3D(x1, y1, z1);
            Point3D p2 = new Point3D(x2, y2, z2);
            Point3D p3 = new Point3D(x3, y3, z3);
            Node n1 = new Node(key1, weight1, p1, info1, tag1);
            Node n2 = new Node(key2, weight2, p2, info2, tag2);
            Node n3 = new Node(key3, weight3, p3, info3, tag3);
            assertEquals(key1 == key2 && Double.compare(weight1, weight2) == 0 && Double.compare(x1, x2) == 0 && Double.compare(y1, y2) == 0 && Double.compare(z1, z2) == 0 && info1.equals(info2) && tag1 == tag2, n1.equals(n2));
            assertEquals(key1 == key3 && Double.compare(weight1, weight3) == 0 && Double.compare(x1, x3) == 0 && Double.compare(y1, y3) == 0 && Double.compare(z1, z3) == 0 && info1.equals(info3) && tag1 == tag3, n1.equals(n3));
            assertEquals(key2 == key3 && Double.compare(weight2, weight3) == 0 && Double.compare(x2, x3) == 0 && Double.compare(y2, y3) == 0 && Double.compare(z2, z3) == 0 && info2.equals(info3) && tag2 == tag3, n2.equals(n3));
            assertTrue(n1.equals(n1)); // reflexive
            assertTrue(n2.equals(n2)); // reflexive
            assertTrue(n3.equals(n3)); // reflexive
            assertTrue(n1.equals(n2) == n2.equals(n1)); // symmetric
            assertTrue(!(n1.equals(n3) && n2.equals(n3)) || n1.equals(n3)); // transitive
        }
    }
    
    @Test
    void testHashCode() {
        assertEquals(n1.hashCode(), n1.hashCode());
        assertEquals(n2.hashCode(), n2.hashCode());
        assertEquals(n3.hashCode(), n3.hashCode());
        assertNotEquals(n1.hashCode(), n2.hashCode());
        assertNotEquals(n1.hashCode(), n3.hashCode());
        assertNotEquals(n2.hashCode(), n3.hashCode());
        for (int i = 0; i < this.sampleSize; i++) {
            int key = rand.nextInt();
            double weight = rand.nextDouble();
            Point3D p = new Point3D(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
            String info = "" + 3 * i;
            int tag = rand.nextInt();
            Node n1 = new Node(key, weight, p, info, tag);
            Node n2 = new Node(key, weight, p, info, tag);
            Node n3 = new Node(key, weight, p, info, tag);
            assertEquals(n1.hashCode(), n2.hashCode());
            assertEquals(n1.hashCode(), n3.hashCode());
            assertEquals(n2.hashCode(), n3.hashCode());
        }
    }
}