package graph.scc;

import model.Graph;
import util.GraphLoader;
import util.Metrics;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {
    @Test
    public void tarjanSimpleCycle() {
        Graph g = new Graph();
        g.addEdge("A","B",1); g.addEdge("B","C",1); g.addEdge("C","A",1);
        TarjanSCC t = new TarjanSCC(g, new Metrics());
        List<List<String>> comps = t.run();
        assertEquals(1, comps.size());
        assertEquals(3, comps.get(0).size());
    }
}
