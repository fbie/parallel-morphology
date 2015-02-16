package dk.itu.parallel.experiments.unionfind;

public class Graph {

	public final int nodes;
	public final int[][] edges;

	public Graph() {
		nodes = 0;
		edges = new int[0][0];
	}

	public Graph(final int nodes, final int[][] edges) {
		this.nodes = nodes;
		this.edges = edges;
	}
}
