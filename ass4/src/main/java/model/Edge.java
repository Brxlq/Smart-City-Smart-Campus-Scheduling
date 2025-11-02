package model;

public class Edge {
    public String u, v;
    public int w;

    public Edge(String u, String v, int w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    @Override
    public String toString() {
        return u + " -> " + v + " (" + w + ")";
    }
}
