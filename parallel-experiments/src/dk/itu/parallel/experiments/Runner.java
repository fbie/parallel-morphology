package dk.itu.parallel.experiments;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Runner {

	protected int threads;
	protected int runs;

	/**
	 * Initialize runner to hard-coded default values.
	 */
	protected void initDefault() {
		threads = 3;
		runs = 3;
	}

	/**
	 * Construct runner from command-line arguments.
	 *
	 * @param args Arguments passed to executable.
	 */
	public Runner(final String[] args) {
		parseArgs(args);
	}

	/**
	 * Set-up and run all experiments.
	 */
	public final void run() {
		runExperiments(setupExperiments());
	}

	/**
	 * Create experiments from filters and the data in this runner.
	 *
	 * @param filters Filters to experiment with.
	 * @return A list of experiments to conduct.
	 */
	abstract public ArrayList<Experiment> setupExperiments();

	/**
	 * Run all experiments and average their running time over
	 * runs runs.
	 *
	 * @param experiments Experiments to conduct.
	 */
	public final void runExperiments(final ArrayList<Experiment> experiments) {
		boolean printedHeader = false;
		while (!experiments.isEmpty()) {
			final Experiment e = experiments.remove(0);

			e.before();
			final Result r = timeAverage(e, runs);
			e.after();

			System.err.println(r);
			if (!printedHeader) {
				System.out.println(r.csvHeader());
				printedHeader = true;
			}
			System.out.println(r.toCsv());
		}
	}

	/**
	 * Compute the average run-time of the experiment
	 * for n runs.
	 *
	 * @param exp Experiment to conduct.
	 * @param n Number of times to conduct experiment.
	 * @return Average time in seconds.
	 */
	protected Result timeAverage(final Experiment exp, final int n) {
		final ArrayList<HashMap<String, Long>> results = new ArrayList<HashMap<String, Long>>();
		for (int i = 0; i < n; ++i) {

			// Suggest to rather garbage collect
			// now than during experiments.
			System.gc();

			results.add(exp.conduct(false));
		}
		return new Result(exp, results);
	}

	/**
	 * Parse arguments and set up runner based
	 * on these. Re-sets runner to default values
	 * before parsing.
	 *
	 * @param args Arguments passed to executable.
	 */
	abstract public void parseArgs(final String[] args);

	/**
	 * Print a help message to stderr.
	 */
	abstract public void printHelp();
}
