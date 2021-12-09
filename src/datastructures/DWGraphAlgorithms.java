package datastructures;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datastructures.serializers.EdgeAdapter;
import datastructures.serializers.GraphAdapter;
import datastructures.serializers.NodeAdapter;

public class DWGraphAlgorithms implements DirectedWeightedGraphAlgorithms {
    
    
    private DirectedWeightedGraph graph;
    private final ChangeTracker<Boolean> isConnectedTracker = new ChangeTracker<>();
//   TODO: implement Dijkstra change tracker - (need to change last src that was used)
//    private final ChangeTracker<HashMap<Integer, Double>> shortestDistanceTracker = new ChangeTracker<>();
//    private final ChangeTracker<HashMap<Integer, LinkedList<Integer>>> shortestPathTracker = new ChangeTracker<>();
    
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
        if (!this.isConnectedTracker.wasChanged(this.graph.getMC())) {
            return this.isConnectedTracker.getData();
        }
        if (this.graph.nodeSize() <= 1) {
            this.isConnectedTracker.setData(true, this.graph.getMC());
            return true;
        }
        Iterator<NodeData> it = this.graph.nodeIter();
        while (it.hasNext()) {
            NodeData n = it.next();
            n.setTag(0);
        }
        it = this.graph.nodeIter();
        NodeData v = it.next();
        DFS(v, this.graph);
        it = this.graph.nodeIter();
        while (it.hasNext()) {
            NodeData n = it.next();
            if (n.getTag() == 0) {
                this.isConnectedTracker.setData(false, this.graph.getMC());
                return false;
            }
            n.setTag(0);
        }
        DirectedWeightedGraph transposed = this.transpose();
        it = transposed.nodeIter();
        v = it.next();
        DFS(v, transposed);
        it = transposed.nodeIter();
        while (it.hasNext()) {
            NodeData n = it.next();
            if (n.getTag() == 0) {
                this.isConnectedTracker.setData(false, this.graph.getMC());
                return false;
            }
        }
        this.isConnectedTracker.setData(true, this.graph.getMC());
        return true;
    }
    
    private void DFS(NodeData v, DirectedWeightedGraph g) {
        Stack<NodeData> S = new Stack<>();
        S.push(v);
        while (!S.isEmpty()) {
            NodeData w = S.pop();
            int key = w.getKey();
            if (w.getTag() == 0) {
                w.setTag(1);
                Iterator<EdgeData> it = g.edgeIter(key);
                if (it == null) continue;
                while (it.hasNext()) {
                    EdgeData e = it.next();
                    NodeData u = g.getNode(e.getDest());
                    if (u.getTag() == 0) {
                        S.push(u);
                    }
                }
            }
        }
    }
    
    private DirectedWeightedGraph transpose() {
        DirectedWeightedGraph t = new DWGraph();
        Iterator<NodeData> it = this.graph.nodeIter();
        while (it.hasNext()) {
            NodeData n = it.next();
            t.addNode(n);
        }
        Iterator<EdgeData> it2 = this.graph.edgeIter();
        while (it2.hasNext()) {
            EdgeData e = it2.next();
            t.connect(e.getDest(), e.getSrc(), e.getWeight());
        }
        return t;
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
        return this.Dijkstra(src).getOrDefault(dest, -1.0);
    }
    
    /**
     * Computes the shortest path between src to dest - as an ordered List of nodes:
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
        List<NodeData> path = new ArrayList<>();
        if (src == dest) {
            path.add(this.graph.getNode(src));
            return path;
        }
        path = this.DijkstraPaths(src).get(dest).stream().map(i -> this.graph.getNode(i)).collect(Collectors.toList());
        if (path.size() == 1) {
            return null;
        }
        return path;
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
     * @param src The source node id
     * @return Map from node id to distance of the shortest path
     */
    public HashMap<Integer, Double> Dijkstra(int src) {
//        if (!this.shortestDistanceTracker.wasChanged(this.graph.getMC())) {
//            return this.shortestDistanceTracker.getData();
//        }
        HashMap<Integer, Double> distances = new HashMap<>();
        Iterator<NodeData> it = this.graph.nodeIter();
        while (it.hasNext()) {
            NodeData n = it.next();
            distances.put(n.getKey(), Double.MAX_VALUE);
        }
        PriorityQueue<NodeData> pq = new PriorityQueue<NodeData>(this.graph.nodeSize());
        pq.add(new Node(src, 0.0));
        distances.put(src, 0.0);
        HashSet<Integer> settled = new HashSet<>();
        
        while (settled.size() != this.graph.nodeSize()) {
            if (pq.isEmpty()) {
//                this.shortestDistanceTracker.setData(distances, this.graph.getMC());
                return distances;
            }
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
                    edgeDistance = e.getWeight();
                    newDistance = distances.get(u) + edgeDistance;
                    
                    // If new distance is cheaper in cost
                    if (newDistance < distances.get(v.getKey()))
                        distances.put(v.getKey(), newDistance);
                    
                    // Add the current node to the queue
                    pq.add(new Node(v.getKey(), distances.get(v.getKey())));
                }
            }
        }
