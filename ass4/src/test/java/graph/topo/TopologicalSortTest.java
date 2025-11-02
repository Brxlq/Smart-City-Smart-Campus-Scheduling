package graph.topo;

import org.junit.jupiter.api.Test;
import util.Metrics;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TopologicalSortTest {
    @Test
    public void topoSimple() {
        Map<Integer, Map<Integer,Integer>> adj = new LinkedHashMap<>();
        adj.put(0, Map.of(1,1,2,1));
        adj.put(1, Map.of(3,1));
        adj.put(2, Map.of(3,1));
        adj.put(3, Map.of());
        Metrics m = new Metrics();
        List<Integer> order = TopologicalSort.sortComponents(adj, m);
        assertEquals(4, order.size());
        assertTrue(order.indexOf(0) < order.indexOf(1));
        assertTrue(order.indexOf(0) < order.indexOf(2));
    }
}
