package dk.itu.parallel.experiments;

/**
 * A timer to time task durations.
 */
public final class Timer {
	long start;

	/**
	 * Create and start the timer.
	 */
	public Timer () {
		start();
	}

	/**
	 * (Re)Start the timer by re-setting its
	 * start time stamp.
	 */
	public void start() {
		start = System.nanoTime();
	}

	/**
	 * @return Elapsed time since last call to start() in nanoseconds.
	 */
	public long nanos() {
		return System.nanoTime() - start;
	}

	/**
	 * @return Elapsed time since last call to start() in milliseconds (rounded).
	 */
	public long ms() {
		return (System.nanoTime() - start) / 1000000L;
	}

	/**
	 * @return Elapsed time since last call to start() in seconds.
	 */
	public float secs() {
		long duration = System.nanoTime() - start;
		return duration / 1000000000.0f;
	}
}
