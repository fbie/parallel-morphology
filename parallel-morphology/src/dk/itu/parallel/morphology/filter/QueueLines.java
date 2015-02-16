package dk.itu.parallel.morphology.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import dk.itu.parallel.morphology.filter.base.FilterThread;
import dk.itu.parallel.morphology.filter.base.QueuesFilter;
import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.CountingSort;
import dk.itu.parallel.morphology.util.Line;
import dk.itu.parallel.morphology.util.queues.Queues;
import dk.itu.parallel.morphology.util.queues.QueuesBuilder;

public class QueueLines extends QueuesFilter<Line> {

	public QueueLines(
			final UnionFind u,
			final QueuesBuilder<Line> builder) {
		super(u, builder);
	}

	public QueueLines(
			final UnionFind u,
			final int threads,
			final QueuesBuilder<Line> builder) {
		super(u, threads, builder);
	}

	@Override
	public List<FilterThread> makeThreads(final int lambda) {

		final int width = image().width;
		final int height = image().height;
		final int size = height * width;
		final AtomicReference<Queues<Line>> queues = new AtomicReference<Queues<Line>>(queues(size));
		final AtomicInteger line = new AtomicInteger(0);

		final CyclicBarrier barrier = new CyclicBarrier(threads);

		final ArrayList<FilterThread> splitLines = new ArrayList<FilterThread>();
		for (int t = 0; t < threads; ++t) {
			splitLines.add(new FilterThread() {

				@Override
				public void filter() throws Exception {
					int i = 0;
					while ((i = line.getAndIncrement()) < height) {
						final int lower = i * width;
						final int upper = lower + width;

						// Split image into lines
						final Line n = new Line(CountingSort.sort(lower, upper, image()));

						// Add all lines to upper queue.
						queues.get().upper.add(n);
					}

					/*---filter0---*/
					while (true) {
						final Queues<Line> local = queues.get();
						if (local.level < 0) // All levels have been visited.
							break;

						// Get next line from upper buffer.
						final Line l = local.upper.poll();
						if (l == null) {
							barrier.await(); // Synchronize across threads.
							local.swapAndDecrement(queues); // Swap if no lines left.
						} else {

							// Filter all pixels on current level.
							while (value(l.current()) == local.level) {
								final int c = l.current();
								uniteNeighbors(c, lambda);
								if (!l.advance()) // Advance pixel pointer if possible.
									break;
							}
							if (l.workLeft()) // Enqueue line to lower queue if work left.
								local.lower.add(l);
						}
					}
					/*---filter1---*/
					return;
				}

			});
		}
		return splitLines;
	}

	@Override
	public String name() {
		return "line";
	}

}
