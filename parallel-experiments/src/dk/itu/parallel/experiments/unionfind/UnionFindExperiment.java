package dk.itu.parallel.experiments.unionfind;

import java.util.ArrayList;
import java.util.HashMap;
import dk.itu.parallel.experiments.CacheMissMonitor;
import dk.itu.parallel.experiments.Experiment;
import dk.itu.parallel.experiments.GarbageCollectionTimeMonitor;
import dk.itu.parallel.experiments.Timer;
import dk.itu.parallel.unionfind.IUnionFind;

public class UnionFindExperiment extends Experiment {

	public final int threads;
	public final IUnionFind algorithm;
	ArrayList<Thread> tasks;
	public final CacheMissMonitor cmm;
	public final Graph graph;

	public UnionFindExperiment(int threads, final IUnionFind algorithm, final Graph graph) {
		this.threads = threads;
		cmm = new CacheMissMonitor(algorithm.toString(),
				new String[]{"cache-misses", "instructions", "L1-icache-prefetches"});
		this.algorithm = algorithm;
		this.graph = graph;
	}

	private void initTheads() {
		// Split edges up in equal-sized chunks.
		final int step = graph.edges.length / threads + 1;

		tasks = new ArrayList<Thread>();
		for (int i = 0; i < threads; ++i) {

			// Compute upper and lower indices instead of copying data.
			final int lower = i * step;
			final int upper = Math.min((i + 1) * step, graph.edges.length);

			tasks.add(new Thread() {
				@Override
				public void run() {
					for (int c = lower; c < upper; ++c)
						algorithm.union(graph.edges[c][0], graph.edges[c][1]);
				}
			});
		}
	}

	@Override
	public void before() {
		conduct(true);
		conduct(true);
		conduct(true);
	}

	@Override
	public void after() {
		// noop
	}

	@Override
	public HashMap<String, Long> conduct(final boolean warmup) {

		// Re-populate data.
		initTheads();
		algorithm.makeRecords(graph.nodes);

		if (!warmup)
			System.err.println("--- TEST START ---");
		else
			System.err.println("--- WARM-UP START ---");

		// Start measuring.
		cmm.start();
		final GarbageCollectionTimeMonitor gcm = new GarbageCollectionTimeMonitor();
		final Timer t = new Timer();

		// Run experiments.
		for (final Thread task : tasks)
			task.start();
		for (final Thread task : tasks) {
			try {
				task.join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Collect metrics.
		long duration = t.ms();
		long garbageTime = gcm.timeSpent();
		cmm.stop();

		if (!warmup)
			System.err.println("--- TEST END   ---");
		else
			System.err.println("--- WARM-UP END   ---");

		final HashMap<String, Long> results = cmm.getResults();
		results.put("ms", duration);
		results.put("gc", garbageTime);
		results.put("retries", algorithm.getTriesAndReset());
		return results;
	}

	@Override
	public String toCsv() {
		return String.format("%s,%d", algorithm, threads);
	}

	@Override
	public String toString() {
		return algorithm.toString();
	}

}
