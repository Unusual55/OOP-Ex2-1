package DataStructures;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
     *
     * @return Transposed Graph
     */
    private DirectedWeightedGraph GraphTranspose() {
        DirectedWeightedGraph g = new DWGraph();
        Iterator<NodeData> nodeiter = this.graph.nodeIter();
        while (nodeiter.hasNext()) {
            g.addNode(nodeiter.next());
        }
        Iterator<EdgeData> edgeiter = this.graph.edgeIter();
        while (edgeiter.hasNext()) {
            EdgeData e = edgeiter.next();
            g.connect(e.getDest(), e.getSrc(), e.getWeight());
        }
        return g;
    }

    private void Dijkstra(int src) {
        this.Dijkstra(new HashMap<>(), src);
    }

    /**
     * This function calculate the shortest path to every node that start from src node and return the index
     *
     * @param distance Empty HashMap that will contain the distances
     * @param src      The source node id
     */
    private void Dijkstra(HashMap<Integer, Double> distance, int src) {
        Iterator<NodeData> it = this.graph.nodeIter();
        while (it.hasNext()) {
            NodeData n = it.next();
            distance.put(n.getKey(), Double.MAX_VALUE);
        }
        PriorityQueue<NodeData> pq = new PriorityQueue<NodeData>(this.graph.nodeSize());
        pq.add(new Node(src, new Point3D(), 0.0, "", -1));
        distance.put(src, 0.0);
        HashSet<Integer> settled = new HashSet<Integer>();

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
                    newDistance = distance.get(u) + edgeDistance;

                    // If new distance is cheaper in cost
                    if (newDistance < distance.get(v.getKey()))
                        distance.put(v.getKey(), newDistance);

                    // Add the current node to the queue
                    pq.add(new Node(v.getKey(), new Point3D(), distance.get(v.getKey()), "", -1));
                }
            }
        }
    }


//        int minid = -1, unvisited = this.graph.nodeSize();
//        double mindist = Double.MAX_VALUE;
//        NodeData curr = this.graph.getNode(src);
//        distance.put(src, 0.0);
//        while (unvisited > 0) {
//            Iterator<EdgeData> adj = this.graph.edgeIter();
//            int nextid = -1;
//            while (adj.hasNext()) {
//                EdgeData e = adj.next();
//                NodeData neighbor = this.graph.getNode(e.getDest());
//                if (neighbor.getTag() == 1) {
//                    continue;
//                }
//                int id = neighbor.getKey();
//                neighbor.setTag(0);
//                if (minid == -1) {
//                    minid = id;
//                }
//                if (nextid == -1) {
//                    nextid = id;
//                }
//                if (!distance.containsKey(id)) {
//                    distance.put(id, Double.MAX_VALUE);
//                }
//                if (distance.get(id) > distance.get(src) + e.getWeight()) {
//                    distance.replace(id, distance.get(id), distance.get(src) + e.getWeight());
//                    if (distance.get(nextid) > distance.get(id)) {
//                        nextid = id;
//                    }
//                }
//            }
//            if (distance.get(minid) > distance.get(nextid)) {
//                minid = nextid;
//                mindist = distance.get(minid);
//            }
//            unvisited--;
//            curr.setTag(1);
//            curr = this.graph.getNode(nextid);
//        }
//    }

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
        HashMap<Integer, Double> dist = new HashMap<>();
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
        HashMap<Integer, Double> dist = new HashMap<>();
        this.Dijkstra(dist, src);
        DirectedWeightedGraph transposed = this.GraphTranspose();
        int curr = dest;
        Stack<NodeData> path = new Stack<NodeData>();
        while (curr != src) {
            int nextid = -1;
            Iterator<EdgeData> edgeiter = transposed.edgeIter(curr);
            while (edgeiter.hasNext()) {
                EdgeData e = edgeiter.next();
                if (nextid == -1) {
                    nextid = e.getSrc();
                }
                if (dist.get(e.getSrc()) + e.getWeight() == dist.get(curr)) {
                    nextid = transposed.getNode(e.getSrc()).getKey();
                    break;
                }
            }
            path.push(this.graph.getNode(curr));
            curr = nextid;
        }
        List<NodeData> ret = new LinkedList<NodeData>();
        for (NodeData n : path) {
            ret.add(0, n);
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
        if (!isConnected()) {
            return null;
        }
        int minid = -1;
        double mindistance = Double.MAX_VALUE;
        Iterator<NodeData> nodes = graph.nodeIter();
        while (nodes.hasNext()) {
            Node n = (Node) nodes.next();
            int id = n.getKey();
            double longest = this.MaxShortestPath(id);
            if (mindistance > longest) {
                mindistance = longest;
                minid = id;
            }
            /**
             * Right now we made few changes in the graph without change it through the iterator, which we can't do
             * To fix the issue, the next line will reset the tags of the nodes to their default value -1, so the
             * Node Iterator will be valid again
             */
            graph.nodeIter().forEachRemaining(nodeData -> nodeData.setTag(-1));
        }
        return graph.getNode(minid);
    }

    /**
     * This function is use dijkstra Algorithm with modification: we can't visit any vertex in without set.
     *
     * @param distance Empty HashMap that will contain the distances from the other nodes
     * @param without  Set of ids of nodes we don't want to visit.
     */
    private void ModifiedDijkstra(HashMap<Integer, Double> distance, HashSet<Integer> without, int src) {
        int minid = -1, unvisited = this.graph.nodeSize() - without.size();
        double mindist = Double.MAX_VALUE;
        NodeData curr = this.graph.getNode(src);
        distance.put(src, 0.0);
        while (unvisited > 0) {
            Iterator<EdgeData> adj = this.graph.edgeIter();
            int nextid = -1;
            while (adj.hasNext()) {
                EdgeData e = adj.next();
                NodeData neighbor = this.graph.getNode(e.getDest());
                if (neighbor.getTag() == 1 || without.contains(neighbor.getKey())) {
                    continue;
                }
                int id = neighbor.getKey();
                neighbor.setTag(0);
                if (minid == -1) {
                    minid = id;
                }
                if (nextid == -1) {
                    nextid = id;
                }
                if (!distance.containsKey(id)) {
                    distance.put(id, Double.MAX_VALUE);
                }
                if (distance.get(id) > distance.get(src) + e.getWeight()) {
                    distance.replace(id, distance.get(id), distance.get(src) + e.getWeight());
                    if (distance.get(nextid) > distance.get(id)) {
                        nextid = id;
                    }
                }
            }
            if (distance.get(minid) > distance.get(nextid)) {
                minid = nextid;
                mindist = distance.get(minid);
            }
            unvisited--;
            curr.setTag(1);
            curr = this.graph.getNode(nextid);
        }
    }
