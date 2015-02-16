package dk.itu.parallel.morphology.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import dk.itu.parallel.morphology.filter.base.Filter;
import dk.itu.parallel.morphology.filter.base.FilterThread;
import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.CountingSort;

public class SplitCounting extends Filter {

	public SplitCounting(final UnionFind u) {
		super(u);
	}

	public SplitCounting(final UnionFind u, final int threads) {
		super(u, threads);
	}

	@Override
	public List<FilterThread> makeThreads(final int lambda) {

		// Width of a section.
		final int step = image().size() / threads + 1;

		final CyclicBarrier barrier = new CyclicBarrier(threads);
		final ArrayList<FilterThread> split = new ArrayList<FilterThread>();

		for (int t = 0; t < threads; ++t) {

			// Lower and (exclusive) upper limits of section.
			final int lower = t * step;
			final int upper = Math.min(image().size(), lower + step);

			split.add(new FilterThread() {

				@Override
				public void filter() throws Exception {

					/*---filter0---*/
					// Pixels sorted after gray-value level.
					final int[] sorted = CountingSort.sort(lower, upper, image());
					int level = image().values - 1; // Gray-values are in [k - 1, 0].
					int p = sorted.length - 1; // Indices are in [n - 1, 0].
					int c = sorted[p];

					// Repeat while not all levels processed.
					while (level >= 0) {

						// Decrement gray level if no pixels left.
						if (c < 0 || value(c) < level) {
							--level;
							barrier.await(); // Synchronize across threads.
						} else {
							uniteNeighbors(c, lambda);

							// Get next pixel or indicate all pixels are filtered.
							c = p > 0 ? sorted[--p] : -1;
						}
					}
					/*---filter1---*/
					return;
				}
			});
		}
		return split;
	}

	@Override
	public String name() {
		return "split-counting";
	}

}
