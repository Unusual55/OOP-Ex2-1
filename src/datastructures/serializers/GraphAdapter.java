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
    
    public List<EdgeData> getEdges() {
        return this.Edges;
    }
    
    public void setEdges(List<EdgeData> edges) {
        this.Edges = edges;
    }
    
    public List<NodeData> getNodes() {
        return this.Nodes;
    }
    
    public void addEdge(EdgeData edge) {
        this.Edges.add(edge);
    }
    
    public void addNode(NodeData node) {
        this.Nodes.add(node);
    }
    
    public void setNodes(List<NodeData> nodes) {
        this.Nodes = nodes;
    }
    
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