//        this.shortestDistanceTracker.setData(distances, this.graph.getMC());
        return distances;
    }
    
    /**
     * This function calculate the shortest path to every node that start from src node and return the index
     *
     * @param src The source node id
     * @return Map from node id to distance of the shortest path
     */
    public HashMap<Integer, LinkedList<Integer>> DijkstraPaths(int src) {
//        if (!this.shortestPathTracker.wasChanged(this.graph.getMC())) {
//            return this.shortestPathTracker.getData();
//        }
        HashMap<Integer, LinkedList<Integer>> paths = new HashMap<>();
        
        HashMap<Integer, Integer> parentNode = new HashMap<>();
        HashMap<Integer, Double> distances = new HashMap<>();
        Iterator<NodeData> it = this.graph.nodeIter();
        while (it.hasNext()) {
            NodeData n = it.next();
            distances.put(n.getKey(), Double.MAX_VALUE);
        }
        PriorityQueue<NodeData> pq = new PriorityQueue<NodeData>(this.graph.nodeSize());
        pq.add(new Node(src, 0.0));
        distances.put(src, 0.0);
        parentNode.put(src, -1);
        HashSet<Integer> settled = new HashSet<>();
        
        while (settled.size() != this.graph.nodeSize()) {
            if (pq.isEmpty()) {
                paths = this.getPath(parentNode, src);
//                this.dijkstraTracker.setData(distances, this.graph.getMC());
//                return distances;
                return paths;
            }
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
                    edgeDistance = e.getWeight();
                    newDistance = distances.get(u) + edgeDistance;
                    
                    // If new distance is cheaper in cost
                    if (newDistance < distances.get(v.getKey())) {
                        parentNode.put(v.getKey(), u);
                        distances.put(v.getKey(), newDistance);
                    }
                    
                    // Add the current node to the queue
                    pq.add(new Node(v.getKey(), distances.get(v.getKey())));
                }
            }
        }
        paths = this.getPath(parentNode, src);
//        this.shortestDistanceTracker.setData(distances, this.graph.getMC());
        return paths;
    }
    
    private HashMap<Integer, LinkedList<Integer>> getPath(HashMap<Integer, Integer> parentNode, int src) {
        HashMap<Integer, LinkedList<Integer>> path = new HashMap<>();
        Iterator<NodeData> it = this.graph.nodeIter();
        while (it.hasNext()) {
            NodeData n = it.next();
            LinkedList<Integer> pathList = new LinkedList<>();
            int curr = n.getKey();
            while (curr != src && parentNode.get(curr) != null) {
                pathList.add(curr);
                curr = parentNode.get(curr);
            }
            pathList.add(src);
            Collections.reverse(pathList);
            path.put(n.getKey(), pathList);
            
        }
        return path;
    }
}
