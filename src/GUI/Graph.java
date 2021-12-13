package GUI;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.GeoLocation;
import api.NodeData;
import datastructures.DWGraph;
import datastructures.Node;
import datastructures.Point3D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Graph extends DWGraph implements DirectedWeightedGraph {
    private Canvas canvas;
    private HashSet<Integer> keys = new HashSet<>();
    private int lastUniqueKey = 0;
    private double width = 0.0, height = 0.0;
    private double minX, minY, maxX, maxY;
    private double nodeWidth = 1.0, nodeHeight = 1.0;
    private Color nodeColor = new Color(255, 0, 0);
    private Color nodeBorderColor = new Color(0, 255, 0);
    private Color chosenNodeColor = new Color(255, 0, 255);
    private Color chosenNodeBorderColor = new Color(0, 255, 0);
    
    public Graph(Canvas canvas) {
        super();
        this.canvas = canvas;
        this.minX = Double.MAX_VALUE;
        this.minY = Double.MAX_VALUE;
        this.maxX = Double.MIN_VALUE;
        this.maxY = Double.MIN_VALUE;
    }
    
    public Graph(DirectedWeightedGraph other, Canvas canvas) {
        super(other);
        this.canvas = canvas;
        Iterator<NodeData> nodeIt = other.nodeIter();
        while (nodeIt.hasNext()) {
            NodeData nd = nodeIt.next();
            this.addNode(nd);
            if (this.addVertex(nd, false)) {
                this.keys.add(nd.getKey());
            }
        }
        Iterator<EdgeData> edgeIt = other.edgeIter();
        while (edgeIt.hasNext()) {
            EdgeData ed = edgeIt.next();
            this.connect(ed.getSrc(), ed.getDest(), ed.getWeight());
        }
        
//        this.reScale();
    }
    
    //region Getters and Setters
    
    public double getNodeWidth() {
        return this.nodeWidth;
    }
    
    public double getNodeHeight() {
        return this.nodeHeight;
    }
    
    public Graph setNodeWidth(double nodeWidth) {
        this.nodeWidth = nodeWidth;
        return this;
    }
    
    public Graph setNodeHeight(double nodeHeight) {
        this.nodeHeight = nodeHeight;
        return this;
    }
    
    public Graph setNodeDimensions(double nodeWidth, double nodeHeight) {
        return this.setNodeWidth(nodeWidth).setNodeHeight(nodeHeight);
    }
    
    public Graph setNodeDimensions(double diameter) {
        return this.setNodeDimensions(diameter, diameter);
    }
    
    public Graph setWidth(double width) {
        this.width = width;
        return this;
    }
    
    public Graph setHeight(double height) {
        this.height = height;
        return this;
    }
    
    
    public double getWidth() {
        return this.width;
    }
    
    public double getHeight() {
        return this.height;
    }
    
    public Graph setGraphDimensions(double width, double height) {
        return this.setWidth(width).setHeight(height);
    }
    
    
    public double getMinX() {
        return this.minX;
    }
    
    public double getMinY() {
        return this.minY;
    }
    
    public double getMaxX() {
        return this.maxX;
    }
    
    public double getMaxY() {
        return this.maxY;
    }
    
    //endregion
    
    //region Add Vertex
    
    public boolean addVertex(double x, double y) {
        while (this.keys.contains(this.lastUniqueKey)) {
            this.lastUniqueKey++;
        }
        boolean didAdd = this.addVertex(new Node(this.lastUniqueKey, new Point3D(x, y)));
//        System.out.println(Arrays.toString(this.keys.stream().map((key) -> this.getNode(key).getLocation().toString()).toArray()));
        return didAdd;
    }
    
    public boolean addVertex(double x, double y, int key) {
        return this.addVertex(new Node(key, new Point3D(x, y)));
    }
    
    public boolean addVertex(NodeData nd) {
        return this.addVertex(nd, false);
    }
    
    private boolean addVertex(NodeData n, boolean withReScaling) {
        int mc = this.getMC();
        int key = n.getKey();
        NodeData nd = new Node(n);
        if (super.getNode(key) == null) {
            super.addNode(nd);
        }
        if (mc == this.getMC())
            return false;
        final double x = this.getNode(key).getLocation().x();
        final double y = this.getNode(key).getLocation().y();
        
        this.keys.add(key);
        
        if (x < this.minX || y < this.minY || x > this.maxX || y > this.maxY) {
            if (x < this.minX) {
                this.minX = x;
            } else if (x > this.maxX) {
                this.maxX = x;
            }
            
            if (y < this.minY) {
                this.minY = y;
            } else if (y > this.maxY) {
                this.maxY = y;
            }
            
            if (withReScaling) {
//                this.reScale();
//                this.reScale();
            }
        }
        
        return true;
    }
    
    //endregion
    
//    public void reScale(double marginLeft, double marginRight, double marginTop, double marginBottom) {
//        Iterator<NodeData> it = super.nodeIter();
//        while (it.hasNext()) {
//            NodeData nd = it.next();
//            int key = nd.getKey();
//            GeoLocation loc = new Point3D(
//                    Graph.map(nd.getLocation().x(), this.minX, this.maxX, marginLeft, this.width - marginRight),
//                    Graph.map(nd.getLocation().y(), this.minY, this.maxY, marginTop, this.height - marginBottom)
//            );
////            this.vertices.get(key).setLocation(loc);
//        }
//    }
    
    public void draw(Graphics g, NodeData chosenNode, double fWorldLeft, double fWorldTop, double fWorldRight, double fWorldBottom) {
        Graphics2D g2 = (Graphics2D)g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//        System.out.println(this.nodeSize());
        Iterator<NodeData> it = super.nodeIter();
        while (it.hasNext()) {
            NodeData nd = it.next();
            int key = nd.getKey();
            GeoLocation loc = nd.getLocation();
            
            Point p = this.canvas.WorldToScreen(loc.x(), loc.y());
            double a = this.nodeWidth / 2;
            double b = this.nodeHeight / 2;
            double x = p.getX() - a;
            double y = p.getY() - b;
            Ellipse2D.Double node = new Ellipse2D.Double(x, y, this.nodeWidth, this.nodeHeight);
//            System.out.println(node.getX());
            if (this.nodeWidth < 4 || this.nodeHeight < 4 || node.getX() + node.getWidth() < 0 || node.getX() > this.width || node.getY() + node.getHeight() < 0 || node.getY() > this.height) {
                if (chosenNode != null && chosenNode.getKey() == key) {
                    chosenNode = null;
                }
                continue;
            }

//            if (chosenNode != null && chosenNode.getKey() == key) {
//                g2.setColor(Color.MAGENTA);
//            } else {
//                g2.setColor(this.nodeColor);
//            }
            g2.setColor(this.nodeColor);
            g2.fill(node);
            g2.setColor(this.nodeBorderColor);
            g2.draw(node);
        }
        if (chosenNode != null) {
            GeoLocation loc = chosenNode.getLocation();
            Point p = this.canvas.WorldToScreen(loc.x(), loc.y());
            double a = this.nodeWidth / 2;
            double b = this.nodeHeight / 2;
            double x = p.getX() - a;
            double y = p.getY() - b;
            Ellipse2D.Double node = new Ellipse2D.Double(x, y, this.nodeWidth, this.nodeHeight);
            g2.setColor(this.chosenNodeColor);
            g2.fill(node);
            g2.setColor(this.chosenNodeBorderColor);
            g2.draw(node);
        }
    }
    
    public static double map(double input, double input_start, double input_end, double output_start, double output_end) {
        double slope = (output_end - output_start) / (input_end - input_start);
        return output_start + slope * (input - input_start);
    }
    
    /**
     * Draw an arrow line between two points.
     * Credit: https://stackoverflow.com/a/3094933/13238827
     *
     * @param g  the graphics component.
     * @param x1 x-position of the first point.
     * @param y1 y-position of the first point.
     * @param x2 x-position of the second point.
     * @param y2 y-position of the second point.
     * @param b  the length of the base of the arrow.
     * @param h  the length of the height of the arrow.
     */
    public static void drawArrowHead(Graphics g, double x1, double y1, double x2, double y2, double b, double h) {
        final Graphics2D g2d = (Graphics2D)g;
        
        AffineTransform tx = new AffineTransform();
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(0, (int)(h / 2));
        arrowHead.addPoint((int)(-b / 2), (int)(-h / 2));
        arrowHead.addPoint((int)(b / 2), (int)(-h / 2));
        
        tx.setToIdentity();
        double angle = Math.atan2(y2 - y1, x2 - x1);
        tx.translate(x2, y2);
        tx.rotate((angle - Math.PI / 2d));
        
        Graphics2D G = (Graphics2D)g2d.create();
        G.setTransform(tx);
        G.fill(arrowHead);
        G.dispose();
    }
}
