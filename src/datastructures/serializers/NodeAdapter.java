package datastructures.serializers;

import api.GeoLocation;
import api.NodeData;
import com.google.gson.*;
import datastructures.Node;
import datastructures.Point3D;

import java.lang.reflect.Type;

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
    
    public String getPos() {
        return this.pos;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setPos(String pos) {
        this.pos = pos;
    }
    
    public void setPos(GeoLocation pos) {
        this.pos = pos.x() + "," + pos.y() + "," + pos.z();
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    @Override
    public int compareTo(NodeAdapter o) {
        return this.id - o.id;
    }
    
    @Override
    public String toString() {
        return "{ \"pos\": \"" + this.pos + "\", \"id\": " + this.id + " }";
    }
    
    @Override
    public JsonElement serialize(NodeData nodeData, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        GeoLocation pos = nodeData.getLocation();
        obj.addProperty("pos", pos.x() + "," + pos.y() + "," + pos.z());
        obj.addProperty("id", nodeData.getKey());
        return obj;
    }
    
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
