package graph.scc;

import model.Graph;
import util.Metrics;

import java.util.*;


public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;

    public TarjanSCC(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public List<List<String>> run() {
        Map<String, Integer> index = new HashMap<>();
        Map<String, Integer> low = new HashMap<>();
        Deque<String> stack = new ArrayDeque<>();
        Set<String> onStack = new HashSet<>();
        List<List<String>> comps = new ArrayList<>();
        int[] idx = {0};

        for (String v : graph.getNodes()) {
            if (!index.containsKey(v)) strongconnect(v, index, low, stack, onStack, comps, idx);
        }
        return comps;
    }

    private void strongconnect(String v,
                               Map<String,Integer> index,
                               Map<String,Integer> low,
                               Deque<String> stack,
                               Set<String> onStack,
                               List<List<String>> comps,
                               int[] idx) {
        index.put(v, idx[0]);
        low.put(v, idx[0]);
        idx[0]++;
        stack.push(v);
        onStack.add(v);
        metrics.dfsNodesVisited++;

        for (var entry : graph.getNeighbors(v).entrySet()) {
            String w = entry.getKey();
            metrics.dfsEdgesVisited++;
            if (!index.containsKey(w)) {
                strongconnect(w, index, low, stack, onStack, comps, idx);
                low.put(v, Math.min(low.get(v), low.get(w)));
            } else if (onStack.contains(w)) {
                low.put(v, Math.min(low.get(v), index.get(w)));
            }
        }

        if (low.get(v).equals(index.get(v))) {
            List<String> comp = new ArrayList<>();
            String w;
            do {
                w = stack.pop();
                onStack.remove(w);
                comp.add(w);
            } while (!w.equals(v));
            comps.add(comp);
        }
    }
}
