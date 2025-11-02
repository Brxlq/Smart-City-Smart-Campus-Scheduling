package graph.scc;

import model.Graph;
import util.Metrics;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CondensationTest {
    @Test
    public void condensationSimple() {
        
        Graph g = new Graph();
        g.addEdge("A","B",1); g.addEdge("B","C",1); g.addEdge("C","A",1);
        g.addEdge("D","E",1); g.addEdge("E","F",1); g.addEdge("F","D",1);
        g.addEdge("C","D",2);
        TarjanSCC t = new TarjanSCC(g, new Metrics());
        List<List<String>> comps = t.run();
        var cond = CondensationBuilder.build(g, comps, new Metrics());
        
        assertTrue(cond.adj.size() >= 2);
        boolean found = false;
        for (var e : cond.adj.entrySet()) {
            for (var kv : e.getValue().entrySet()) {
                if (kv.getValue() == 2) found = true;
            }
        }
        assertTrue(found);
    }
}
