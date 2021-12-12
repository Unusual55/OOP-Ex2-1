package test.datastructures;
import datastructures.*;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DWGraphTest {
    
    
    private final boolean customSeed = true;
    private final long seed = 0;
    Random rand = new Random(customSeed ? seed : System.currentTimeMillis());
    private final int sampleSize = 10;
    
    private final int numberOfNodes = 10;
    private final int numberOfOutEdges = 2;
    private final int numberOfInEdges = 2;
    private final int outVariant = 0;
    private final int inVariant = 0;
    
    DWGraph g1, g2, g3;
    
    @BeforeEach
    void setUp() {
        g1 = new DWGraph();
        g2 = new DWGraph();
        g3 = new DWGraph();
        
        for (int i = 0; i < this.numberOfNodes; i++) {
            final double weightConst = i / (double)this.numberOfNodes;
            g1.addNode(new Node(i, i + 0.3 + weightConst));
            if (i % 2 == 0) {
                g2.addNode(new Node(i, i + 0.4 + weightConst));
            }
            if (i % 3 == 0) {
                g3.addNode(new Node(i, i + 0.5 + weightConst));
            }
        }
        for (int i = 0; i < this.numberOfNodes; i++) {
            final double weightConst = i / (double)this.numberOfNodes;
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            g1.connect(i, dest, i + 0.1 + weightConst);
            if (i % 2 == 0) {
                dest = (2 * whichConnect) % this.numberOfNodes;
                dest -= dest % 2;
                g2.connect(i, dest, i + 0.2 + weightConst);
            }
            if (i % 3 == 0) {
                dest = (3 * whichConnect) % this.numberOfNodes;
                dest -= dest % 3;
                g3.connect(i, dest, i + 0.3 + weightConst);
            }
        }
    }
    
    @Test
    void testClone() {
        DWGraph g = new DWGraph(g1);
        assertEquals(g, g1);
        assertNotSame(g, g1);
    }
    
    @Test
    void getNode() {
        NodeData n;
        for (int i = 0; i < this.numberOfNodes; i++) {
            final double weightConst = i / (double)this.numberOfNodes;
            n = g1.getNode(i);
            assertEquals(i, n.getKey());
            assertEquals(i + 0.3 + weightConst, n.getWeight(), 0.001);
            if (i % 2 == 0) {
                n = g2.getNode(i);
                assertEquals(i, n.getKey());
                assertEquals(i + 0.4 + weightConst, n.getWeight(), 0.001);
            }
            if (i % 3 == 0) {
                n = g3.getNode(i);
                assertEquals(i, n.getKey());
                assertEquals(i + 0.5 + weightConst, n.getWeight(), 0.001);
            }
        }
        
        DWGraph g = this.generateGraph(this.numberOfNodes, this.numberOfOutEdges, this.numberOfInEdges, this.outVariant, this.inVariant);
        Iterator<NodeData> it = g.nodeIter();
        while (it.hasNext()) {
            n = it.next();
            assertEquals(n.getKey(), g.getNode(n.getKey()).getKey());
        }
    }
    
    @Test
    void getEdge() {
        EdgeData e;
        for (int i = 0; i < this.numberOfNodes; i++) {
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            e = g1.getEdge(i, dest);
            assertEquals(i, e.getSrc());
            assertEquals(dest, e.getDest());
            if (i % 2 == 0) {
                dest = (2 * whichConnect) % this.numberOfNodes;
                dest -= dest % 2;
                e = g2.getEdge(i, dest);
                assertEquals(i, e.getSrc());
                assertEquals(dest, e.getDest());
            }
            if (i % 3 == 0) {
                dest = (3 * whichConnect) % this.numberOfNodes;
                dest -= dest % 3;
                if (dest != i) {
                    e = g3.getEdge(i, dest);
                    assertEquals(i, e.getSrc());
                    assertEquals(dest, e.getDest());
                }
            }
        }
        
        DWGraph g = this.generateGraph(this.numberOfNodes, this.numberOfOutEdges, this.numberOfInEdges, this.outVariant, this.inVariant);
        Iterator<EdgeData> it = g.edgeIter();
        while (it.hasNext()) {
            e = it.next();
            assertEquals(e.getSrc(), g.getEdge(e.getSrc(), e.getDest()).getSrc());
            assertEquals(e.getDest(), g.getEdge(e.getSrc(), e.getDest()).getDest());
        }
    }
    
    @Test
    void addNode() {
        DWGraph g = new DWGraph();
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.addNode(new Node(i, i + 0.3));
            assertEquals(i, g.getNode(i).getKey());
            assertEquals(i + 0.3, g.getNode(i).getWeight(), 0.001);
        }
    }
    
    @Test
    void connect() {
        DWGraph g = new DWGraph();
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.addNode(new Node(i, i + 0.3));
        }
        for (int i = 0; i < this.numberOfNodes; i++) {
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            g.connect(i, dest, i + 0.3);
            assertEquals(i, g.getEdge(i, dest).getSrc());
            assertEquals(dest, g.getEdge(i, dest).getDest());
        }
    }
    
    @Test
    void removeNode() {
        DWGraph g = new DWGraph();
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.addNode(new Node(i, i + 0.3));
        }
        for (int i = 0; i < this.numberOfNodes; i++) {
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            g.connect(i, dest, i + 0.3);
        }
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.removeNode(i);
            assertEquals(this.numberOfNodes - i - 1, g.nodeSize());
            assertNull(g.getNode(i));
        }
    }
    
    @Test
    void removeEdge() {
        DWGraph g = new DWGraph();
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.addNode(new Node(i, i + 0.3));
        }
        int connect = 0;
        for (int i = 0; i < this.numberOfNodes; i++) {
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            if (g.getEdge(i, dest) == null) {
                g.connect(i, dest, i + 0.3);
                connect++;
            }
        }
        for (int i = 0; i < this.numberOfNodes; i++) {
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            g.removeEdge(i, dest);
            assertEquals(connect - i - 1, g.edgeSize());
            assertNull(g.getEdge(i, dest));
        }
    }
    
    @Test
    void nodeIter() {
        DWGraph g = new DWGraph();
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.addNode(new Node(i, i + 0.3));
        }
        Iterator<NodeData> it = g.nodeIter();
        int i = 0;
        while (it.hasNext()) {
            assertEquals(i, it.next().getKey());
            i++;
        }
        
        DWGraph g2 = new DWGraph(g);
        it = g2.nodeIter();
        i = 0;
        while (it.hasNext()) {
            NodeData n = it.next();
            it.remove();
            assertEquals(this.numberOfNodes - i - 1, g2.nodeSize());
            assertNull(g2.getNode(n.getKey()));
            i++;
        }
        
        
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, () -> {
            DWGraph g3 = new DWGraph(g);
            Iterator<NodeData> it2 = g3.nodeIter();
            g3.addNode(new Node(this.numberOfNodes, this.numberOfNodes + 0.3));
            it2.hasNext();
        });
        assertTrue(exception.getMessage().contains("Graph was changed since iterator was constructed."));
        exception = assertThrows(RuntimeException.class, () -> {
            DWGraph g3 = new DWGraph(g);
            Iterator<NodeData> it2 = g3.nodeIter();
            g3.addNode(new Node(this.numberOfNodes + 1, this.numberOfNodes + 1 + 0.3));
            it2.next();
        });
        assertTrue(exception.getMessage().contains("Graph was changed since iterator was constructed."));
    }
    
    @Test
    void edgeIter() {
        DWGraph g = new DWGraph();
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.addNode(new Node(i, i + 0.3));
        }
        int connects = 0;
        for (int i = 0; i < this.numberOfNodes; i++) {
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            if (g.getEdge(i, dest) == null) {
                g.connect(i, dest, i + 0.3);
                connects++;
            }
        }
        Iterator<EdgeData> it = g.edgeIter();
        int i = 0;
        while (it.hasNext()) {
            assertEquals(i, it.next().getSrc());
            i++;
        }
        
        DWGraph g2;
        g2 = new DWGraph(g);
        it = g2.edgeIter();
        i = 0;
        while (it.hasNext()) {
            EdgeData e = it.next();
            it.remove();
            assertEquals(connects - i - 1, g2.edgeSize());
            assertNull(g2.getEdge(e.getSrc(), e.getDest()));
            i++;
        }
        
        RuntimeException exception;
        exception = assertThrows(RuntimeException.class, () -> {
            DWGraph g3 = new DWGraph(g);
            Iterator<EdgeData> it2 = g3.edgeIter();
            g3.addNode(new Node(this.numberOfNodes, this.numberOfNodes + 0.3));
            it2.hasNext();
        });
        assertTrue(exception.getMessage().contains("Graph was changed since iterator was constructed."));
        exception = assertThrows(RuntimeException.class, () -> {
            DWGraph g3 = new DWGraph(g);
            Iterator<EdgeData> it2 = g3.edgeIter();
            g3.addNode(new Node(this.numberOfNodes + 1, this.numberOfNodes + 1 + 0.3));
            it2.next();
        });
        assertTrue(exception.getMessage().contains("Graph was changed since iterator was constructed."));
    }
    
    // TODO: edgeIter(node_id)
    
    @Test
    void nodeSize() {
        DWGraph g = new DWGraph();
        assertEquals(0, g.nodeSize());
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.addNode(new Node(i, i + 0.3));
        }
        assertEquals(this.numberOfNodes, g.nodeSize());
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.removeNode(i);
        }
        assertEquals(0, g.nodeSize());
    }
    
    @Test
    void edgeSize() {
        DWGraph g = new DWGraph();
        assertEquals(0, g.edgeSize());
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.addNode(new Node(i, i + 0.3));
        }
        assertEquals(0, g.edgeSize());
        int connects = 0;
        for (int i = 0; i < this.numberOfNodes; i++) {
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            if (g.getEdge(i, dest) == null) {
                g.connect(i, dest, i + 0.3);
                connects++;
            }
        }
        assertEquals(connects, g.edgeSize());
        for (int i = 0; i < this.numberOfNodes; i++) {
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            g.removeEdge(i, dest);
        }
        assertEquals(0, g.edgeSize());
    }
    
    @Test
    void getMC() {
        DWGraph g = new DWGraph();
        assertEquals(0, g.getMC());
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.addNode(new Node(i, i + 0.3));
        }
        assertEquals(this.numberOfNodes, g.getMC());
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.removeNode(i);
        }
        assertEquals(2 * this.numberOfNodes, g.getMC());
    }
    
    @Test
    void testToString() {
        DWGraph g = new DWGraph();
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.addNode(new Node(i, i + 0.3));
        }
        int connects = 0;
        for (int i = 0; i < this.numberOfNodes; i++) {
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            if (g.getEdge(i, dest) == null) {
                g.connect(i, dest, i + 0.3);
                connects++;
            }
        }
        String s = g.toString();
        String expected = "";
        StringBuilder sb = new StringBuilder();
        sb.append("{\n\t\"Edges\": [");
        Iterator<EdgeData> it = g.edgeIter();
        while (it.hasNext()) {
            EdgeData edge = it.next();
            sb.append("\n\t\t{").append("\n\t\t\t\"src\": ").append(edge.getSrc()).append(",\n\t\t\t\"w\": ").append(edge.getWeight()).append(",\n\t\t\t\"dest\": ").append(edge.getDest()).append("\n\t\t}");
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("\n\t],\n\t\"Nodes\": [");
        Iterator<NodeData> it2 = g.nodeIter();
        while (it2.hasNext()) {
            NodeData node = it2.next();
            GeoLocation pos = node.getLocation();
            sb.append("\n\t\t{").append("\n\t\t\t\"pos\": \"").append(pos.x()).append(",").append(pos.y()).append(",").append(pos.z()).append("\"").append(",\n\t\t\t\"id\": ").append(node.getKey()).append("\n\t\t}");
            if (it2.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("\n\t]\n}");
        assertEquals(s, sb.toString());
    }
    
    @Test
    void testEquals() {
        DWGraph g = new DWGraph();
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.addNode(new Node(i, i + 0.3));
        }
        int connects = 0;
        for (int i = 0; i < this.numberOfNodes; i++) {
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            if (g.getEdge(i, dest) == null) {
                g.connect(i, dest, i + 0.3);
                connects++;
            }
        }
        DWGraph g2 = new DWGraph();
        for (int i = 0; i < this.numberOfNodes; i++) {
            g2.addNode(new Node(i, i + 0.3));
        }
        int connects2 = 0;
        for (int i = 0; i < this.numberOfNodes; i++) {
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            if (g2.getEdge(i, dest) == null) {
                g2.connect(i, dest, i + 0.3);
                connects2++;
            }
        }
        assertEquals(g, g2);
    }
    
    @Test
    void testHashCode() {
        DWGraph g = new DWGraph();
        for (int i = 0; i < this.numberOfNodes; i++) {
            g.addNode(new Node(i, i + 0.3));
        }
        int connects = 0;
        for (int i = 0; i < this.numberOfNodes; i++) {
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            if (g.getEdge(i, dest) == null) {
                g.connect(i, dest, i + 0.3);
                connects++;
            }
        }
        DWGraph g2 = new DWGraph();
        for (int i = 0; i < this.numberOfNodes; i++) {
            g2.addNode(new Node(i, i + 0.3));
        }
        int connects2 = 0;
        for (int i = 0; i < this.numberOfNodes; i++) {
            final int whichConnect = (i + 1 + i % 2 + i % 3);
            int dest = whichConnect % this.numberOfNodes;
            if (g2.getEdge(i, dest) == null) {
                g2.connect(i, dest, i + 0.3);
                connects2++;
            }
        }
        assertEquals(g.hashCode(), g2.hashCode());
    }
    
    private DWGraph generateGraph(int numberOfNodes, int numberOfOutEdges, int numberOfInEdges, int outVariant, int inVariant) {
        DWGraph g = new DWGraph();
        ArrayList<Integer> keys = new ArrayList<>(numberOfNodes);
        int sz = g.nodeSize();
        int i = 0;
        while (sz < numberOfNodes) {
            int key = rand.nextInt();
            g.addNode(new Node(key, rand.nextDouble(), new Point3D(rand.nextDouble(), rand.nextDouble(), rand.nextDouble()), "", rand.nextInt()));
            if (sz != g.nodeSize()) {
                keys.add(key);
                i++;
                sz = g.nodeSize();
            }
        }
        
        int outCounter = 0;
        int inCounter = 0;
        int outNum = numberOfOutEdges + (outVariant != 0 ? rand.nextInt(outVariant) - 2 * outVariant : 0);
        int intNum = numberOfInEdges + (inVariant != 0 ? rand.nextInt(inVariant) - 2 * inVariant : 0);
        for (int key : keys) {
            List<Integer> k = new ArrayList<Integer>(keys);
            while (outCounter < outNum && k.size() > 0) {
                int whichKey = rand.nextInt(k.size());
                int K = k.remove(whichKey);
                outCounter++;
                g.connect(key, K, rand.nextDouble());
            }
            k = new ArrayList<Integer>(keys);
            while (inCounter < intNum && k.size() > 0) {
                int whichKey = rand.nextInt(k.size());
                int K = k.remove(whichKey);
                inCounter++;
                g.connect(K, key, rand.nextDouble());
            }
        }
        return g;
    }
}