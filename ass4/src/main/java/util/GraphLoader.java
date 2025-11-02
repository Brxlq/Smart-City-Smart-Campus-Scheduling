package util;

import com.google.gson.*;
import model.Graph;

import java.io.FileReader;

public class GraphLoader {
    
    public static Graph loadGraph(String path) {
        try (FileReader fr = new FileReader(path)) {
            JsonObject root = JsonParser.parseReader(fr).getAsJsonObject();
            Graph g = new Graph();
            if (root.has("edges")) {
                JsonArray edges = root.getAsJsonArray("edges");
                for (JsonElement e : edges) {
                    JsonObject o = e.getAsJsonObject();
                    String u = o.get("u").getAsString();
                    String v = o.get("v").getAsString();
                    int w = o.get("w").getAsInt();
                    g.addEdge(u, v, w);
                }
            }
            if (root.has("source")) g.setSource(root.get("source").getAsString());
            return g;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
