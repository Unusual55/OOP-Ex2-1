package datastructures;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datastructures.serializers.EdgeAdapter;
import datastructures.serializers.GraphAdapter;
import datastructures.serializers.NodeAdapter;

public class DWGraphAlgorithms implements DirectedWeightedGraphAlgorithms {
    
    public static void main(String[] args) {
        DWGraph g = new DWGraph();
        g.addNode(new Node('a'));
        g.addNode(new Node('b'));
        g.addNode(new Node('c'));
        g.addNode(new Node('d'));
        g.addNode(new Node('e'));
        g.addNode(new Node('f'));
        g.addNode(new Node('g'));
        g.addNode(new Node('h'));
        
        g.connect('a', 'b', 1.0);
        g.connect('b', 'c', 1.0);
        g.connect('b', 'f', 1.0);
        g.connect('b', 'e', 1.0);
        g.connect('c', 'd', 1.0);
        g.connect('c', 'g', 1.0);
        g.connect('d', 'c', 1.0);
        g.connect('d', 'h', 1.0);
        g.connect('e', 'a', 1.0);
        g.connect('e', 'f', 1.0);
        g.connect('f', 'g', 1.0);
        g.connect('g', 'f', 1.0);
        g.connect('h', 'd', 1.0);
        g.connect('h', 'g', 1.0);
        
        DWGraphAlgorithms alg = new DWGraphAlgorithms();
        alg.init(g);
        HashMap<Integer, HashSet<Integer>> map = alg.SCC();
//        Iterator<Integer> it = map.keySet().iterator();
//        while (it.hasNext()) {
//            int key = it.next();
//            System.out.print("Component " + key + ": ");
//            for (int id : map.get(key)) {
//                System.out.print((char)id + " ");
//            }
//            System.out.println();
//        }
        System.out.println(map);
//        alg.save("graph.json");
//        alg.init(new DWGraph());
//        alg.load("graph.json");
//        System.out.println(alg.getGraph().edgeSize());
    }
    
    private DirectedWeightedGraph graph;
    
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
    
    public HashMap<Integer, HashSet<Integer>> SCC() {
        int[] globals = {1};
        HashMap<Integer, Integer> index = new HashMap<>();
        HashMap<Integer, Integer> lowlink = new HashMap<>();
        Stack<Integer> stack = new Stack<>();
        HashMap<Integer, Boolean> onStack = new HashMap<>();
        HashMap<Integer, HashSet<Integer>> scc = new HashMap<>();
        Iterator<NodeData> it = this.graph.nodeIter();
        while (it.hasNext()) {
            NodeData n = it.next();
            int v = n.getKey();
            if (index.get(v) == null) {
                this.strongconnect(v, globals, index, lowlink, stack, onStack, scc);
            }
        }
        return scc;
    }
    
    private void strongconnect(int v, int[] globals, HashMap<Integer, Integer> index, HashMap<Integer, Integer> lowlink, Stack<Integer> stack, HashMap<Integer, Boolean> onStack, HashMap<Integer, HashSet<Integer>> scc) {
        index.put(v, globals[0]);
        lowlink.put(v, globals[0]);
        globals[0]++;
        stack.push(v);
        onStack.put(v, true);
        Iterator<EdgeData> it = this.graph.edgeIter(v);
        while (it.hasNext()) {
            EdgeData e = it.next();
            int w = e.getDest();
            if (index.get(w) == null) {
                strongconnect(w, globals, index, lowlink, stack, onStack, scc);
                lowlink.put(v, Math.min(lowlink.get(v), lowlink.get(w)));
            } else if (onStack.get(w)) {
                lowlink.put(v, Math.min(lowlink.get(v), index.get(w)));
            }
        }
        if (Objects.equals(lowlink.get(v), index.get(v))) {
            HashSet<Integer> component = new HashSet<>();
            int w;
            do {
                w = stack.pop();
                onStack.put(w, false);
                component.add(w);
            } while (w != v);
            scc.put(scc.size(), component);
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
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(EdgeData.class, new EdgeAdapter())
                .registerTypeAdapter(NodeData.class, new NodeAdapter())
                .setPrettyPrinting();
        Gson g = builder.create();
        try {
            FileWriter writer = new FileWriter(file);
            g.toJson(GraphAdapter.fromGraph(this.graph), writer);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(EdgeData.class, new EdgeAdapter())
                .registerTypeAdapter(NodeData.class, new NodeAdapter())
                .setPrettyPrinting();
        Gson g = builder.create();
        try {
            FileReader reader = new FileReader(file);
            this.graph = GraphAdapter.toGraph(g.fromJson(reader, GraphAdapter.class));
            reader.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * This function calculate the shortest path to every node that start from src node and return the index
     *
     * @param distances Empty HashMap that will contain the distances
     * @param src       The source node id
     */
    private void Dijkstra(HashMap<Integer, Double> distances, int src) {
        Iterator<NodeData> it = this.graph.nodeIter();
        while (it.hasNext()) {
            NodeData n = it.next();
            distances.put(n.getKey(), Double.MAX_VALUE);
        }
        PriorityQueue<NodeData> pq = new PriorityQueue<>(this.graph.nodeSize());
        pq.add(new Node(src, 0.0));
        distances.put(src, 0.0);
        HashSet<Integer> settled = new HashSet<>();
        
        while (settled.size() != this.graph.nodeSize()) {
            if (pq.isEmpty())
                return;
            int u = pq.remove().getKey();
            if (settled.contains(u))
                continue;
            settled.add(u);
            double edgeDistance = -1;
            double newDistance = -1;
            
            Iterator<EdgeData> eIt = this.graph.edgeIter(u);
            while (eIt.hasNext()) {
                EdgeData e = eIt.next();
                NodeData v = this.graph.getNode(e.getDest());
                
                // If current node hasn't already been processed
                if (!settled.contains(v.getKey())) {
                    edgeDistance = v.getWeight();
                    newDistance = distances.get(u) + edgeDistance;
                    
                    // If new distance is cheaper in cost
                    if (newDistance < distances.get(v.getKey()))
                        distances.put(v.getKey(), newDistance);
                    
                    // Add the current node to the queue
                    pq.add(new Node(v.getKey(), distances.get(v.getKey())));
                }
            }
        }
    }
    
}
