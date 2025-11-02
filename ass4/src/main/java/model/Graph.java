package model;

import java.util.*;

public class Graph {
    
    private final Map<String, Map<String, Integer>> adj = new LinkedHashMap<>();
    private String source = null;

    public void addNode(String id) { adj.putIfAbsent(id, new LinkedHashMap<>()); }

    public void addEdge(String u, String v, int w) {
        addNode(u); addNode(v);
        adj.get(u).put(v, Math.toIntExact(w));
    }

    public Set<String> getNodes() { return adj.keySet(); }

    public Map<String,Integer> getNeighbors(String u) {
        return adj.getOrDefault(u, Collections.emptyMap());
    }

    public Map<String, Map<String,Integer>> getAdjacency() { return adj; }

    public void setSource(String s) { this.source = s; addNode(s); }
    public String getSource() { return source; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var u : adj.keySet()) {
            sb.append(u).append(" -> ").append(adj.get(u)).append("\n");
        }
        return sb.toString();
    }
}
