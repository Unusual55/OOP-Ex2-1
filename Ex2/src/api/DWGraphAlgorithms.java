package api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

public class DWGraphAlgorithms implements DirectedWeightedGraphAlgorithms {
    
    
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
    
    
    // Private class used for the isConnected method, We made this in order to not change the Node Data type.
    private class IsConnectedProps {
        public int index;
        public int lowlink;
        public boolean onstack;
        
        public IsConnectedProps(int index, int lowlink, boolean onstack) {
            this.index = index;
            this.lowlink = lowlink;
            this.onstack = onstack;
        }
    }
    
    
    /**
     * https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm
     * <p>
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        HashMap<Integer, Integer> components = this.stronglyConnectedComponents();
        int prev = -1;
        for (Integer c : components.values()) {
            if (prev != -1 && prev != c) {
                return false;
            }
            prev = c;
        }
        return true;
    }
    
    
    private void isConnected(NodeData v, int[] globals, HashMap<Integer, IsConnectedProps> props, Stack<NodeData> stack, HashMap<Integer, Integer> output) {
        int key = v.getKey();
        IsConnectedProps pv = props.get(key);
        pv.index = globals[0];
        pv.lowlink = globals[0];
        globals[0]++;
        stack.push(v);
        pv.onstack = true;
        
        for (Iterator<EdgeData> it = this.graph.edgeIter(key); it.hasNext(); ) {
            EdgeData edge = it.next();
            NodeData w = this.graph.getNode(edge.getDest());
            IsConnectedProps pw = props.get(w.getKey());
            if (pw.index == -1) {
                this.isConnected(w, globals, props, stack, output);
                pv.lowlink = Math.min(pv.lowlink, pw.lowlink);
            } else if (pw.onstack) {
                pv.lowlink = Math.min(pv.lowlink, pw.index);
            }
        }
        if (pv.lowlink == pv.index) {
            NodeData w;
            globals[1]++;
            do {
                w = stack.pop();
                output.put(w.getKey(), globals[1]);
                props.get(w.getKey()).onstack = false;
            } while (!v.equals(w));
        }
    }
    
    private HashMap<Integer, Integer> stronglyConnectedComponents() {
        HashMap<Integer, IsConnectedProps> props = new HashMap<>();
        HashMap<Integer, Integer> components = new HashMap<>();
        int index = 0;
        int numOfComponents = 0;
        int[] globals = new int[]{index, numOfComponents};
        Stack<NodeData> stack = new Stack<>();
        
        for (Iterator<NodeData> it = this.graph.nodeIter(); it.hasNext(); ) {
            NodeData v = it.next();
            props.put(v.getKey(), new IsConnectedProps(-1, -1, false));
            components.put(v.getKey(), -1);
        }
        
        for (Iterator<NodeData> it = this.graph.nodeIter(); it.hasNext(); ) {
            NodeData v = it.next();
            IsConnectedProps pv = props.get(v.getKey());
            if (pv.index == -1) {
                this.isConnected(v, globals, props, stack, components);
            }
        }
        return components;
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
        this.DFS(v, (NodeData w) -> {
        });
    }
}
