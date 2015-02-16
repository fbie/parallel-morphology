package dk.itu.parallel.morphology.util.sort;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

import dk.itu.parallel.morphology.util.image.Image;

/**
 * Bucket-sorting implementation of linear pixel sort.
 * The class uses ConcurrentLinkedQueue as queue implementations
 * to be thread-safe. Sorting and retrieval are wait-free.
 *
 * @author Florian Biermann
 */
public class BucketPixelSort extends LinearPixelSort {

	final AtomicReferenceArray<ConcurrentLinkedQueue<Integer>> bucket;
	final AtomicInteger i;

	public BucketPixelSort(final Image img, final int threads) {
		super(img, threads);
		bucket = new AtomicReferenceArray<ConcurrentLinkedQueue<Integer>>(img.values);
		i = new AtomicInteger(0);
	}

	@Override
	public void sort() {
		int p = i.getAndIncrement();
		while (p < img.size()) {
			final int v = value(p);

			// Initialize on the fly.
			if (bucket.get(v) == null)
				bucket.compareAndSet(v, null, new ConcurrentLinkedQueue<Integer>());

			bucket.get(v).add(p);
			p = i.getAndIncrement();
		}
	}

	@Override
	public Integer next(final int level) {
		final ConcurrentLinkedQueue<Integer> b = bucket.get(level);
		return b == null ? null : b.poll();
	}
}
