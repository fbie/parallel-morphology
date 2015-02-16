package dk.itu.parallel.morphology.util.sort;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import dk.itu.parallel.morphology.util.image.Image;

/**
 * A counting sort implementation for pixels. Sorting is
 * blocking using barriers while retrieval is wait-free.
 *
 * @author Florian Biermann
 */
public class CountingPixelSort extends LinearPixelSort {

	final AtomicIntegerArray pixels;
	final AtomicInteger i;
	final AtomicInteger next;

	final AtomicIntegerArray count;
	final AtomicIntegerArray start;

	final CyclicBarrier barrier;

	public CountingPixelSort(final Image img, final int threads) {
		super(img, threads);

		// Counting variable during sorting.
		i = new AtomicInteger(0);
		next = new AtomicInteger(img.size() - 1);

		// Sorted pixel array.
		pixels = new AtomicIntegerArray(img.size());

		// Stores number of pixels per level
		count = new AtomicIntegerArray(img.values);

		// Keeps level start indices.
		start = new AtomicIntegerArray(img.values);
		barrier = new CyclicBarrier(threads);
	}

	@Override
	public void sort() throws Exception {

		// Count how many pixels of each value there are.
		int c = 0;
		while ((c = i.getAndIncrement()) < pixels.length()) {
			count.incrementAndGet(value(c));
		}
		waitAndReset();

		// Compute start indices.
		while ((c = i.getAndIncrement()) < start.length()) {
			for (int t = 0; t < c; ++t)
				start.addAndGet(c, count.get(t));
		}
		waitAndReset();

		// Sort pixels into array at their respective
		// starting points.
		while ((c = i.getAndIncrement()) < pixels.length()) {
			pixels.set(start.getAndIncrement(value(c)), c);
		}
	}

	private void waitAndReset() throws InterruptedException, BrokenBarrierException {
		barrier.await();
		final int exp = i.get();
		i.compareAndSet(exp, 0);
		barrier.await();
	}

	@Override
	public Integer next(final int level) {
		while (true) {
			final int n = next.get();
			if (start.get(level) - count.get(level) <= n) {
				if (next.compareAndSet(n, n - 1))
					return pixels.get(n);
			} else {
				return null;
			}
		}
	}

}
