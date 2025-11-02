package graph.dagsp;

import org.junit.jupiter.api.Test;
import util.Metrics;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class DAGShortestPathTest {
    @Test
    public void shortestAndLongest() {
        Map<Integer, Map<Integer,Integer>> adj = new LinkedHashMap<>();
        adj.put(0, new LinkedHashMap<>(Map.of(1,2,2,4)));
        adj.put(1, new LinkedHashMap<>(Map.of(2,1)));
        adj.put(2, new LinkedHashMap<>(Map.of(3,3)));
        adj.put(3, new LinkedHashMap<>());
        List<Integer> topo = List.of(0,1,2,3);
        Metrics m = new Metrics();
        var res = DAGShortestPaths.shortest(adj, topo, 0, m);
        assertEquals(3L, res.dist.get(2));
        var lr = DAGShortestPaths.longest(adj, topo, 0, m);
        assertEquals(7L, lr.dist.get(3));
    }
}
