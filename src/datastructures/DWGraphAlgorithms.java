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
        g.addNode(new Node(1, new Point3D(0.321, 0.123, 0.456)));
        g.addNode(new Node(2, new Point3D(0.123, 0.456, 0.321)));
        g.addNode(new Node(3, new Point3D(0.456, 0.321, 0.123)));
        g.addNode(new Node(4, new Point3D(0.321, 0.123, 0.456)));
        g.addNode(new Node(5, new Point3D(0.123, 0.456, 0.321)));
        
        g.connect(1, 2, 43.675);
        g.connect(1, 4, 23.456);
        g.connect(2, 3, 34.567);
        g.connect(2, 4, 45.678);
        g.connect(4, 5, 67.890);
    
        DWGraphAlgorithms alg = new DWGraphAlgorithms();
        alg.init(g);
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
