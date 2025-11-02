package graph.topo;

import util.Metrics;

import java.util.*;


public class TopologicalSort {

    
    public static List<Integer> sortComponents(Map<Integer, Map<Integer,Integer>> adj, Metrics metrics) {
        Map<Integer,Integer> indeg = new LinkedHashMap<>();
        for (Integer u : adj.keySet()) indeg.put(u, 0);
        for (var entry : adj.entrySet()) {
            for (Integer v : entry.getValue().keySet()) indeg.put(v, indeg.getOrDefault(v,0)+1);
        }
        Queue<Integer> q = new ArrayDeque<>();
        for (var e : indeg.entrySet()) if (e.getValue()==0) { q.add(e.getKey()); metrics.kahnPushes++; }
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.remove(); metrics.kahnPops++;
            order.add(u);
            for (Integer v : adj.getOrDefault(u, Collections.emptyMap()).keySet()) {
                indeg.put(v, indeg.get(v)-1);
                if (indeg.get(v)==0) { q.add(v); metrics.kahnPushes++; }
            }
        }
        return order;
    }

    
    public static List<String> sortStrings(Map<String, Map<String,Integer>> adj, Metrics metrics) {
        Map<String,Integer> indeg = new LinkedHashMap<>();
        for (String u : adj.keySet()) indeg.put(u, 0);
        for (var entry : adj.entrySet()) for (String v : entry.getValue().keySet()) indeg.put(v, indeg.getOrDefault(v,0)+1);
        Queue<String> q = new ArrayDeque<>();
        for (var e : indeg.entrySet()) if (e.getValue()==0) { q.add(e.getKey()); metrics.kahnPushes++; }
        List<String> order = new ArrayList<>();
        while (!q.isEmpty()) {
            String u = q.remove(); metrics.kahnPops++;
            order.add(u);
            for (String v : adj.getOrDefault(u, Collections.emptyMap()).keySet()) {
                indeg.put(v, indeg.get(v)-1);
                if (indeg.get(v)==0) { q.add(v); metrics.kahnPushes++; }
            }
        }
        return order;
    }
}
