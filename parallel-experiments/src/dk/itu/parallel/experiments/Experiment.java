package dk.itu.parallel.experiments;

import java.util.HashMap;

/**
 * This class is an experiment setup. The setup is
 * made when calling the constructor and it can be
 * executed as many times necessary in the same
 * setup.
 */
public abstract class Experiment {

	/**
	 * Executed before calling conduct().
	 */
	abstract public void before();

	/**
	 * Executed after done calling conduct().
	 */
	abstract public void after();

	/**
	 * Conduct this experiment.
	 *
	 * @return Time and cache-misses during the experiment.
	 */
	abstract public HashMap<String, Long> conduct(final boolean warmup);

	/**
	 * @return Standard CSV header for experiments.
	 */
	public static String csvHeader() {
		return "#algorithm,threads";
	}

	/**
	 * @return CSV representation of this experiment.
	 */
	abstract public String toCsv();
}
