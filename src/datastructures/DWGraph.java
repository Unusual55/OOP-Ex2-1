package datastructures;

import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import api.DirectedWeightedGraph;

import java.util.*;
import java.util.function.Consumer;

public class DWGraph implements DirectedWeightedGraph {
    
    
    private HashMap<Integer, NodeData> nodes;
    private HashMap<Integer, HashMap<Integer, EdgeData>> outEdges;
    private HashMap<Integer, HashSet<Integer>> inEdges;
    private int nodeCounter;
    private int edgeCounter;
    private int modeCounter;
    
    //region Constructors
    
    /**
     * Default constructor.
     */
    public DWGraph() {
        this.nodes = new HashMap<>();
        this.outEdges = new HashMap<>();
        this.inEdges = new HashMap<>();
        this.nodeCounter = 0;
        this.edgeCounter = 0;
        this.modeCounter = 0;
    }
    
    /**
     * Copy constructor
     *
     * @param other DirectedWeightedGraph object to copy
     */
    public DWGraph(DirectedWeightedGraph other) {
        this.nodes = new HashMap<>();
        this.outEdges = new HashMap<>();
        this.inEdges = new HashMap<>();
        this.nodeCounter = 0;
        this.edgeCounter = 0;
        Iterator<NodeData> it = other.nodeIter();
        while (it.hasNext()) {
            NodeData n = it.next();
            this.addNode(new Node(n));
        }
        it = other.nodeIter();
        while (it.hasNext()) {
            NodeData n = it.next();
            int key = n.getKey();
            Iterator<EdgeData> it2 = other.edgeIter(key);
            if (it2 != null) {
                while (it2.hasNext()) {
                    EdgeData e = it2.next();
                    this.connect(key, e.getDest(), e.getWeight());
                }
            }
        }
        this.modeCounter = other.getMC();
    }
    
    //endregion
    
