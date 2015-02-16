package dk.itu.parallel.morphology.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import dk.itu.parallel.morphology.filter.base.FilterThread;
import dk.itu.parallel.morphology.filter.base.QueuesFilter;
import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.queues.Queues;
import dk.itu.parallel.morphology.util.queues.QueuesBuilder;

/**
 * A wait-free area opening filter using
 * queues pixel queues.
 *
 * @author Florian Biermann
 */
public class QueuePixels extends QueuesFilter<Long> {

	public QueuePixels(
			final UnionFind u,
			final QueuesBuilder<Long> builder) {
		super(u, builder);
	}

	public QueuePixels(
			final UnionFind u,
			final int threads,
			final QueuesBuilder<Long> builder) {
		super(u, threads, builder);
	}

	@Override
	public List<FilterThread> makeThreads(final int lambda) {

		final AtomicReference<Queues<Long>> queues = new AtomicReference<Queues<Long>>(queues(image().size()));
		final CyclicBarrier barrier = new CyclicBarrier(threads);
		final AtomicLong i = new AtomicLong(0);

		final ArrayList<FilterThread> queue = new ArrayList<FilterThread>();
		for (int t = 0; t < threads; ++t) {
			queue.add(new FilterThread() {

				@Override
				public void filter() throws Exception {
					for (long c = i.getAndIncrement(); c < image().size(); c = i.getAndIncrement()) {
						queues.get().upper.add(c);
					}

					// Synchronize initialization.
					barrier.await();

					/*---filter0---*/
					while (true) {
						final Queues<Long> local = queues.get();
						if (local.level < 0) // All levels have been visited.
							break;

						// Get next pixel from upper buffer.
						final Long c = local.upper.poll();
						if (c == null) {
							barrier.await(); // Synchronize threads.
							local.swapAndDecrement(queues); // Swap if no pixels left.
						} else {
							// Filter pixel only if it is at current level.
							if (value(c.intValue()) == local.level) {
								uniteNeighbors(c.intValue(), lambda);
							} else {
								local.lower.add(c); // Enqueue pixel to lower buffer.
							}
						}
					}
					/*---filter1---*/
					return;
				}
			});
		}
		return queue;
	}

	@Override
	public String name() {
		return "pixel";
	}
}
