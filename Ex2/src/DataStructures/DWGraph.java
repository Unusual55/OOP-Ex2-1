package DataStructures;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;

import java.util.*;
import java.util.function.Consumer;

/**
 * This class represent graph data structure- G=(V,E) G- graph object, V- vertices, E- edges
 * The properties of graph are HashMap which contain the Nodes, HashMap which contain the edges,
 * counter of nodes, counter of edges and counter of changes in the graph
 */
public class DWGraph implements DirectedWeightedGraph {

    private HashMap<Integer, NodeData> nodes;
    private HashMap<Integer, HashMap<Integer, EdgeData>> edges;
    private int nodeCount;
    private int edgeCount;
    private int modeCount;

    public DWGraph() {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        this.nodeCount = 0;
        this.edgeCount = 0;
        this.modeCount = 0;

    }

    public DWGraph(DirectedWeightedGraph other) {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        for (Iterator<NodeData> it = other.nodeIter(); it.hasNext(); ) {
            NodeData node = it.next();
            this.addNode(node);
        }

        for (Iterator<NodeData> it = this.nodeIter(); it.hasNext(); ) {
            NodeData node = it.next();
            int key = node.getKey();
            for (Iterator<EdgeData> iter = other.edgeIter(key); iter.hasNext(); ) {
                EdgeData edge = iter.next();
                EdgeData e = new Edge(edge);
                this.edges.get(e.getSrc()).put(e.getDest(), e);
            }
        }
    }

    /**
     * returns the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public NodeData getNode(int key) {
        return this.nodes.get(key);
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public EdgeData getEdge(int src, int dest) {
        if (this.nodes.containsKey(src) && this.nodes.containsKey(dest)) {
            return this.edges.get(src).get(dest);
        }
        return null;
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n
     */
    @Override
    public void addNode(NodeData n) {
        int key = n.getKey();
        if (this.nodes.containsKey(key)) return;
        this.nodes.put(key, n);
        this.edges.put(key, new HashMap<>());
        this.nodeCount++;
        this.modeCount++;
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (src == dest) return;
        if (w < 0) throw new IllegalArgumentException("Weight cannot be negative");

        if (this.getEdge(src, dest) != null && this.edges.get(src).get(dest).getWeight() != w) {
            EdgeData e = new Edge(src, dest, w);
            this.edges.get(src).put(dest, e);
            this.modeCount++;
        } else if (this.nodes.containsKey(src) && this.nodes.containsKey(dest) && this.getEdge(src, dest) == null) {
            EdgeData e = new Edge(src, dest, w);
            this.edges.get(src).put(dest, e);
            this.edgeCount++;
            this.modeCount++;
        }

    }

    /**
     * This method returns an Iterator for the
     * collection representing all the nodes in the graph.
     * Note: if the graph was changed since the iterator was constructed - a RuntimeException should be thrown.
     *
     * @return Iterator<node_data>
     */
    @Override
    public Iterator<NodeData> nodeIter() {
        return new Iterator<NodeData>() {
            private  Iterator<NodeData> it = nodes.values().iterator();
            public int startModeCounter = modeCount;
            private Node n=null;

            @Override
            public boolean hasNext() {
                if (modeCount != startModeCounter)
                    throw new RuntimeException("Graph was changed since iterator was constructed");
                return it.hasNext();
            }

            @Override
            public NodeData next() {
                if (modeCount != startModeCounter)
                    throw new RuntimeException("Graph was changed since iterator was constructed");
                n= (Node) it.next();
                return n;
            }

            @Override
            public void remove(){
                if (modeCount != startModeCounter)
                    throw new RuntimeException("Graph was changed since iterator was constructed");
                if (n != null) {
                    removeNode(n.getKey());
                    this.startModeCounter = getMC();
                }
            }

            @Override
            public void forEachRemaining(Consumer<? super NodeData> action) {
                Iterator.super.forEachRemaining(action);
            }
        };
    }

    /**
     * This method returns an Iterator for all the edges in this graph.
     * Note: if any of the edges going out of this node were changed since the iterator was constructed - a RuntimeException should be thrown.
     *
     * @return Iterator<EdgeData>
     */
    @Override
    public Iterator<EdgeData> edgeIter() {
        return new Iterator<EdgeData>() {
            private Iterator<HashMap<Integer, EdgeData>> it = edges.values().iterator();
            private Iterator<EdgeData> edge = null;
            public int startModeCounter = modeCount;
            private EdgeData e = null;
            private int edgecount=edgeCount;

            @Override
            public boolean hasNext() {
                if (modeCount != startModeCounter)
                    throw new RuntimeException("Graph was changed since iterator was constructed");
                if(edge==null&&!it.hasNext()){
                    return false;
                }
                else if(edge==null&&it.hasNext()){
                    return true;
                }
                return true;
            }

            @Override
            public EdgeData next() {
                if (modeCount != startModeCounter)
                    throw new RuntimeException("Graph was changed since iterator was constructed");
                if (edge == null || !edge.hasNext()) {
                    edge = it.next().values().iterator();
                }
                e = edge.next();
                edgecount--;
                return e;
            }

            @Override
            public void remove() {
                if (modeCount != startModeCounter)
                    throw new RuntimeException("Graph was changed since iterator was constructed");
                if (e != null) {
                    removeEdge(e.getSrc(), e.getDest());
                    this.startModeCounter = getMC();
                }
            }

            @Override
            public void forEachRemaining(Consumer<? super EdgeData> action) {
                Iterator.super.forEachRemaining(action);
            }
        };
    }

