package graph.scc;

import model.Graph;
import util.Metrics;

import java.util.*;

public class CondensationBuilder {

    public static class Condensation {
        public final Map<Integer, Map<Integer, Integer>> adj = new LinkedHashMap<>();
        public final Map<String, Integer> nodeToComp = new HashMap<>();
        public final Map<Integer, List<String>> compNodes = new LinkedHashMap<>();
        public final Map<Integer, Integer> compSize = new LinkedHashMap<>();
    }

    public static Condensation build(Graph g, List<List<String>> comps, Metrics metrics) {
        Condensation c = new Condensation();
        
        for (int i = 0; i < comps.size(); ++i) {
            c.compNodes.put(i, new ArrayList<>(comps.get(i)));
            c.compSize.put(i, comps.get(i).size());
            for (String v : comps.get(i)) c.nodeToComp.put(v, i);
        }
        
        for (int i = 0; i < comps.size(); ++i) c.adj.put(i, new LinkedHashMap<>());

        for (String u : g.getNodes()) {
            int cu = c.nodeToComp.get(u);
            for (var e : g.getNeighbors(u).entrySet()) {
                metrics.dfsEdgesVisited++;
                String v = e.getKey();
                int w = e.getValue();
                int cv = c.nodeToComp.get(v);
                if (cu != cv) {
                    Map<Integer,Integer> row = c.adj.get(cu);
                    row.put(cv, row.getOrDefault(cv,0) + w);
                }
            }
        }
        return c;
    }
}
