package api;


import java.util.Objects;

/**
 * This class represent an edge in the graph.
 * The properties of every edge are the id of the source vertex and the destenation vertex, it's weight,
 * it's info and it's tag which we use in the algorithm to check it's color.
 * The numbers that represent colors of edges are the same as in Node class:
 *      -1) White- Unvisited edge which we didn't discover yet
 *      0) Gray- Unvisited edge which we already discovered
 *      1) Black- Visited edge which we already left
 */
public class Edge implements EdgeData {
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;
    
    public Edge(int src, int dest, double weight, String info, int tag) {
        if (weight < 0) throw new IllegalArgumentException("Weight cannot be negative");
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        this.info = info;
        this.tag = tag;
    }
    
    public Edge(int src, int dest, double weight) {
        this(src, dest, weight, "", -1);
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

    /**
     * This fuction returns a string representation of this object
     * @return String that represents this object
     */
    @Override
    public String toString() {
        return "" + this.src + "->" + this.dest + " (" + this.weight + ")";
    }

    /**
     * This function get an object the compare it to this object
     * @param other The object we compare to this object
     * @return True if they are equal, otherwise false
     */
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
