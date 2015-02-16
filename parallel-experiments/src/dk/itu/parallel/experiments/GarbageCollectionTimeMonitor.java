package dk.itu.parallel.experiments;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

/**
 * A simple class to measure time spent
 * on garbage collection.
 *
 * @author Florian Biermann
 *
 */
public class GarbageCollectionTimeMonitor {

	long start; // Milliseconds.

	public GarbageCollectionTimeMonitor() {
		reset();
	}

	/**
	 * Sets time spent to zero.
	 */
	public void reset() {
		start = getGarbageCollectionTime();
	}

	/**
	 * @return Time spent on garbage collection in milliseconds.
	 */
	public long timeSpent() {
		return getGarbageCollectionTime() - start;
	}

	/**
	 * Code originates from
	 * http://stackoverflow.com/questions/13915357/measuring-time-spent-on-gc-in-jvm
	 *
	 * @return The time spent on garbage collection in milliseconds.
	 */
	private static long getGarbageCollectionTime() {
	    long collectionTime = 0;
	    for (final GarbageCollectorMXBean garbageCollectorMXBean
	    		: ManagementFactory.getGarbageCollectorMXBeans()) {
	        collectionTime += garbageCollectorMXBean.getCollectionTime();
	    }
	    return collectionTime;
	}

	public String toString() {
		return String.format("Spent %dms on garbage collection.", timeSpent());
	}
}
