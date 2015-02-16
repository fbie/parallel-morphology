package dk.itu.parallel.experiments.morphology;

import java.util.HashMap;

import dk.itu.parallel.experiments.CacheMissMonitor;
import dk.itu.parallel.experiments.Experiment;
import dk.itu.parallel.experiments.GarbageCollectionTimeMonitor;
import dk.itu.parallel.experiments.Timer;
import dk.itu.parallel.morphology.filter.base.Filter;
import dk.itu.parallel.morphology.util.image.Image;

public class MorphologyExperiment extends Experiment {

	public final Filter filter;
	public final Image img;
	public final int lambda;

	final CacheMissMonitor cmm;

	/**
	 * Construct an experiment. The experiment can be run
	 * as many times as you could wish for. The state
	 * is never changed and the image won't be messed with.
	 *
	 * @param filter Filter algorithm to use in this experiment.
	 * @param img Image to filter.
	 * @param lambda Lambda to filter image for.
	 */
	public MorphologyExperiment(final Filter filter, final Image img, final int lambda) {
		this.filter = filter;
		this.img = img;
		this.lambda = lambda;

		cmm = new CacheMissMonitor(filter.toString(),
				new String[]{"cache-misses", "instructions", "L1-icache-prefetches"});
	}

	/**
	 * Execute before calling conduct().
	 */
	@Override
	public void before() {
		conduct(true);
		conduct(true);
		conduct(true);
	}

	/**
	 * Execute after done calling conduct().
	 */
	@Override
	public void after() {
		// noop
	}

	/**
	 * Conduct this experiment.
	 *
	 * @return Time taken to conduct the experiment.
	 */
	@Override
	public HashMap<String, Long> conduct(final boolean warmup) {
		// Set up filter.
		filter.initialize(img, lambda);

		if (!warmup)
			System.err.println("--- TEST START ---");
		else
			System.err.println("--- WARM-UP START ---");

		// Start measuring.
		cmm.start();
		final GarbageCollectionTimeMonitor gcm = new GarbageCollectionTimeMonitor();
		final Timer t = new Timer();

		// Run experiment.
		filter.filter(lambda);

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
		results.put("retries", filter.u.getTriesAndReset());
		results.put("gc", garbageTime);
		return results;
	}

	@Override
	public String toCsv() {
		return String.format("%s,%d", filter, filter.threads);
	}

	@Override
	public String toString() {
		return String.format("%s on a %dx%d image for lambda=%d using %d threads",
				filter, img.width, img.height, lambda, filter.threads);
	}

}
