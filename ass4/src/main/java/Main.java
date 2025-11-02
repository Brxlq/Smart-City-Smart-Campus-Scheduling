import model.Graph;
import util.GraphLoader;
import util.Metrics;
import graph.scc.TarjanSCC;
import graph.scc.CondensationBuilder;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPaths;

import java.util.*;


public class Main {
    public static void main(String[] args) {
        String path = args.length>0 ? args[0] : "C:\\Users\\erko\\Documents\\ee\\ass4\\data\\small2.json";
        Graph g = GraphLoader.loadGraph(path);
        System.out.println("Loaded graph:\n" + g);

        Metrics m = new Metrics();

       
        m.startTimer();
        TarjanSCC tarjan = new TarjanSCC(g, m);
        List<List<String>> comps = tarjan.run();
        m.stopTimer();
        System.out.println("SCC count=" + comps.size());
        for (int i=0;i<comps.size();++i) System.out.println(" comp " + i + " size=" + comps.get(i).size() + " nodes=" + comps.get(i));
        System.out.println("Metrics after SCC: " + m);

        
        Metrics m2 = new Metrics();
        m2.startTimer();
        CondensationBuilder.Condensation cond = CondensationBuilder.build(g, comps, m2);
        m2.stopTimer();
        System.out.println("\nCondensation DAG (component adjacency with weights):");
        for (var e : cond.adj.entrySet()) System.out.println(e.getKey() + " -> " + e.getValue());
        System.out.println("Condensation metrics: " + m2);

        
        Metrics m3 = new Metrics(); m3.startTimer();
        List<Integer> topo = TopologicalSort.sortComponents(cond.adj, m3);
        m3.stopTimer();
        System.out.println("\nTopological order of components: " + topo);

        
        List<String> expanded = new ArrayList<>();
        for (int cid : topo) {
            List<String> nodes = new ArrayList<>(cond.compNodes.get(cid));
            Collections.sort(nodes);
            expanded.addAll(nodes);
        }
        System.out.println("Derived order of original tasks after SCC compression: " + expanded);
        System.out.println("Topo metrics: " + m3);

        
        String srcNode = g.getSource();
        int srcComp;
        if (srcNode != null && cond.nodeToComp.containsKey(srcNode)) {
            srcComp = cond.nodeToComp.get(srcNode);
            System.out.println("Using source node from JSON: " + srcNode + " -> comp " + srcComp);
        } else {
            
            Map<Integer,Integer> indeg = new HashMap<>();
            for (Integer u : cond.adj.keySet()) indeg.put(u, 0);
            for (var e : cond.adj.entrySet()) for (Integer v : e.getValue().keySet()) indeg.put(v, indeg.getOrDefault(v,0)+1);
            srcComp = indeg.entrySet().stream().filter(en->en.getValue()==0).map(Map.Entry::getKey).findFirst().orElse(0);
            System.out.println("No source in JSON; choosing component source: " + srcComp);
        }

        Metrics m4 = new Metrics(); m4.startTimer();
        
        var shortest = DAGShortestPaths.shortest(cond.adj, topo, srcComp, m4);
        m4.stopTimer();
        System.out.println("\nShortest distances (component-level) from comp " + srcComp + ":");
        for (var e : shortest.dist.entrySet()) System.out.println(" comp " + e.getKey() + " dist=" + (e.getValue()>=Long.MAX_VALUE/4 ? "INF" : e.getValue()));
        System.out.println("DAG-SP metrics (shortest): " + m4);

        
        Metrics m5 = new Metrics(); m5.startTimer();
        long bestLen = Long.MIN_VALUE; List<Integer> bestPath = null; int bestSrc = -1;
        
        Map<Integer,Integer> indeg2 = new HashMap<>();
        for (Integer u : cond.adj.keySet()) indeg2.put(u, 0);
        for (var e : cond.adj.entrySet()) for (Integer v : e.getValue().keySet()) indeg2.put(v, indeg2.get(v)+1);
        List<Integer> sources = new ArrayList<>();
        for (var en : indeg2.entrySet()) if (en.getValue()==0) sources.add(en.getKey());
        if (sources.isEmpty()) sources.add(srcComp);

        for (int s : sources) {
            var lr = DAGShortestPaths.longest(cond.adj, topo, s, m5);
            for (var e : lr.dist.entrySet()) {
                long val = e.getValue();
                if (val > bestLen) { bestLen = val; bestSrc = s; bestPath = lr.reconstruct(e.getKey()); }
            }
        }
        m5.stopTimer();
        System.out.println("\nCritical path on condensation (component ids): " + bestPath + " length=" + bestLen + " from srcComp=" + bestSrc);
        
        List<String> expandedCritical = new ArrayList<>();
        if (bestPath != null) for (int cid : bestPath) {
            List<String> nodes = new ArrayList<>(cond.compNodes.get(cid));
            Collections.sort(nodes);
            expandedCritical.addAll(nodes);
        }
        System.out.println("Critical path expanded to original nodes: " + expandedCritical);
        System.out.println("DAG-SP (longest) metrics: " + m5);

        
        System.out.println("\nSUMMARY: file=" + path + " nodes=" + g.getNodes().size() + " components=" + comps.size() + " compSizes=" + cond.compSize);
    }
}
