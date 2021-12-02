package DataStructures;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import java.util.*;
import java.util.function.Consumer;

public class DWGraphAlgorithms implements DirectedWeightedGraphAlgorithms {
    
    
    DirectedWeightedGraph graph;
    private final ChangeTracker<HashMap<Integer, Integer>> connectedComponentsTracker = new ChangeTracker<>();
    
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
        
        if (!this.connectedComponentsTracker.wasChanged(this.graph.getMC())) {
            return this.connectedComponentsTracker.getData();
        }
        
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
        
        this.connectedComponentsTracker.setData(components, this.graph.getMC());
        
        return components;
    }

    /**
     * This fuction return a transposed graph from the current graph using iterators
     * @return Transposed Graph
     */
    private DirectedWeightedGraph GraphTranspose(){
        DirectedWeightedGraph g=new DWGraph();
        Iterator<NodeData> nodeiter=this.graph.nodeIter();
        while(nodeiter.hasNext()){
            g.addNode(nodeiter.next());
        }
        Iterator<EdgeData> edgeiter=this.graph.edgeIter();
        while(edgeiter.hasNext()){
            EdgeData e=edgeiter.next();
            g.connect(e.getDest(), e.getSrc(), e.getWeight());
        }
        return g;
    }

    /**
     * This function calculate the shortest path to every node that start from src node and return the index
     * @param distance Empty HashMap that will contain the distances
     * @param src The source node id
     */
    private void Dijkstra(HashMap<Integer, Double> distance, int src){
        int minid=-1, unvisited=this.graph.nodeSize();
        double mindist=Double.MAX_VALUE;
        NodeData curr=this.graph.getNode(src);
        distance.put(src, 0.0);
        while (unvisited>0){
            Iterator<EdgeData> adj=this.graph.edgeIter();
            int nextid=-1;
            while(adj.hasNext()){
                EdgeData e= adj.next();
                NodeData neighbor= this.graph.getNode(e.getDest());
                if(neighbor.getTag()==1){
                    continue;
                }
                int id=neighbor.getKey();
                neighbor.setTag(0);
                if(minid==-1){
                    minid=id;
                }
                if(nextid==-1){
                    nextid=id;
                }
                if(!distance.containsKey(id)){
                    distance.put(id, Double.MAX_VALUE);
                }
                if(distance.get(id)>distance.get(src)+e.getWeight()){
                    distance.replace(id, distance.get(id), distance.get(src)+e.getWeight());
                    if(distance.get(nextid)>distance.get(id)){
                        nextid=id;
                    }
                }
            }
            if(distance.get(minid)>distance.get(nextid)){
                minid=nextid;
                mindist=distance.get(minid);
            }
            unvisited--;
            curr.setTag(1);
            curr=this.graph.getNode(nextid);
        }
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
        HashMap<Integer, Double> dist=new HashMap<>();
        this.Dijkstra(dist, src);
        return dist.get(dest);
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
        HashMap<Integer, Double> dist=new HashMap<>();
        this.Dijkstra(dist, src);
        DirectedWeightedGraph transposed=this.GraphTranspose();
        int curr=dest;
        Stack<NodeData> path=new Stack<NodeData>();
        while(curr!=src){
            int nextid=-1;
            Iterator<EdgeData> edgeiter=transposed.edgeIter(curr);
            while(edgeiter.hasNext()){
                EdgeData e=edgeiter.next();
                if(nextid==-1){
                    nextid=e.getSrc();
                }
                if(dist.get(e.getSrc())+e.getWeight()==dist.get(curr)){
                    nextid=transposed.getNode(e.getSrc()).getKey();
                    break;
                }
            }
            path.push(this.graph.getNode(curr));
            curr=nextid;
        }
        List<NodeData> ret=new LinkedList<NodeData>();
        for (NodeData n:path) {
            ret.add(0,n);
        }
        return ret;
    }
    
    /**
     * Finds the NodeData which minimizes the max distance to all the other nodes.
     * Assuming the graph isConnected, elese return null. See: https://en.wikipedia.org/wiki/Graph_center
     *
     * @return the Node data to which the max shortest path to all the other nodes is minimized.
     */
    @Override
    public NodeData center() {
        if(!isConnected()){
            return null;
        }
        int minid=-1;
        double mindistance=Double.MAX_VALUE;
        Iterator<NodeData> nodes=graph.nodeIter();
        while(nodes.hasNext()){
            Node n= (Node) nodes.next();
            int id=n.getKey();
            double longest=this.LongestPath(n);
            if(mindistance>longest){
                mindistance=longest;
                minid=id;
            }
        }
        return graph.getNode(minid);
    }

    /**
     * This function Calculate the distance of the longest path that start from a node to any
     * other vertex in the graph
     * @param s the source vertex
     * @return The distance of the longest path
     */
    private double LongestPath(NodeData s){
        int unvisitedcouter= graph.nodeSize();
        DWGraph g= (DWGraph) this.copy();
        HashMap<Integer, Double> distance=new HashMap<>();
        int maxid=-1;
        while(unvisitedcouter>0){
            Iterator<EdgeData> Adj=g.edgeIter(s.getKey());
            int nextid=-1;
            while(Adj.hasNext()){
                Edge e= (Edge) Adj.next();
                Node n= (Node) g.getNode(e.getDest());
                int id=e.getDest();
                if(maxid==-1){
                    maxid=id;
                }
                if(!distance.containsKey(id)){
                    distance.put(id,0.0);
                }
                if(n.getTag()==1){
                    continue;
                }
                n.setTag(0);
                if(distance.get(id)<distance.get(s.getKey())+e.getWeight()){
                    distance.replace(id,distance.get(id),distance.get(s.getKey())+e.getWeight());
                    nextid=id;
                    if(distance.get(maxid)<distance.get(id)){
                        maxid=id;
                    }
                }
            }
            s.setTag(1);
            unvisitedcouter--;
            s= (Node) g.getNode(nextid);
        }
//        return distance.get(maxid);
        double ret=0;
        for (double x: distance.keySet()) {
            ret+=x;
        }
        return ret;
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
