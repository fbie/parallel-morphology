package dk.itu.parallel.morphology.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

import dk.itu.parallel.morphology.filter.base.Filter;
import dk.itu.parallel.morphology.filter.base.FilterThread;
import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.CountingSort;
import dk.itu.parallel.morphology.util.Line;

public class HashedLines extends Filter {

	public HashedLines(final UnionFind u) {
		super(u);
	}

	public HashedLines(final UnionFind u, final int threads) {
		super(u, threads);
	}

	@Override
	public List<FilterThread> makeThreads(final int lambda) {
		final int width = image().width;
		final int height = image().height;

		final AtomicReferenceArray<ConcurrentLinkedQueue<Line>> lines = new AtomicReferenceArray<ConcurrentLinkedQueue<Line>>(image().values);
		final AtomicInteger value = new AtomicInteger(0);
		final AtomicInteger line = new AtomicInteger(0);

		final CyclicBarrier barrier = new CyclicBarrier(threads);

		final ArrayList<FilterThread> splitLines = new ArrayList<FilterThread>();
		for (int t = 0; t < threads; ++t) {
			splitLines.add(new FilterThread() {

				@Override
				public void filter() throws Exception {
					int i = 0;
					while ((i = value.getAndIncrement()) < image().values) {
						lines.set(i, new ConcurrentLinkedQueue<Line>());
					}
					
					i = 0;
					while ((i = line.getAndIncrement()) < height) {
						final int lower = i * width;
						final int upper = lower + width;

						// Split image into lines
						final Line l = new Line(CountingSort.sort(lower, upper, image()));

						// Add all lines to the according queue.
						final int v = value(l.current());
						lines.get(v).add(l);
					}

					int level = image().values - 1;
					/*---filter0---*/
					while (true) {
						if (level < 0) // All levels have been visited.
							break;

						// Get next line from upper buffer.
						final Line l = lines.get(level).poll();
						if (l == null) {
							barrier.await(); // Synchronize across threads.
							--level;
						} else {

							// Filter all pixels on current level.
							while (value(l.current()) == level) {
								final int c = l.current();
								uniteNeighbors(c, lambda);
								if (!l.advance()) // Advance pixel pointer if possible.
									break;
							}

							if (l.workLeft()) // Add line to next gray-level queue if work left.
								lines.get(value(l.current())).add(l);
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
		return "hashed-lines";
	}

}
