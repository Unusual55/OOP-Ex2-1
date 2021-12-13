package datastructures.serializers;

import api.EdgeData;
import com.google.gson.*;
import datastructures.Edge;

import java.lang.reflect.Type;

/**
 * This class is the adapter that support us to load the edges from .json files and save the edges to the wanted
 * format to .json file.
 */
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

    /**
     * This function return the id of the source node of the edge
     * @return the source id
     */
    public int getSrc() {
        return this.src;
    }

    /**
     * This function return the weight of the edge
      * @return
     */
    public double getW() {
        return this.w;
    }

    /**
     * This function return the id of the destenation node of the edge
     * @return
     */
    public int getDest() {
        return this.dest;
    }

    /**
     * This function
     * @param o compare between two EdgeAdapter objects
     * @return
     */
    @Override
    public int compareTo(EdgeAdapter o) {
        return this.src - o.src;
    }

    /**
     * This function return a string representation of the Edge
     * @return
     */
    @Override
    public String toString() {
        return "{ \"src\": " + this.src + ", \"w\": " + this.w + ", \"dest\": " + this.dest + " }";
    }

    /**
     * This function serialize the EdgeData into a proper format for the json file
     * @param edgeData
     * @param type
     * @param jsonSerializationContext
     * @return
     */
    @Override
    public JsonElement serialize(EdgeData edgeData, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("src", edgeData.getSrc());
        obj.addProperty("w", edgeData.getWeight());
        obj.addProperty("dest", edgeData.getDest());
        return obj;
    }

    /**
     * This function deserialize the .json into aN object that implement the EdgeData interface.
     * @param jsonElement
     * @param type
     * @param jsonDeserializationContext
     * @return
     * @throws JsonParseException
     */
    @Override
    public EdgeData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        return new Edge(obj.get("src").getAsInt(), obj.get("dest").getAsInt(), obj.get("w").getAsDouble());
    }
}
