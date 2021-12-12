package datastructures;

import api.EdgeData;
import api.GeoLocation;

import java.util.Objects;

public class Edge implements EdgeData, Comparable<Edge> {
    
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;
    
    //region Constructors
    
    /**
     * @param src The key (id) of the source node of the edge
     * @param dest The key (id) of the destination node of the edge
     * @param weight The weight of the edge (Note: the weight must be positive)
     * @param info The remark (metadata) associated with this edge
     * @param tag Temporal data (aka color: e,g, white, gray, black)
     */
    public Edge(int src, int dest, double weight, String info, int tag) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.info = info;
        this.tag = tag;
    }
    
    /**
     * @param src The key (id) of the source node of the edge
     * @param dest The key (id) of the destination node of the edge
     * @param weight The weight of the edge (Note: the weight must be positive)
     */
    public Edge(int src, int dest, double weight) {
        this(src, dest, weight, "", 0);
    }
    
    /**
     * @param src The key (id) of the source node of the edge
     * @param dest The key (id) of the destination node of the edge
     */
    public Edge(int src, int dest) {
        this(src, dest, 1.0, "", 0);
    }
    
    /**
     * Copy constructor
     *
     * @param other EdgeData object to copy
     */
    public Edge(EdgeData other) {
        this(other.getSrc(), other.getDest(), other.getWeight(), other.getInfo(), other.getTag());
    }
    
    //endregion
    
    //region Getters and Setters
    
    /**
     * @return The key (id) of the source node of the edge
     */
    @Override
    public int getSrc() {
        return this.src;
    }
    
    /**
     * @param src The new key (id) of the source node of the edge
     * @return The modified EdgeData instance. (allows chaining)
     */
    public EdgeData setSrc(int src) {
        this.src = src;
        return this;
    }
    
    /**
     * @return The key (id) of the destination node of the edge
     */
    @Override
    public int getDest() {
        return this.dest;
    }
    
    /**
     * @param dest The new key (id) of the destination node of the edge
     * @return The modified EdgeData instance. (allows chaining)
     */
    public EdgeData setDest(int dest) {
        this.dest = dest;
        return this;
    }
    
    /**
     * @return The weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return this.weight;
    }
    
    /**
     * @param weight The new weight of this edge (positive value).
     * @return The modified EdgeData instance. (allows chaining)
     */
    public EdgeData setWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        this.weight = weight;
        return this;
    }
    
    /**
     * @return Yhe remark (metadata) associated with this edge.
     */
    @Override
    public String getInfo() {
        return this.info;
    }
    
    /**
     * @param s The new remark (metadata) associated with this edge.
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }
    
    /**
     * @return Temporal data (tag)
     */
    @Override
    public int getTag() {
        return this.tag;
    }
    
    /**
     * @param t The new value of the temporal data (tag)
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }
    
    //endregion
    
    @Override
    public String toString() {
        return this.src + "--" + this.weight + "->" + this.dest;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        EdgeData e = (EdgeData)o;
        return this.src == e.getSrc() &&
                this.dest == e.getDest() &&
                Double.compare(this.weight, e.getWeight()) == 0 &&
                this.info.equals(e.getInfo()) &&
                this.tag == e.getTag();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.src, this.dest, this.weight, this.info, this.tag);
    }
    
    @Override
    public int compareTo(Edge o) {
        return Double.compare(this.getWeight(), o.getWeight());
    }
}
