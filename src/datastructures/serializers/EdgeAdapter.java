package datastructures.serializers;

import api.EdgeData;
import com.google.gson.*;
import datastructures.Edge;

import java.lang.reflect.Type;

public class EdgeAdapter implements Comparable<EdgeAdapter>, JsonSerializer<EdgeData>, JsonDeserializer<EdgeData> {
    private int src;
    private double w;
    private int dest;
    
    public EdgeAdapter() {
        this.src = -1;
        this.w = -1.0;
        this.dest = -1;
    }
    
    public EdgeAdapter(int src, double w, int dest) {
        this.src = src;
        this.w = w;
        this.dest = dest;
    }
    
    public int getSrc() {
        return this.src;
    }
    
    public double getW() {
        return this.w;
    }
    
    public int getDest() {
        return this.dest;
    }
    
    
    @Override
    public int compareTo(EdgeAdapter o) {
        return this.src - o.src;
    }
    
    @Override
    public String toString() {
        return "{ \"src\": " + this.src + ", \"w\": " + this.w + ", \"dest\": " + this.dest + " }";
    }
    
    
    @Override
    public JsonElement serialize(EdgeData edgeData, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("src", edgeData.getSrc());
        obj.addProperty("w", edgeData.getWeight());
        obj.addProperty("dest", edgeData.getDest());
        return obj;
    }
    
    @Override
    public EdgeData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        return new Edge(obj.get("src").getAsInt(), obj.get("dest").getAsInt(), obj.get("w").getAsDouble());
    }
}
