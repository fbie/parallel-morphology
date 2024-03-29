package dk.itu.parallel.experiments.unionfind;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dk.itu.parallel.experiments.Experiment;
import dk.itu.parallel.experiments.Runner;
import dk.itu.parallel.unionfind.Optimistic;
import dk.itu.parallel.unionfind.Transactional;
import dk.itu.parallel.unionfind.TransactionalWhile;
import dk.itu.parallel.unionfind.WaitFree;

public class UnionFindRunner extends Runner {

	Graph graph;

	@Override
	public void initDefault() {
		super.initDefault();
		graph = new Graph();
	}

	public UnionFindRunner(final String[] args) {
		super(args);
	}

	@Override
	public ArrayList<Experiment> setupExperiments() {
		final ArrayList<Experiment> experiments = new ArrayList<Experiment>();

		for (int t = 1; t <= threads; ++t) {
			experiments.add(new UnionFindExperiment(t, new WaitFree(), graph));
			experiments.add(new UnionFindExperiment(t, new Optimistic(), graph));
			experiments.add(new UnionFindExperiment(t, new Transactional(), graph));
			experiments.add(new UnionFindExperiment(t, new TransactionalWhile(), graph));
		}
		return experiments;
	}

	@Override
	public void parseArgs(String[] args) {
		initDefault();

		switch (args.length) {
		case 3:
			runs = Integer.parseInt(args[2]);
		case 2:
			threads = Integer.parseInt(args[1]);
		case 1:
			// If the argument is -h or --help, print help.
			if (!(args[0].equals("-h") || args[0].equals("--help"))) {
				graph = parseFile(args[0]);
				break;
			}
		default:
			printHelp();
			System.exit(0);
		}
	}

	/**
	 * This function parses graphs as generated by the GTgraph package.
	 *
	 * @param filename Path to the file.
	 * @return A graph object holding an array of arrays (edges) and number of vertices.
	 */
	public static Graph parseFile(final String filename) {
		String line;
		int vertices = 0;
		int[][] edges = null;
		int current = 0;

		try {
			final BufferedReader br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {

				// Get number of vertices.
				if (line.startsWith("c No. of vertices")) {
					String[] elements = line.split("\\s+:\\s+");
					vertices = Integer.parseInt(elements[1].trim());

				// Get number of edges.
				} else if (line.startsWith("c No. of directed edges")) {
					final int len = Integer.parseInt(line.split("\\s+:\\s+")[1].trim());
					edges = new int[len][2];

				// Parse edges.
				} else if (line.startsWith("a")) {
					final String[] elements = line.split(" ");

					// Decrement by one to adapt to array usage.
					edges[current++] = new int[]{
							Integer.parseInt(elements[1].trim()) - 1,
							Integer.parseInt(elements[2].trim()) - 1};
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Graph(vertices, edges);
	}

	@Override
	public void printHelp() {
		System.err.println("Usage: java dk.itu.parallel.experiments.UnionFindRunner graph [nThreads*] [nRuns*]");
		System.err.println(" graph		Path to text file defining a graph.");
		System.err.println(" nThreads	Upper limit of threads to use for tests. Default is 3.");
		System.err.println(" nRuns		Number of runs to average timing over. Default is 3.");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final UnionFindRunner r = new UnionFindRunner(args);
		r.run();
	}

}
