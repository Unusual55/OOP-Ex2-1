package datastructures.serializers;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import datastructures.DWGraph;
import datastructures.Node;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class use both EdgeAdapter and NodeAdapter in order to load and save the graph to .json file
 */
public class GraphAdapter {
    @SerializedName("Edges")
    @Expose
    public List<EdgeData> Edges;
    @SerializedName("Nodes")
    @Expose
    public List<NodeData> Nodes;
    
    public GraphAdapter() {
        this.Edges = new ArrayList<>();
        this.Nodes = new ArrayList<>();
    }
    
    public GraphAdapter(List<EdgeData> edges, List<NodeData> nodes) {
        this.Edges = edges;
        this.Nodes = nodes;
    }

    /**
     * This function return the edges of the graph
     * @return
     */
    public List<EdgeData> getEdges() {
        return this.Edges;
    }

    /**
     * This function set the edges of the graph
     * @param edges
     */
    public void setEdges(List<EdgeData> edges) {
        this.Edges = edges;
    }

    /**
     * This function return the nodes of the graph
     * @return
     */
    public List<NodeData> getNodes() {
        return this.Nodes;
    }

    /**
     * This function add a new edge to the graph
     * @param edge
     */
    public void addEdge(EdgeData edge) {
        this.Edges.add(edge);
    }

    /**
     * This function add a new node to the graph
     * @param node
     */
    public void addNode(NodeData node) {
        this.Nodes.add(node);
    }

    /**
     * This function set the nodes of the graph
     * @param nodes
     */
    public void setNodes(List<NodeData> nodes) {
        this.Nodes = nodes;
    }

    /**
     * This function turn the input graph into an GraphAdapter object in order to serialize it.
     * @param graph
     * @return
     */
    public static GraphAdapter fromGraph(DirectedWeightedGraph graph) {
        GraphAdapter adapter = new GraphAdapter();
        Iterator<EdgeData> edgeIterator = graph.edgeIter();
        while (edgeIterator.hasNext()) {
            EdgeData edge = edgeIterator.next();
            adapter.addEdge(edge);
        }
        Iterator<NodeData> nodeIterator = graph.nodeIter();
        while (nodeIterator.hasNext()) {
            NodeData node = nodeIterator.next();
            adapter.addNode(node);
        }
        return adapter;
    }

    /**
     * This function get an GraphAdapter and turn it into an object that implement the
     * DirectedWeightedGraph interface
     * @param adapter
     * @return
     */
    public static DirectedWeightedGraph toGraph(GraphAdapter adapter) {
        DirectedWeightedGraph graph = new DWGraph();
        for (NodeData node : adapter.getNodes()) {
            graph.addNode(node);
        }
        for (EdgeData edge : adapter.getEdges()) {
            graph.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
        }
        
        return graph;
    }
}
