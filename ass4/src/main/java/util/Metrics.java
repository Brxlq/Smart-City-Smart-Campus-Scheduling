package util;

public class Metrics {
    public long dfsNodesVisited = 0;
    public long dfsEdgesVisited = 0;
    public long kahnPushes = 0;
    public long kahnPops = 0;
    public long relaxations = 0;
    private long startNs = 0;
    private long elapsedNs = 0;

    public void startTimer() { startNs = System.nanoTime(); }
    public void stopTimer() { elapsedNs = System.nanoTime() - startNs; }
    public long elapsedNs() { return elapsedNs; }

    @Override
    public String toString() {
        return String.format("time(ns)=%d, dfsNodes=%d, dfsEdges=%d, kahnPushes=%d, kahnPops=%d, relax=%d",
                elapsedNs, dfsNodesVisited, dfsEdgesVisited, kahnPushes, kahnPops, relaxations);
    }
}
