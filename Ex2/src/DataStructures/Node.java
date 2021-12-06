package DataStructures;


import api.GeoLocation;
import api.NodeData;

import java.util.Comparator;
import java.util.Objects;

/*
This function represent a vertex in the graph.
The properties every Node object contains are:
1) key: Unique number that change from every node and used as it's id.
2) position: Location in the 3D space
3) weight: The weight this node have
4) info: Information about the node
5) tag: Integer property that used as color marking while traveling the graph
    -1) White- Unvisited node which we didn't discover yet
    0) Gray- Unvisited node which we already discover
    1) Black- Visited node which we already left
 */
public class Node implements NodeData, Comparator<Node> {
    private int key;
    private GeoLocation position;
    private double weight;
    private String info;
    private int tag;

    public Node(int key, GeoLocation position, double weight, String info, int tag) {
        this.key = key;
        this.position = position;
        this.weight = weight;
        this.info = info;
        this.tag = tag;
    }

    public Node(NodeData other) {
        this(other.getKey(), new Point3D(other.getLocation()), other.getWeight(), other.getInfo(), other.getTag());
    }

    /**
     * Returns the key (id) associated with this node.
     *
     * @return
     */
    @Override
    public int getKey() {
        return this.key;
    }

    /**
     * Returns the location of this node, if none return null.
     *
     * @return
     */
    @Override
    public GeoLocation getLocation() {
        return this.position;
    }

    /**
     * Allows changing this node's location.
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(GeoLocation p) {
        this.position = new Point3D(p);
    }

    /**
     * Returns the weight associated with this node.
     *
     * @return
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
     * Returns the remark (meta data) associated with this node.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return this.info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
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
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    /**
     * This function return a String representation of this object
     *
     * @return String that represent this object
     */
    @Override
    public String toString() {
        return "(" + key + ")";
    }

    /**
     * This function get object as an input and check if it's equal to this object
     *
     * @param other the object we compare this object to
     * @return True if they are equal, otherwise false
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Node node = (Node) other;
        return this.key == node.getKey() &&
                this.position.equals(node.getLocation()) &&
                Double.compare(this.weight, node.getWeight()) == 0 &&
                this.info.equals(node.getInfo()) &&
                this.tag == node.getTag();
    }

    /**
     * Since we have overridden equals we also are required to override hashCode
     *
     * @return the hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.position, this.weight, this.info, this.tag);
    }

    @Override
    public int compare(Node node1, Node node2) {

        if (node1.getWeight() < node2.getWeight())
            return -1;

        if (node1.getWeight() > node2.getWeight())
            return 1;

        return 0;
    }
}