    //region Graph modification
    
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
     * @param src  The source of the edge.
     * @param dest The destination of the edge.
     * @return the data of the edge (src,dest), null if none.
     */
    @Override
    public EdgeData getEdge(int src, int dest) {
        return this.outEdges.containsKey(src) ? this.outEdges.get(src).get(dest) : null; // If dest is not in the map, it will return null.
    }
    
    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n The node to be added.
     */
    @Override
    public void addNode(NodeData n) {
        if (this.nodes.put(n.getKey(), n) == null) {
            this.modeCounter++;
            this.nodeCounter++;
        }
    }
    
    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc.) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (w <= 0.0) {
            throw new IllegalArgumentException("Weight must be positive.");
        }
        if (src == dest || this.getNode(src) == null || this.getNode(dest) == null) {
            return;
        }
        Edge e = new Edge(src, dest, w);
        if (!this.outEdges.containsKey(src)) {
            this.outEdges.put(src, new HashMap<>());
        }
        if (!this.inEdges.containsKey(dest)) {
            this.inEdges.put(dest, new HashSet<>());
        }
        this.outEdges.get(src).put(dest, e);
        if (this.inEdges.get(dest).add(src)) {
            this.edgeCounter++;
        }
        this.modeCounter++;
    }
    
    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key The id of the node
     * @return the data of the removed node (null if none).
     */
    @Override
    public NodeData removeNode(int key) {
        NodeData removed = this.nodes.remove(key);
        if (removed != null) {
            if (this.inEdges.containsKey(key)) {
                for (int connectedNode : this.inEdges.get(key)) {
                    this.outEdges.get(connectedNode).remove(key);
                    if (this.outEdges.get(connectedNode).isEmpty()) {
                        this.outEdges.remove(connectedNode);
                    }
                }
                this.edgeCounter -= this.inEdges.get(key).size();
                this.inEdges.remove(key);
            }
            if (this.outEdges.containsKey(key)) {
                for (EdgeData e : this.outEdges.get(key).values()) {
                    this.inEdges.get(e.getDest()).remove(key);
                    if (this.inEdges.get(e.getDest()).isEmpty()) {
                        this.inEdges.remove(e.getDest());
                    }
                }
                this.edgeCounter -= this.outEdges.get(key).size();
                this.outEdges.remove(key);
            }
            this.nodeCounter--;
            this.modeCounter++;
        }
        return removed;
    }
    
    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param src  The id of the source node
     * @param dest The id of the destination node
     * @return the data of the removed edge (null if none).
     */
    @Override
    public EdgeData removeEdge(int src, int dest) {
        if (!this.nodes.containsKey(src) || !this.nodes.containsKey(dest) || !this.outEdges.containsKey(src)) {
            return null;
        }
        EdgeData removed = this.outEdges.get(src).remove(dest);
        if (removed != null) {
            this.edgeCounter--;
            this.modeCounter++;
        }
        this.inEdges.remove(dest);
        return removed;
    }
    
    //endregion
    
    //region Iterators
    
    /**
     * This method returns an Iterator for the
     * collection representing all the nodes in the graph.
     * Note: if the graph was changed since the iterator was constructed - a RuntimeException should be thrown.
     *
     * @return Iterator<node_data>
     */
    @Override
    public Iterator<NodeData> nodeIter() {
        return new Iterator<>() {
            private int mode = DWGraph.this.modeCounter;
            private final Iterator<NodeData> it = DWGraph.this.nodes.values().iterator();
            private NodeData next = null;
            
            @Override
            public boolean hasNext() {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                return this.it.hasNext();
            }
            
            @Override
            public NodeData next() {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                return this.next = this.it.next();
            }
            
            @Override
            public void remove() {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                if (this.next != null) {
                    this.it.remove();
                    DWGraph.this.removeNode(this.next.getKey());
                    this.mode = DWGraph.this.modeCounter;
                }
                
            }
            
            @Override
            public void forEachRemaining(Consumer<? super NodeData> action) {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
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
        return new Iterator<>() {
            private int mode = DWGraph.this.modeCounter;
            private final Iterator<HashMap<Integer, EdgeData>> outerIt = DWGraph.this.outEdges.values().iterator();
            private Iterator<EdgeData> innerIt = null;
            private EdgeData next = null;
            
            @Override
            public boolean hasNext() {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                if (this.innerIt == null) return this.outerIt.hasNext();
                return this.outerIt.hasNext() || this.innerIt.hasNext();
            }
            
            @Override
            public EdgeData next() {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                if (this.innerIt == null || !this.innerIt.hasNext()) {
                    this.innerIt = this.outerIt.next().values().iterator();
                }
                return this.next = this.innerIt.next();
            }
            
            @Override
            public void remove() {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                if (this.next != null) {
                    DWGraph.this.removeEdge(this.next.getSrc(), this.next.getDest());
                    this.mode = DWGraph.this.modeCounter;
                }
            }
            
            @Override
            public void forEachRemaining(Consumer<? super EdgeData> action) {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                Iterator.super.forEachRemaining(action);
            }
        };
    }
    
    /**
     * This method returns an Iterator for edges getting out of the given node (all the edges starting (source) at the given node).
     * Note: if the graph was changed since the iterator was constructed - a RuntimeException should be thrown.
     *
     * @param node_id The id of the node
     * @return Iterator<EdgeData>
     */
    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return new Iterator<>() {
            private int mode = DWGraph.this.modeCounter;
            private final Iterator<EdgeData> it = DWGraph.this.outEdges.containsKey(node_id) ? DWGraph.this.outEdges.get(node_id).values().iterator() : null;
            private EdgeData next = null;
            
            @Override
            public boolean hasNext() {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                return this.it != null && this.it.hasNext();
            }
            
            @Override
            public EdgeData next() {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                if (this.it == null) {
                    throw new NoSuchElementException();
                }
                return this.next = this.it.next();
            }
            
            @Override
            public void remove() {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                if (this.it == null) {
                    throw new NoSuchElementException();
                }
                if (this.next != null) {
                    this.it.remove();
                    DWGraph.this.removeEdge(this.next.getSrc(), this.next.getDest());
                    this.mode = DWGraph.this.modeCounter;
                }
            }
            
            @Override
            public void forEachRemaining(Consumer<? super EdgeData> action) {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                Iterator.super.forEachRemaining(action);
            }
        };
    }
    
    public Iterator<EdgeData> inEdgeIter(int node_id) {
        return new Iterator<>() {
            private int mode = DWGraph.this.modeCounter;
            private final Iterator<Integer> it = DWGraph.this.inEdges.containsKey(node_id) ? DWGraph.this.inEdges.get(node_id).iterator() : null;
            private EdgeData next = null;
            
            @Override
            public boolean hasNext() {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                return this.it != null && this.it.hasNext();
            }
            
            @Override
            public EdgeData next() {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                if (this.it == null) {
                    throw new NoSuchElementException();
                }
                return this.next = DWGraph.this.outEdges.get(this.it.next()).get(node_id);
            }
            
            @Override
            public void remove() {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                if (this.it == null) {
                    throw new NoSuchElementException();
                }
                if (this.next != null) {
                    this.it.remove();
                    DWGraph.this.removeEdge(this.next.getSrc(), this.next.getDest());
                    this.mode = DWGraph.this.modeCounter;
                }
                
            }
            
            @Override
            public void forEachRemaining(Consumer<? super EdgeData> action) {
                if (this.mode != DWGraph.this.modeCounter) {
                    throw new RuntimeException("Graph was changed since iterator was constructed.");
                }
                Iterator.super.forEachRemaining(action);
            }
        };
    }
    
    //endregion
    
    //region Counters
    
    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return The number of vertices (nodes) in the graph.
     */
    @Override
    public int nodeSize() {
        return this.nodes.size();
    }
    
    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return The number of edges (assume directional graph).
     */
    @Override
    public int edgeSize() {
        return this.edgeCounter;
    }
    
    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return The Mode Count - for testing changes in the graph.
     */
    @Override
    public int getMC() {
        return this.modeCounter;
    }
    
    //endregion
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n\t\"Edges\": [");
        Iterator<EdgeData> it = this.edgeIter();
        while (it.hasNext()) {
            EdgeData edge = it.next();
            sb.append("\n\t\t{").append("\n\t\t\t\"src\": ").append(edge.getSrc()).append(",\n\t\t\t\"w\": ").append(edge.getWeight()).append(",\n\t\t\t\"dest\": ").append(edge.getDest()).append("\n\t\t}");
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("\n\t],\n\t\"Nodes\": [");
        Iterator<NodeData> it2 = this.nodeIter();
        while (it2.hasNext()) {
            NodeData node = it2.next();
            GeoLocation pos = node.getLocation();
            sb.append("\n\t\t{").append("\n\t\t\t\"pos\": \"").append(pos.x()).append(",").append(pos.y()).append(",").append(pos.z()).append("\"").append(",\n\t\t\t\"id\": ").append(node.getKey()).append("\n\t\t}");
            if (it2.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("\n\t]\n}");
        
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        DWGraph g = (DWGraph)o;
        return this.nodeSize() == g.nodeSize() &&
                this.edgeSize() == g.edgeSize() &&
                this.getMC() == g.getMC() &&
                this.nodesMapEqual(g.nodes) &&
                this.outEdgesMapEqual(g.outEdges) &&
                this.inEdgesMapEqual(g.inEdges);
    }
    
    private boolean nodesMapEqual(HashMap<Integer, NodeData> other) {
        if (this.nodes == other) return true;
        if (this.nodes.size() != other.size()) return false;
        if (!this.nodes.keySet().equals(other.keySet())) return false;
        Iterator<NodeData> it = this.nodes.values().iterator();
        Iterator<NodeData> it2 = other.values().iterator();
        while (it.hasNext() && it2.hasNext()) {
            NodeData n1 = it.next();
            NodeData n2 = it2.next();
            if (!n1.equals(n2)) return false;
        }
        return true;
    }
    
    private boolean outEdgesMapEqual(HashMap<Integer, HashMap<Integer, EdgeData>> other) {
        if (this.outEdges == other) return true;
        if (!this.outEdges.keySet().equals(other.keySet())) return false;
        Iterator<HashMap<Integer, EdgeData>> IT1 = this.outEdges.values().iterator();
        Iterator<HashMap<Integer, EdgeData>> IT2 = other.values().iterator();
        while (IT1.hasNext() && IT2.hasNext()) {
            HashMap<Integer, EdgeData> map1 = IT1.next();
            HashMap<Integer, EdgeData> map2 = IT2.next();
            if (!map1.keySet().equals(map2.keySet())) return false;
            Iterator<EdgeData> it1 = map1.values().iterator();
            Iterator<EdgeData> it2 = map2.values().iterator();
            while (it1.hasNext() && it2.hasNext()) {
                EdgeData e1 = it1.next();
                EdgeData e2 = it2.next();
                if (!e1.equals(e2)) return false;
            }
        }
        return true;
    }
    
    private boolean inEdgesMapEqual(HashMap<Integer, HashSet<Integer>> other) {
        if (this.inEdges == other) return true;
        if (!this.inEdges.keySet().equals(other.keySet())) return false;
        Iterator<HashSet<Integer>> IT1 = this.inEdges.values().iterator();
        Iterator<HashSet<Integer>> IT2 = other.values().iterator();
        while (IT1.hasNext() && IT2.hasNext()) {
            HashSet<Integer> set1 = IT1.next();
            HashSet<Integer> set2 = IT2.next();
            if (set1.size() != set2.size()) return false;
            if (!set1.equals(set2)) return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.nodes, this.outEdges, this.inEdges, this.nodeSize(), this.edgeSize(), this.getMC());
    }
    
}
