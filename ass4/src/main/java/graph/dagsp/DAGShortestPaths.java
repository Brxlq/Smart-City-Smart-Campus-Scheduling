package graph.dagsp;

import util.Metrics;

import java.util.*;


public class DAGShortestPaths {

    public static class PathResult {
        public final Map<Integer, Long> dist;
        public final Map<Integer, Integer> pred;
        public PathResult(Map<Integer, Long> dist, Map<Integer,Integer> pred) { this.dist=dist; this.pred=pred; }
        public List<Integer> reconstruct(int target) {
            LinkedList<Integer> path = new LinkedList<>();
            Integer cur = target;
            while (cur != null) { path.addFirst(cur); cur = pred.get(cur); }
            return path;
        }
    }

    public static PathResult shortest(Map<Integer, Map<Integer,Integer>> adj, List<Integer> topo, int src, Metrics metrics) {
        Map<Integer, Long> dist = new HashMap<>();
        Map<Integer, Integer> pred = new HashMap<>();
        for (Integer v : adj.keySet()) dist.put(v, Long.MAX_VALUE/4);
        dist.put(src, 0L);
        for (Integer u : topo) {
            if (dist.get(u) == Long.MAX_VALUE/4) continue;
            for (var e : adj.getOrDefault(u, Collections.emptyMap()).entrySet()) {
                int v = e.getKey(); int w = e.getValue();
                metrics.relaxations++;
                long alt = dist.get(u) + w;
                if (alt < dist.get(v)) { dist.put(v, alt); pred.put(v, u); }
            }
        }
        return new PathResult(dist, pred);
    }

    public static PathResult longest(Map<Integer, Map<Integer,Integer>> adj, List<Integer> topo, int src, Metrics metrics) {
        Map<Integer, Long> dist = new HashMap<>();
        Map<Integer, Integer> pred = new HashMap<>();
        for (Integer v : adj.keySet()) dist.put(v, Long.MIN_VALUE/4);
        dist.put(src, 0L);
        for (Integer u : topo) {
            if (dist.get(u) == Long.MIN_VALUE/4) continue;
            for (var e : adj.getOrDefault(u, Collections.emptyMap()).entrySet()) {
                int v = e.getKey(); int w = e.getValue();
                metrics.relaxations++;
                long alt = dist.get(u) + w;
                if (alt > dist.get(v)) { dist.put(v, alt); pred.put(v, u); }
            }
        }
        return new PathResult(dist, pred);
    }
}
