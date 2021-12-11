package GUI;

import api.DirectedWeightedGraph;
import api.GeoLocation;
import api.NodeData;
import datastructures.DWGraph;
import datastructures.Node;
import datastructures.Point3D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Graph extends DWGraph {
    private Canvas canvas;
    private HashMap<Integer, NodeData> vertices;
    private HashSet<Integer> keys = new HashSet<>();
    private int lastUniqueKey = 0;
    private double marginLeft = 0.0, marginRight = 0.0, marginTop = 0.0, marginBottom = 0.0;
    private double width = 0.0, height = 0.0;
    private double minX, minY, maxX, maxY;
    private double nodeWidth = 1.0, nodeHeight = 1.0;
    private Color nodeColor = new Color(255, 0, 0);
    private Color nodeBorderColor = new Color(0, 255, 0);
    
    public Graph(Canvas canvas) {
        super();
        this.canvas = canvas;
        this.vertices = new HashMap<>();
        this.minX = Double.MAX_VALUE;
        this.minY = Double.MAX_VALUE;
        this.maxX = Double.MIN_VALUE;
        this.maxY = Double.MIN_VALUE;
    }
    
    public Graph(DirectedWeightedGraph other, Canvas canvas) {
        super(other);
        this.vertices = new HashMap<>();
        Iterator<NodeData> it = other.nodeIter();
        while (it.hasNext()) {
            NodeData nd = it.next();
            if (this.addVertex(nd, false)) {
                this.keys.add(nd.getKey());
            }
        }
        this.reScale();
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
    
    public Graph setGraphDimensions(double width, double height) {
        return this.setWidth(width).setHeight(height);
    }
    
    public Graph setMarginLeft(double marginLeft) {
        this.marginLeft = marginLeft;
        return this;
    }
    
    public Graph setMarginRight(double marginRight) {
        this.marginRight = marginRight;
        return this;
    }
    
    public Graph setMarginTop(double marginTop) {
        this.marginTop = marginTop;
        return this;
    }
    
    public Graph setMarginBottom(double marginBottom) {
        this.marginBottom = marginBottom;
        return this;
    }
    
    public Graph setMargins(double marginLeft, double marginRight, double marginTop, double marginBottom) {
        return this.setMarginLeft(marginLeft).setMarginRight(marginRight).setMarginTop(marginTop).setMarginBottom(marginBottom);
    }
    
    public Graph setMargins(double marginLR, double marginTB) {
        return this.setMargins(marginLR, marginLR, marginTB, marginTB);
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
        this.keys.add(key);
        final double x = this.getNode(key).getLocation().x();
        final double y = this.getNode(key).getLocation().y();
        
        this.vertices.put(key, nd);
        
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
                this.reScale();
            }
        }
        
        return true;
    }
    
    //endregion
    
    public void reScale() {
        Iterator<NodeData> it = super.nodeIter();
        while (it.hasNext()) {
            NodeData nd = it.next();
            int key = nd.getKey();
            NodeData n = this.vertices.get(key);
            GeoLocation loc = new Point3D(
                    Graph.map(nd.getLocation().x(), this.minX, this.maxX, this.marginLeft, this.width - this.marginRight),
                    Graph.map(nd.getLocation().y(), this.minY, this.maxY, this.marginTop, this.height - this.marginBottom)
            );
            n.setLocation(loc);
        }
    }
    
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
//        System.out.println(this.nodeSize());
        Iterator<NodeData> it = super.nodeIter();
        while (it.hasNext()) {
            NodeData nd = it.next();
            int key = nd.getKey();
            NodeData n = this.vertices.get(key);
            GeoLocation loc = n.getLocation();
            
            Point p = this.canvas.WorldToScreen(loc.x(), loc.y());
            
            Ellipse2D.Double node = new Ellipse2D.Double(p.getX()  - this.nodeWidth / 2, p.getY()  - this.nodeHeight / 2, this.nodeWidth, this.nodeHeight);
//            System.out.println(node.x + " " + node.y + " " + node.width + " " + node.height);
            g2.setColor(this.nodeColor);
            g2.fill(node);
            g2.setColor(this.nodeBorderColor);
            g2.draw(node);
//            g2.fillOval((int)loc.x() - (int)(this.nodeWidth / 2), (int)loc.y() - (int)(this.nodeHeight / 2), (int)(this.nodeWidth), (int)(this.nodeHeight));
            
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
