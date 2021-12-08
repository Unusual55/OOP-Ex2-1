package datastructures;

import datastructures.Edge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {
    
    private final boolean customSeed = true;
    private final long seed = 0;
    Random rand = new Random(customSeed ? seed : System.currentTimeMillis());
    private final int sampleSize = 10;
    
    Edge e1, e2, e3;
    
    @BeforeEach
    void setUp() {
        e1 = new Edge(1, 2, 0.3183098861837907, "RED", 1);
        e2 = new Edge(1, 3, 0.36787944117144233, "GREEN", 0);
        e3 = new Edge(2, 4, 0.6180339887498948, "BLUE", -1);
    }
    
    @Test
    void testConstructor() {
        assertEquals(1, e1.getSrc());
        assertEquals(2, e1.getDest());
        assertEquals(0, Double.compare(0.3183098861837907, e1.getWeight()));
        assertEquals("RED", e1.getInfo());
        assertEquals(1, e1.getTag());
        e1 = new Edge(43, 5, 0.36787944117144233);
        assertEquals(43, e1.getSrc());
        assertEquals(5, e1.getDest());
        assertEquals(0, Double.compare(0.36787944117144233, e1.getWeight()));
        assertEquals("", e1.getInfo());
        assertEquals(0, e1.getTag());
        e1 = new Edge(45, 432);
        assertEquals(45, e1.getSrc());
        assertEquals(432, e1.getDest());
        assertEquals(0, Double.compare(1.0, e1.getWeight()));
        assertEquals("", e1.getInfo());
        assertEquals(0, e1.getTag());
        
        IllegalArgumentException exception;
        exception = assertThrows(IllegalArgumentException.class, () -> new Edge(0, 1, 0.0, "RED", 1));
        assertTrue(exception.getMessage().contains("Weight must be positive"));
        exception = assertThrows(IllegalArgumentException.class, () -> new Edge(0, 1, -1.0, "RED", 1));
        assertTrue(exception.getMessage().contains("Weight must be positive"));
        
        Edge e;
        for (int i = 0; i < this.sampleSize; i++) {
            int src = rand.nextInt(this.sampleSize);
            int dest = rand.nextInt(this.sampleSize);
            double weight = rand.nextDouble();
            String info = "";
            int tag = rand.nextInt(this.sampleSize);
            e = new Edge(src, dest, weight, info, tag);
            assertEquals(src, e.getSrc());
            assertEquals(dest, e.getDest());
            assertEquals(0, Double.compare(weight, e.getWeight()));
            assertEquals(info, e.getInfo());
            assertEquals(tag, e.getTag());
            e = new Edge(src, dest, weight);
            assertEquals(src, e.getSrc());
            assertEquals(dest, e.getDest());
            assertEquals(0, Double.compare(weight, e.getWeight()));
            assertEquals("", e.getInfo());
            assertEquals(0, e.getTag());
            e = new Edge(src, dest);
            assertEquals(src, e.getSrc());
            assertEquals(dest, e.getDest());
            assertEquals(0, Double.compare(1.0, e.getWeight()));
            assertEquals("", e.getInfo());
            assertEquals(0, e.getTag());
            Edge e1 = new Edge(src, dest, weight, info, tag);
            e = new Edge(e1);
            assertEquals(e1.getSrc(), e.getSrc());
            assertEquals(e1.getDest(), e.getDest());
            assertEquals(0, Double.compare(e1.getWeight(), e.getWeight()));
            assertEquals(e1.getInfo(), e.getInfo());
            assertEquals(e1.getTag(), e.getTag());
            assertNotSame(e, e1);
        }
    }
    
    @Test
    void getSrc() {
        assertEquals(1, e1.getSrc());
        assertEquals(1, e2.getSrc());
        assertEquals(2, e3.getSrc());
        int src;
        Edge e;
        for (int i = 0; i < this.sampleSize; i++) {
            src = rand.nextInt(this.sampleSize);
            e = new Edge(src, -1);
            assertEquals(src, e.getSrc());
        }
    }
    
    @Test
    void setSrc() {
        e1.setSrc(2);
        assertEquals(2, e1.getSrc());
        e2.setSrc(3);
        assertEquals(3, e2.getSrc());
        e3.setSrc(4);
        assertEquals(4, e3.getSrc());
        int src;
        Edge e;
        for (int i = 0; i < this.sampleSize; i++) {
            src = rand.nextInt(this.sampleSize);
            e = new Edge(-1, -1);
            e.setSrc(src);
            assertEquals(src, e.getSrc());
        }
    }
    
    @Test
    void getDest() {
        assertEquals(2, e1.getDest());
        assertEquals(3, e2.getDest());
        assertEquals(4, e3.getDest());
        int dest;
        Edge e;
        for (int i = 0; i < this.sampleSize; i++) {
            dest = rand.nextInt(this.sampleSize);
            e = new Edge(-1, dest);
            assertEquals(dest, e.getDest());
        }
    }
    
    @Test
    void setDest() {
        e1.setDest(2);
        assertEquals(2, e1.getDest());
        e2.setDest(3);
        assertEquals(3, e2.getDest());
        e3.setDest(4);
        assertEquals(4, e3.getDest());
        int dest;
        Edge e;
        for (int i = 0; i < this.sampleSize; i++) {
            dest = rand.nextInt(this.sampleSize);
            e = new Edge(-1, -1);
            e.setDest(dest);
            assertEquals(dest, e.getDest());
        }
    }
    
    @Test
    void getWeight() {
        assertEquals(0, Double.compare(0.3183098861837907, e1.getWeight()));
        assertEquals(0, Double.compare(0.36787944117144233, e2.getWeight()));
        assertEquals(0, Double.compare(0.6180339887498948, e3.getWeight()));
        int src, dest;
        double weight;
        Edge e;
        for (int i = 0; i < this.sampleSize; i++) {
            src = rand.nextInt(this.sampleSize);
            dest = rand.nextInt(this.sampleSize);
            weight = rand.nextDouble() * this.sampleSize;
            e = new Edge(src, dest, weight);
            assertEquals(0, Double.compare(weight, e.getWeight()));
        }
    }
    
    @Test
    void setWeight() {
        e1.setWeight(0.5);
        assertEquals(0, Double.compare(0.5, e1.getWeight()));
        e2.setWeight(0.6);
        assertEquals(0, Double.compare(0.6, e2.getWeight()));
        e3.setWeight(0.7);
        assertEquals(0, Double.compare(0.7, e3.getWeight()));
    
        IllegalArgumentException exception;
        exception = assertThrows(IllegalArgumentException.class, () -> e1.setWeight(-1));
        assertTrue(exception.getMessage().contains("Weight must be positive"));
        exception = assertThrows(IllegalArgumentException.class, () -> e2.setWeight(0.0));
        assertTrue(exception.getMessage().contains("Weight must be positive"));
    
    
        int src, dest;
        double weight;
        Edge e;
        for (int i = 0; i < this.sampleSize; i++) {
            src = rand.nextInt(this.sampleSize);
            dest = rand.nextInt(this.sampleSize);
            weight = rand.nextDouble() * this.sampleSize;
            e = new Edge(src, dest, 420.69);
            e.setWeight(weight);
            assertEquals(0, Double.compare(weight, e.getWeight()));
        }
    }
    
    @Test
    void getInfo() {
        assertEquals("RED", e1.getInfo());
        assertEquals("GREEN", e2.getInfo());
        assertEquals("BLUE", e3.getInfo());
        String info;
        Edge e;
        for (int i = 0; i < this.sampleSize; i++) {
            info = "At " + i;
            e = new Edge(-1, -1, 420.69, info, -1);
            assertEquals(info, e.getInfo());
        }
    }
    
    @Test
    void setInfo() {
        e1.setInfo("12");
        assertEquals("12", e1.getInfo());
        e2.setInfo("32");
        assertEquals("32", e2.getInfo());
        e3.setInfo("xD");
        assertEquals("xD", e3.getInfo());
        String info;
        Edge e;
        for (int i = 0; i < this.sampleSize; i++) {
            info = "At " + i;
            e = new Edge(-1, -1, 420.69, "Bla Bla", i);
            e.setInfo(info);
            assertEquals(info, e.getInfo());
        }
    }
    
    @Test
    void getTag() {
        assertEquals(1, e1.getTag());
        assertEquals(0, e2.getTag());
        assertEquals(-1, e3.getTag());
        int tag;
        Edge e;
        for (int i = 0; i < this.sampleSize; i++) {
            tag = rand.nextInt(this.sampleSize);
            e = new Edge(-1, -1, 420.69, "", tag);
            assertEquals(tag, e.getTag());
        }
    }
    
    @Test
    void setTag() {
        e1.setTag(2);
        assertEquals(2, e1.getTag());
        e2.setTag(3);
        assertEquals(3, e2.getTag());
        e3.setTag(4);
        assertEquals(4, e3.getTag());
        int tag;
        Edge e;
        for (int i = 0; i < this.sampleSize; i++) {
            tag = rand.nextInt(this.sampleSize);
            e = new Edge(-1, -1, 420.69, "", -1);
            e.setTag(tag);
            assertEquals(tag, e.getTag());
        }
    }
    
    @Test
    void testToString() {
        assertEquals("1--0.3183098861837907->2", e1.toString());
        assertEquals("1--0.36787944117144233->3", e2.toString());
        assertEquals("2--0.6180339887498948->4", e3.toString());
        int src, dest;
        double weight;
        Edge e;
        for (int i = 0; i < this.sampleSize; i++) {
            src = rand.nextInt(this.sampleSize);
            dest = rand.nextInt(this.sampleSize);
            weight = rand.nextDouble() * this.sampleSize;
            e = new Edge(src, dest, weight);
            assertEquals(src + "--" + weight + "->" + dest, e.toString());
        }
    }
    
    @Test
    void testEquals() {
        assertTrue(e1.equals(e1)); // reflexive
        assertTrue(e1.equals(e2) == e2.equals(e1)); // symmetric
        assertTrue(!(e1.equals(e3) && e2.equals(e3)) || e1.equals(e3)); // transitive
        final double chanceOfBeingEqual = 0.1;
        for (int i = 0; i < this.sampleSize; i++) {
            boolean equal2 = rand.nextDouble() < chanceOfBeingEqual;
            boolean equal3 = rand.nextDouble() < chanceOfBeingEqual;
            int src1 = rand.nextInt(), src2 = equal2 ? src1 : rand.nextInt(), src3 = equal3 ? src1 : rand.nextInt();
            int dest1 = rand.nextInt(), dest2 = equal2 ? dest1 : rand.nextInt(), dest3 = equal3 ? dest1 : rand.nextInt();
            double weight1 = rand.nextDouble(), weight2 = equal2 ? weight1 : rand.nextDouble(), weight3 = equal3 ? weight1 : rand.nextDouble();
            String info1 = "" + 3 * i, info2 = equal2 ? info1 : "" + 3 * i + 1, info3 = equal3 ? info1 : "" + 3 * i + 2;
            int tag1 = rand.nextInt(), tag2 = equal2 ? tag1 : rand.nextInt(), tag3 = equal3 ? tag1 : rand.nextInt();
            Edge e1 = new Edge(src1, dest1, weight1, info1, tag1);
            Edge e2 = new Edge(src2, dest2, weight2, info2, tag2);
            Edge e3 = new Edge(src3, dest3, weight3, info3, tag3);
            assertEquals(src1 == src2 && dest1 == dest2 && Double.compare(weight1, weight2) == 0 && info1.equals(info2) && tag1 == tag2, e1.equals(e2));
            assertEquals(src1 == src3 && dest1 == dest3 && Double.compare(weight1, weight3) == 0 && info1.equals(info3) && tag1 == tag3, e1.equals(e3));
            assertEquals(src2 == src3 && dest2 == dest3 && Double.compare(weight2, weight3) == 0 && info2.equals(info3) && tag2 == tag3, e2.equals(e3));
            assertTrue(e1.equals(e1)); // reflexive
            assertTrue(e2.equals(e2)); // reflexive
            assertTrue(e3.equals(e3)); // reflexive
            assertTrue(e1.equals(e2) == e2.equals(e1)); // symmetric
            assertTrue(!(e1.equals(e3) && e2.equals(e3)) || e1.equals(e3)); // transitive
        }
    }
    
    @Test
    void testHashCode() {
        assertEquals(e1.hashCode(), e1.hashCode());
        assertEquals(e2.hashCode(), e2.hashCode());
        assertEquals(e3.hashCode(), e3.hashCode());
        assertNotEquals(e1.hashCode(), e2.hashCode());
        assertNotEquals(e1.hashCode(), e3.hashCode());
        assertNotEquals(e2.hashCode(), e3.hashCode());
        for (int i = 0; i < this.sampleSize; i++) {
            int src = rand.nextInt();
            int dest = rand.nextInt();
            double weight = rand.nextDouble();
            String info = "" + 3 * i;
            int tag = rand.nextInt();
            Edge e1 = new Edge(src, dest, weight, info, tag);
            Edge e2 = new Edge(src, dest, weight, info, tag);
            Edge e3 = new Edge(src, dest, weight, info, tag);
            assertEquals(e1.hashCode(), e2.hashCode());
            assertEquals(e1.hashCode(), e3.hashCode());
            assertEquals(e2.hashCode(), e3.hashCode());
        }
    }
}