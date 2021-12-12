package datastructures;

import api.GeoLocation;
import api.NodeData;

import java.util.Comparator;
import java.util.Objects;

public class Node implements NodeData, Comparable<Node> {
    
    private int key;
    private double weight;
    private GeoLocation location;
    private String info;
    private int tag;
    
    //region Constructors
    
    /**
     * @param key      The key (id) of the node
     * @param weight   The weight of the edge
     * @param location The location of the node
     * @param info     The remark (metadata) associated with this edge
     * @param tag      Temporal data (aka color: e,g, white, gray, black)
     */
    public Node(int key, double weight, GeoLocation location, String info, int tag) {
        this.key = key;
        this.weight = weight;
        this.location = location;
        this.info = info;
        this.tag = tag;
    }
    
    /**
     * @param key      The key (id) of the node
     * @param weight   The weight of the edge
     * @param location The location of the node
     * @param info     The remark (metadata) associated with this edge
     */
    public Node(int key, double weight, GeoLocation location, String info) {
        this(key, weight, location, info, 0);
    }
    
    /**
     * @param key      The key (id) of the node
     * @param weight   The weight of the edge
     * @param location The location of the node
     */
    public Node(int key, double weight, GeoLocation location) {
        this(key, weight, location, "", 0);
    }
    
    /**
     * @param key      The key (id) of the node
     * @param location The location of the node
     */
    public Node(int key, GeoLocation location) {
        this(key, 0.0, location, "", 0);
    }
    
    /**
     * @param key    The key (id) of the node
     * @param weight The weight of the edge
     */
    public Node(int key, double weight) {
        this(key, weight, new Point3D(), "", 0);
    }
    
    /**
     * @param key The key (id) of the node
     */
    public Node(int key) {
        this(key, 0.0, new Point3D(), "", 0);
    }
    
    /**
     * Copy constructor
     *
     * @param other NodeData object to copy
     */
    public Node(NodeData other) {
        this(other.getKey(), other.getWeight(), new Point3D(other.getLocation()), other.getInfo(), other.getTag());
    }
    
    //endregion
    
    //region Getters and Setters
    
    /**
     * @return The key (id) associated with this node.
     */
    @Override
    public int getKey() {
        return this.key;
    }
    
    /**
     * @param key - new key (id) of this node.
     * @return The modified NodeData instance. (allows chaining)
     */
    public NodeData setKey(int key) {
        this.key = key;
        return this;
    }
    
    /**
     * @return the location of this node, if none return null.
     */
    @Override
    public GeoLocation getLocation() {
        return this.location;
    }
    
    /**
     * Allows changing this node's location.
     *
     * @param p - new location  (position) of this node.
     */
    @Override
    public void setLocation(GeoLocation p) {
        this.location = new Point3D(p);
    }
    
    /**
     * @return The weight associated with this node.
     */
    @Override
    public double getWeight() {
        return this.weight;
    }
    
    /**
     * Allows changing this node's weight.
     *
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        this.weight = w;
    }
    
    /**
     * @return The remark (metadata) associated with this node.
     */
    @Override
    public String getInfo() {
        return this.info;
    }
    
    /**
     * Allows changing the remark (metadata) associated with this node.
     *
     * @param s - the new remark (metadata)
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }
    
    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     *
     * @return The tag (temporal data aka color: e,g, white, gray, black) associated with this node.
     */
    @Override
    public int getTag() {
        return this.tag;
    }
    
    /**
     * Allows setting the "tag" value for temporal marking a node - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }
    
    //endregion
    
    @Override
    public String toString() {
        return "(" + this.key + ")";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        NodeData n = (NodeData)o;
        return this.key == n.getKey() &&
                Double.compare(this.weight, n.getWeight()) == 0 &&
                this.location.equals(n.getLocation()) &&
                this.info.equals(n.getInfo()) &&
                this.tag == n.getTag();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.weight, this.location, this.info, this.tag);
    }
    
    
    @Override
    public int compareTo(Node o) {
        return Double.compare(this.getWeight(), o.getWeight());
    }
}