//
//    private List<NodeData> ShorterPath(HashMap<Integer, Double> dist, int dest, int src) {
//        DirectedWeightedGraph transposed=this.GraphTranspose();
//        int curr=dest;
//        Stack<NodeData> path=new Stack<NodeData>();
//        while(curr!=src){
//            int nextid=-1;
//            Iterator<EdgeData> edgeiter=transposed.edgeIter(curr);
//            while(edgeiter.hasNext()){
//                EdgeData e=edgeiter.next();
//                if(nextid==-1){
//                    nextid=e.getSrc();
//                }
//                if(dist.get(e.getSrc())+e.getWeight()==dist.get(curr)){
//                    nextid=transposed.getNode(e.getSrc()).getKey();
//                    break;
//                }
//            }
//            path.push(this.graph.getNode(curr));
//            curr=nextid;
//        }
//        List<NodeData> ret=new LinkedList<NodeData>();
//        for (NodeData n:path) {
//            ret.add(0,n);
//        }
//        return ret;
//    }
//
//    private int MarkedNodes(HashMap<Integer, Double> dist, HashSet<Integer> needed, int src, HashSet<Integer> outset){
//        int included=0;
//        int retid=src;
//        for (int i:needed) {
//            Iterator<Integer> iditer=needed.iterator();
//            while(iditer.hasNext()){
//                int id=iditer.next();
//                LinkedList<NodeData> path= (LinkedList<NodeData>) this.ShorterPath(dist, id, src);
//                int currentcounter=0;
//                HashSet<Integer> already=new HashSet<Integer>();
//                for (int j = 0; j < path.size(); j++) {
//                    if(needed.contains(path.get(i).getKey())){
//                        already.add(path.get(i).getKey());
//                        currentcounter++;
//                    }
//                }
//                if(currentcounter>included){
//                    outset.clear();
//                    outset.addAll(already);
//                    included=currentcounter;
//                    retid=id;
//                }
//            }
//        }
//        return retid;
//    }
//
//    private void SingleSourceTsp

    /**
     * This function get an id of the source vertex which we will use to find the longest path out of all of the shortest
     * paths that start from this node
     *
     * @param src
     * @return The distance to this node
     */
    private double MaxShortestPath(int src) {
        HashMap<Integer, Double> dist = new HashMap<>();
        this.Dijkstra(dist, src);
        double maxdistance = 0;
        for (Double distance : dist.values()) {
            maxdistance = Math.max(distance, maxdistance);
        }
        return maxdistance;
    }

    /**
     * This function Calculate the distance of the longest path that start from a node to any
     * other vertex in the graph
     *
     * @param s the source vertex
     * @return The distance of the longest path
     */
    private double LongestPath(NodeData s) {
        int unvisitedcouter = graph.nodeSize();
        DWGraph g = (DWGraph) this.copy();
        Stack<Integer> path = new Stack<Integer>();
        HashMap<Integer, Double> distance = new HashMap<>();
        path.push(s.getKey());
        int maxid = -1;
        while (unvisitedcouter > 0) {
            Iterator<EdgeData> Adj = g.edgeIter(s.getKey());
            int nextid = -1;
            while (Adj.hasNext()) {
                Edge e = (Edge) Adj.next();
                Node n = (Node) g.getNode(e.getDest());
                int id = e.getDest();
                if (maxid == -1) {
                    maxid = id;
                }
                if (!distance.containsKey(id)) {
                    distance.put(id, 0.0);
                }
                if (n.getTag() == 1) {
                    continue;
                }
                n.setTag(0);
                if (distance.get(id) < distance.get(s.getKey()) + e.getWeight()) {
                    distance.replace(id, distance.get(id), distance.get(s.getKey()) + e.getWeight());
                    nextid = id;
                    if (distance.get(maxid) < distance.get(id)) {
                        maxid = id;
                    }
                }
            }
            s.setTag(1);
            if (graph.getNode(nextid).getTag() == 1) {//if the tag of all of the neighbors are 1->return to the previous node and
                if (path.isEmpty() == false) {
                    s = graph.getNode(path.pop());
                }
            } else {//go to the furthest neighbor
                path.push(nextid);
                s = (NodeData) g.getNode(nextid);
                unvisitedcouter--;
            }
        }
        return distance.get(maxid);
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
        JsonParser jsonparser = new JsonParser();
        DirectedWeightedGraph g = new DWGraph();
        try {
            FileReader reader = new FileReader("./" + file);
            Object obj = jsonparser.parse(reader);
            JsonObject job = (JsonObject) obj;
            JsonArray edges = job.get("Edges").getAsJsonArray();
            JsonArray nodes = job.get("Nodes").getAsJsonArray();
            ValidateJson(job);
            for (int i = 0; i < nodes.size(); i++) {
                JsonObject node = nodes.get(i).getAsJsonObject();
                String[] pos = node.get("pos").toString().split(",");
                double x = Double.parseDouble(pos[0].substring(1));
                double y = Double.parseDouble(pos[1].substring(1));
                double z = Double.parseDouble(pos[2].substring(1, pos[2].length() - 1));
                int id = Integer.parseInt(node.get("id").toString());
                Node n = new Node(id, new Point3D(x, y, z), 0, "", -1);
                g.addNode(n);
            }
            for (int i = 0; i < edges.size(); i++) {
                JsonObject edge = edges.get(i).getAsJsonObject();
                int src = Integer.parseInt(edge.get("src").toString());
                int dest = Integer.parseInt(edge.get("dest").toString());
                double weight = Double.parseDouble(edge.get("w").toString());
                g.connect(src, dest, weight);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            this.graph = g;
            return true;
        }
    }

    private void ValidateJson(JsonObject job) {
        if (job.size() != 2) {
            throw new IllegalArgumentException("The valid .json should contain 2 members");
        }
        if (!job.has("Nodes") || !job.has("Edges")) {
            throw new IllegalArgumentException("The valid .json members are supposed to be Edges and Nodes.");
        }
        JsonArray edges = job.get("Edges").getAsJsonArray();
        JsonArray nodes = job.get("Nodes").getAsJsonArray();
        for (int i = 0; i < nodes.size(); i++) {
            JsonObject node = nodes.get(i).getAsJsonObject();
            String[] pos = node.get("pos").toString().split(",");
            if (pos.length != 3) {
                throw new IllegalArgumentException("The valid position should contain 3 parameters");
            }
            try {
                double x = Double.parseDouble(pos[0].substring(1));
                double y = Double.parseDouble(pos[1].substring(1));
                double z = Double.parseDouble(pos[2].substring(1, pos[2].length() - 1));
                int id = Integer.parseInt(node.get("id").toString());
                Node n = new Node(id, new Point3D(x, y, z), 0, "", -1);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("The input is invalid.");
            }
        }
        for (int i = 0; i < edges.size(); i++) {
            JsonObject edge = edges.get(i).getAsJsonObject();
            try {
                int src = Integer.parseInt(edge.get("src").toString());
                int dest = Integer.parseInt(edge.get("dest").toString());
                double weight = Double.parseDouble(edge.get("w").toString());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("The input is invalid.");
            }
        }
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
