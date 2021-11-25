package api;


import java.util.Objects;

public class Edge implements EdgeData {
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;
    
    public Edge(int src, int dest, double weight, String info, int tag) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.info = info;
        this.tag = tag;
    }
    
    public Edge(EdgeData other) {
        this(other.getSrc(), other.getDest(), other.getWeight(), other.getInfo(), other.getTag());
    }
    
    /**
     * The id of the source node of this edge.
     *
     * @return
     */
    @Override
    public int getSrc() {
        return this.src;
    }
    
    /**
     * The id of the destination node of this edge
     *
     * @return
     */
    @Override
    public int getDest() {
        return this.dest;
    }
    
    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return this.weight;
    }
    
    /**
     * Returns the remark (meta data) associated with this edge.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return this.info;
    }
    
    /**
     * Allows changing the remark (meta data) associated with this edge.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }
    
    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     *
     * @return
     */
    @Override
    public int getTag() {
        return this.tag;
    }
    
    /**
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }
    
    @Override
    public String toString() {
        return "" + this.src + "->" + this.dest + " (" + this.weight + ")";
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null || this.getClass() != other.getClass()) return false;
        EdgeData e = (Edge)other;
        return (this.src == e.getSrc() && this.dest == e.getDest() && Double.compare(this.weight, e.getWeight()) == 0 && this.info.equals(e.getInfo()) && this.tag == e.getTag());
    }
    
    /**
     * Since we have overridden equals we also are required to override hashCode
     *
     * @return the hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.src, this.dest, this.weight, this.info, this.tag);
    }
}
