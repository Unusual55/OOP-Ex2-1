package api;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class DWGraphAlgorithms implements DirectedWeightedGraphAlgorithms {
    
    public static void main(String[] args) {
        DWGraph g = new DWGraph();
        g.addNode(new Node(1, new Point3D(), 0.54541, "", -1));
        g.addNode(new Node(2, new Point3D(), 0.26544, "", -1));
        g.addNode(new Node(3, new Point3D(), 0.46687, "", -1));
        g.addNode(new Node(4, new Point3D(), 0.34878, "", -1));
        g.addNode(new Node(5, new Point3D(), 0.94835, "", -1));
        g.addNode(new Node(6, new Point3D(), 0.85845, "", -1));
        g.addNode(new Node(7, new Point3D(), 0.61135, "", -1));
        
        g.connect(1,2, 1);
        g.connect(1,3, 1);
        g.connect(2,4, 1);
        g.connect(2,5, 1);
        g.connect(3,6, 1);
        
        DWGraphAlgorithms alg = new DWGraphAlgorithms();
        alg.init(g);
        
        alg.DFS(g.getNode(1), (NodeData v) -> {
            Node w = (Node) v;
            System.out.println(w);
        });
    }
    
    
    DirectedWeightedGraph graph;
    
    public DWGraphAlgorithms() {
        this.graph = new DWGraph();
    }
    
    /**
     * Inits the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(DirectedWeightedGraph g) {
        this.graph = g;
    }
    
    /**
     * Returns the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public DirectedWeightedGraph getGraph() {
        return this.graph;
    }
    
    /**
     * Computes a deep copy of this weighted graph.
     *
     * @return
     */
    @Override
    public DirectedWeightedGraph copy() {
        return new DWGraph(this.graph);
    }
    
    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        return false;
    }
    
    /**
     * Computes the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }
    
    /**
     * Computes the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        return null;
    }
    
    /**
     * Finds the NodeData which minimizes the max distance to all the other nodes.
     * Assuming the graph isConnected, elese return null. See: https://en.wikipedia.org/wiki/Graph_center
     *
     * @return the Node data to which the max shortest path to all the other nodes is minimized.
     */
    @Override
    public NodeData center() {
        return null;
    }
    
    /**
     * Computes a list of consecutive nodes which go over all the nodes in cities.
     * the sum of the weights of all the consecutive (pairs) of nodes (directed) is the "cost" of the solution -
     * the lower the better.
     * See: https://en.wikipedia.org/wiki/Travelling_salesman_problem
     *
     * @param cities
     */
    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        return null;
    }
    
    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        return false;
    }
    
    /**
     * This method loads a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        return false;
    }
    
    // https://en.wikipedia.org/wiki/Graph_traversal#Depth-first_search
    // https://en.wikipedia.org/wiki/Depth-first_search
    public void DFS(NodeData v, Consumer<NodeData> func) {
        func.accept(v);
        int TAG = v.getTag();
        // label v as discovered
        v.setTag(1);
        // for all directed edges from v to w that are in G.adjacentEdges(v) do
        for (Iterator<EdgeData> it = this.graph.edgeIter(v.getKey()); it.hasNext(); ) {
            EdgeData e = it.next();
            NodeData w = this.graph.getNode(e.getDest());
            // if vertex w is not labeled as discovered then
            if (w.getTag() != 1) {
                // recursively call DFS(G, w)
                this.DFS(w, func);
            }
        }
        v.setTag(TAG);
    }
    
    public void DFS(NodeData v) {
        this.DFS(v, (NodeData w) -> {});
    }
}
