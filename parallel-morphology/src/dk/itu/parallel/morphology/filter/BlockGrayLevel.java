package dk.itu.parallel.morphology.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import dk.itu.parallel.morphology.filter.base.FilterThread;
import dk.itu.parallel.morphology.filter.base.SortFilter;
import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.sort.LinearPixelSort;
import dk.itu.parallel.morphology.util.sort.LinearPixelSortBuilder;

public class BlockGrayLevel extends SortFilter {

	public BlockGrayLevel(final UnionFind u, final LinearPixelSortBuilder builder) {
		super(u, builder);
	}

	public BlockGrayLevel(
			final UnionFind u,
			final int threads,
			final LinearPixelSortBuilder builder) {
		super(u, threads, builder);
	}

	@Override
	public List<FilterThread> makeThreads(final int lambda) {

		// Buckets containing pixel indices.
		final LinearPixelSort sorted = builder.create(image(), threads);
		final CyclicBarrier barrier = new CyclicBarrier(threads);

		// Initialize the buckets concurrently.
		final ArrayList<FilterThread> blockGrayLevel = new ArrayList<FilterThread>(threads);

		for (int t = 0; t < threads; ++t) {
			blockGrayLevel.add(new FilterThread() {

				@Override
				public void filter() throws Exception {

					/*---filter0---*/
					// sorted is of type LinearPixelSort.
					sorted.sort(); // Sort pixels in parallel.
					int level = image().values - 1; // Gray-values are in [k - 1, 0].
					while (level >= 0) {
						barrier.await(); // Synchronize across threads.

						// Get initial pixel for current level.
						Integer c = sorted.next(level);
						while (c != null) {
							uniteNeighbors(c, lambda);
							c = sorted.next(level);
						}
						--level;
					}
					/*---filter1---*/
					return;
				}
			});
		}
		return blockGrayLevel;
	}

	@Override
	public String name() {
		return "block";
	}
}
