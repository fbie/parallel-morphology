package dk.itu.parallel.experiments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * A performance measurement tool that wraps
 * around perf on Linux. Don't even try to get
 * it to run on Windows like this.
 */
public class CacheMissMonitor {

	final HashMap<String, Long> events;

	/**
	 * Worker thread to scan through the output of the perf.
	 */
	class PerfOutputScanner extends Thread {

		@Override
		public void run() {

			// Hook up to perf's stderr.
			final InputStream err = perf.getErrorStream();
			final BufferedReader errReader = new BufferedReader(new InputStreamReader(err));

			// Scan stderr for cache miss output.
			try {
				String line;
				while ((line = errReader.readLine()) != null) {
					final String[] elements = line.split("\\s+");
					for (final String event : events.keySet()) {
						if (line.contains(event)) {
							events.put(event, Long.parseLong(elements[1].replace(".", "")));
							break;
						}
					}
				}
				errReader.close();
			} catch (final IOException e) {
				ignore(e);
			}
		}
	}

	final String name;
	final Runtime rt;

	// That is the same process we're running in.
	final int pid;

	volatile Process perf;

	// For scanning stderr of perf.
	Thread scanner;

	public CacheMissMonitor(final String name, final String[] events) {
		this.name = name;

		// The runtime reference makes
		// it more handy to start a new
		// process.
		rt = Runtime.getRuntime();

		// http://stackoverflow.com/questions/35842/how-can-a-java-program-get-its-own-process-id
		pid = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);

		this.events = new HashMap<String, Long>();
		for (final String event : events)
			this.events.put(event, (long) -1);
	}

	/**
	 * Start measuring cache misses if
	 * not currently monitoring. Never
	 * throws, since this class only
	 * monitors.
	 */
	public void start() {

		// Ignore if already monitoring, but
		// let the user know.
		if (perf != null) {
			message("Already monitoring, ignoring call to start()");
			return;
		}

		// Build a comma-separated list of events.
		StringBuilder builder = new StringBuilder();
		for (final String event : events.keySet()) {
			builder.append(event);
			builder.append(",");
		}
		builder.deleteCharAt(builder.length() - 1);

		// Try to create a process, but never
		// mess with the program. Notify the user
		// but otherwise, just continue.
		try {
			perf = rt.exec("perf stat --log-fd 2 -e " + builder.toString() + " -p " + pid);
//			perf = rt.exec("perf stat -e " + builder.toString() + " -p " + pid);
			try {
				Thread.sleep(10);	// Give the process some time to start.
			} catch (InterruptedException e) {
				// Really ignore this!
			}
			scanner = new PerfOutputScanner();
			scanner.start();
		} catch (final SecurityException | IOException e) {
			ignore(e);
		}
	}

	/**
	 * Stop measuring cache misses if
	 * currently monitoring. Never
	 * throws.
	 */
	public void stop() {
		if (perf == null) {
			message("Already stopped.");
			return;
		}

		try {
			// Get perf's PID the Java-way.
			final Field perfField = perf.getClass().getDeclaredField("pid");
			perfField.setAccessible(true);
			final int perfPid = perfField.getInt(perf);

			// Kill that perf.
			final Process killer = rt.exec("kill -2 " + perfPid);
			killer.waitFor();

		} catch (final IllegalArgumentException	// Dangerous business here!
				| IllegalAccessException
				| NoSuchFieldException
				| SecurityException
				| IOException
				| InterruptedException e) {
			ignore(e);
			message("Cannot measure cache misses!");
			perf.destroy();
		}

		// Just close everything.
		try {
			perf.waitFor();
			scanner.join();
			perf = null;
		} catch (final InterruptedException e) {
			ignore(e);
		}
	}

	/**
	 * Returns the last measured number of occurences of a given event.
	 *
	 * @param event The event to look up.
	 * @return Number of occurences of the given event.
	 */
	public long getLast(final String event) {
		return events.get(event);
	}

	/**
	 * @return A copy of all results at once.
	 */
	public HashMap<String, Long> getResults() {
		return new HashMap<String, Long>(events);
	}

	/**
	 * Print a somewhat formatted message that
	 * is obviously associated to this object
	 * to stderr.
	 *
	 * @param msg Message to print.
	 */
	private void message(final String msg) {
		System.err.println(this + ": " + msg);
	}

	/**
	 * Ignore an exception but notify the user
	 * that it happened.
	 *
	 * @param e Some exception.
	 */
	private void ignore(final Exception e) {
		message(e.toString());
		message("Continuing...");
	}

	@Override
	public String toString() {
		return CacheMissMonitor.class.getName() + " \"" + name + "\" PID " + pid;
	}

}
