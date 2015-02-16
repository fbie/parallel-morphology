package dk.itu.parallel.morphology.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import dk.itu.parallel.morphology.filter.base.Filter;
import dk.itu.parallel.morphology.filter.base.FilterThread;
import dk.itu.parallel.morphology.unionfind.UnionFind;

public class Split extends Filter {

	public Split(final UnionFind u) {
		super(u);
	}

	public Split(final UnionFind u, final int threads) {
		super(u, threads);
	}

	@Override
	public List<FilterThread> makeThreads(final int lambda) {

		// Width of a section.
		final int step = image().size() / threads + 1;

		final CyclicBarrier barrier = new CyclicBarrier(threads);
		final ArrayList<FilterThread> split = new ArrayList<FilterThread>();

		for (int t = 0; t < threads; ++t) {
			final int start = t * step;
			split.add(new FilterThread() {

				@Override
				public void filter() throws Exception {

					// Lower index of this section.
					final int lower = start;

					// Upper (exclusive) index.
					final int upper = Math.min(image().size(), start + step);

					/*---filter0---*/
					int level = image().values - 1; // Gray-values are in [k - 1, 0].
					while (level >= 0) { // Repeat while not all levels processed.
						barrier.await(); // Synchronize across threads.
						for (int c = lower; c < upper; ++c) {
							if (value(c) == level) { // Filter only pixels at current level.
								uniteNeighbors(c, lambda);
							}
						}
						--level; // Decrement gray level.
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
		return "split";
	}
}
