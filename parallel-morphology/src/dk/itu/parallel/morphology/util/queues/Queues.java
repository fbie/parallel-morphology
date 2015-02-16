package dk.itu.parallel.morphology.util.queues;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Maintains two queues and a level pointer.
 * The queues keep track of not-yet processed
 * pixels in some form.
 *
 * By maintaining queues and level on the same object,
 * a single compare-and-swap is sufficient to swap queues
 * and to decrease the current gray level.
 *
 * @author Florian Biermann
 *
 * @param <T> Usually pixel (Integer) or lines.
 */
/*---queues0---*/
public class Queues<T> {

	// Queues containing work items.
	public final Queue<T> upper;
	public final Queue<T> lower;

	// Current level pointer.
	public final int level;

	/*---queues1---*/
	public static <T> Queues<T> newMSQ() {
		return new Queues<T>(
				new ConcurrentLinkedQueue<T>(),
				new ConcurrentLinkedQueue<T>(),
				255);
	}

	public static <T> Queues<T> newArray(final int size) {
		return new Queues<T>(
				new ConcurrentArrayQueue<T>(size),
				new ConcurrentArrayQueue<T>(size),
				255);
	}

	public Queues(
			final Queue<T> upper,
			final Queue<T> lower,
			final int level) {

		this.upper = upper;
		this.lower = lower;
		this.level = level;
	}

	/**
	 * Swap upper and lower queues of a reference if
	 * the reference's state matches the last known.
	 * Also decrement the level of queues.
	 *
	 * @param ref Atomic reference to a Queues instance.
	 * @param last Last observed state of ref.
	 */
	/*---swap0---*/
	public boolean swapAndDecrement(
			final AtomicReference<Queues<T>> ref) {

		// Swap queues from last observation, decrement level.
		final Queues<T> swap = new Queues<T>(lower, upper, level - 1);
		return ref.compareAndSet(this, swap);
	}
	/*---swap1---*/
	/*---queues2---*/
}
/*---queues3---*/