    /**
     * This method returns an Iterator for edges getting out of the given node (all the edges starting (source) at the given node).
     * Note: if the graph was changed since the iterator was constructed - a RuntimeException should be thrown.
     *
     * @param node_id
     * @return Iterator<EdgeData>
     */
    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return new Iterator<EdgeData>() {
            private Iterator<EdgeData> it = edges.get(node_id).values().iterator();
            public  int startModeCounter = modeCount;
            private Edge e=null;

            @Override
            public boolean hasNext() {
                if (modeCount != startModeCounter)
                    throw new RuntimeException("Graph was changed since iterator was constructed");
                return it.hasNext();
            }

//            @Override
//            public EdgeData next() {
//                if (modeCount != startModeCounter)
//                    throw new RuntimeException("Graph was changed since iterator was constructed");
//                if(this.hasNext()) {
//                    e = (Edge) it.next();
//                }
//                return null;
//            }
            @Override
            public EdgeData next() {
                if (modeCount != startModeCounter)
                    throw new RuntimeException("Graph was changed since iterator was constructed");
                if(it.hasNext()){
                    e= (Edge) it.next();
                }
                else{
                    return null;
                }
                return e;
            }

            @Override
            public void remove(){
                if (modeCount != startModeCounter)
                    throw new RuntimeException("Graph was changed since iterator was constructed");
                if (e != null) {
                    removeEdge(e.getSrc(), e.getDest());
                    this.startModeCounter = getMC();
                }
            }

            @Override
            public void forEachRemaining(Consumer<? super EdgeData> action) {
                Iterator.super.forEachRemaining(action);
            }
        };
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public NodeData removeNode(int key) {
        if (!this.nodes.containsKey(key)) return null;
        int sz = this.edges.get(key).size();
        this.edges.remove(key);
        this.edgeCount -= sz;
        this.modeCount += sz;
        for (int i : this.edges.keySet()) {
            if (this.edges.get(i).containsKey(key)) {
                this.edges.get(i).remove(key);
                this.edgeCount--;
                this.modeCount++;
            }
        }
        NodeData n = this.nodes.remove(key);
        if (n != null) {
            this.nodeCount--;
            this.modeCount++;
        }
        return n;

    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public EdgeData removeEdge(int src, int dest) {
        EdgeData e = this.edges.get(src).remove(dest);
        if (e != null) {
            this.edgeCount--;
            this.modeCount++;
        }
        return e;
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return this.nodeCount;
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return this.edgeCount;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return
     */
    @Override
    public int getMC() {
        return this.modeCount;
    }
    /**
     * This function returns a string representation of this object
     * @return String that represent this object
     */
    @Override
    public String toString() {
        StringBuilder nodesValue = new StringBuilder("{");
        for (NodeData n : this.nodes.values()) {
            nodesValue.append(n);
        }
        nodesValue.append("}");

        StringBuilder edgesValue = new StringBuilder("{");
        for (HashMap<Integer, EdgeData> h : this.edges.values()) {
            if (!h.values().isEmpty()) {
                edgesValue.append("{ ");
                for (Object o : h.values()) {
                    EdgeData e = (Edge) o;
                    edgesValue.append(e).append(" ");

                }
                edgesValue.append("}");
            }
        }
        edgesValue.append("}");

        return "DWGraph{" +
                "nodes=" + nodesValue +
                ", edges=" + edgesValue +
                '}';
    }

    /**
     * This function get a object as input and compare it to this object
     * @param o The object which we compare to this object
     * @return True if they are equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectedWeightedGraph other = (DWGraph) o;
        return this.nodeCount == other.nodeSize() &&
                this.edgeCount == other.edgeSize() &&
                this.modeCount == other.getMC() &&
                this.nodeEquals(other.nodeIter()) &&
                this.edgeEquals(other.edgeIter());
    }

    /**
     * This function compare between two iterators of Nodes
     * @param other Iterator of Nodes
     * @return True if they are equal, otherwise false
     */
    private boolean nodeEquals(Iterator<NodeData> other) {
        Iterator<NodeData> thisIter = this.nodeIter();
        while (thisIter.hasNext() && other.hasNext()) {
            Node n1 = (Node) thisIter.next();
            Node n2 = (Node) other.next();
            if (!n1.equals(n2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This function compare between two iterators of Edges
     * @param other Iterator of edges
     * @return True if they are equal, otherwise false
     */
    private boolean edgeEquals(Iterator<EdgeData> other) {
        Iterator<EdgeData> thisIter = this.edgeIter();
        while (thisIter.hasNext() && other.hasNext()) {
            Edge n1 = (Edge) thisIter.next();
            Edge n2 = (Edge) other.next();
            if (!n1.equals(n2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Since we have overridden equals we also are required to override hashCode
     *
     * @return the hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.nodes, this.edges, this.nodeCount, this.edgeCount, this.modeCount);
    }

    /**
     *This function returns the nodes HashMap of this graph
     * @return HashMap of Nodes
     */
    public HashMap<Integer, NodeData> getNodes() {
        return this.nodes;
    }

    /**
     * This function returns the edges HashMap of this graph
     * @return HashMap of Edges
     */
    public HashMap<Integer, HashMap<Integer, EdgeData>> getEdges() {
        return this.edges;
    }


}
