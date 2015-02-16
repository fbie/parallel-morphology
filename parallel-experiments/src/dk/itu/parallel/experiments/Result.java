package dk.itu.parallel.experiments;

import java.util.ArrayList;
import java.util.HashMap;

public class Result {

	final Experiment exp;
	final int runs;
	final HashMap<String, Long> results;

	public Result(final Experiment exp, final ArrayList<HashMap<String, Long>> results) {
		this.exp = exp;
		this.results = new HashMap<String, Long>();
		runs = results.size();

		// Compute average of everything.
		for (final HashMap<String, Long> r : results) {
			for (final String key : r.keySet()) {
				if (!this.results.containsKey(key))
					this.results.put(key, r.get(key));
				else
					this.results.put(key, this.results.get(key) + r.get(key));
			}
		}

		for (final String key : this.results.keySet()) {
			this.results.put(key, (long)(this.results.get(key) / runs + 0.5));
		}
	}

	public String csvHeader() {
		final StringBuilder builder = new StringBuilder(Experiment.csvHeader());
		for (final String key : results.keySet()) {
			builder.append(",");
			builder.append(key);
		}
		return builder.toString();
	}

	public String toCsv() {
		final StringBuilder builder = new StringBuilder(exp.toCsv());
		for (final long val : results.values()) {
			builder.append(",");
			builder.append(val);
		}
		return builder.toString();
	}

	@Override
	public String toString() {
		return toCsv();
	}
}
