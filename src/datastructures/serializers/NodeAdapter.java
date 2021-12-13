package datastructures.serializers;

import api.GeoLocation;
import api.NodeData;
import com.google.gson.*;
import datastructures.Node;
import datastructures.Point3D;

import java.lang.reflect.Type;
/**
 * This class is the adapter that support us to load the nodes from .json files and save the nodes to the wanted
 * format to .json file.
 */
public class NodeAdapter implements Comparable<NodeAdapter>, JsonSerializer<NodeData>, JsonDeserializer<NodeData> {
    private String pos;
    private int id;
    
    public NodeAdapter() {
        this.pos = "";
        this.id = 0;
    }
    
    public NodeAdapter(int id, String pos) {
        this.pos = pos;
        this.id = id;
    }
    
    public NodeAdapter(int id, GeoLocation pos) {
        this.pos = pos.x() + "," + pos.y() + "," + pos.z();
        this.id = id;
    }

    /**
     * This function return a string representation of the position of the node
     * @return
     */
    public String getPos() {
        return this.pos;
    }

    /**
     * This function return the id of the node
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * This function get a String that represent a new position of the node, and assign it as the new position
     * @param pos
     */
    public void setPos(String pos) {
        this.pos = pos;
    }

    /**
     * This function get an object that implements GeoLocation and assign it as the new position of the node
     * @param pos
     */
    public void setPos(GeoLocation pos) {
        this.pos = pos.x() + "," + pos.y() + "," + pos.z();
    }

    /**
     * This function get an id number and assign it as the new id of the node
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * This function compare between two NodeAdapter objects
     * @param o
     * @return
     */
    @Override
    public int compareTo(NodeAdapter o) {
        return this.id - o.id;
    }

    /**
     * This function return a string representation of the node
     * @return
     */
    @Override
    public String toString() {
        return "{ \"pos\": \"" + this.pos + "\", \"id\": " + this.id + " }";
    }

    /**
     * This function serialize the node into the proper format of the .json file
     * @param nodeData
     * @param type
     * @param jsonSerializationContext
     * @return
     */
    @Override
    public JsonElement serialize(NodeData nodeData, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        GeoLocation pos = nodeData.getLocation();
        obj.addProperty("pos", pos.x() + "," + pos.y() + "," + pos.z());
        obj.addProperty("id", nodeData.getKey());
        return obj;
    }

    /**
     * This function deserialize the .json file into an object that implement the NodeData interface
     * @param jsonElement
     * @param type
     * @param jsonDeserializationContext
     * @return
     * @throws JsonParseException
     */
    @Override
    public NodeData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        String[] pos = obj.get("pos").getAsString().split(",");
        if (pos.length != 3) {
            throw new JsonParseException("Invalid position format");
        }
        return new Node(obj.get("id").getAsInt(), new Point3D(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2])));
    }
}
