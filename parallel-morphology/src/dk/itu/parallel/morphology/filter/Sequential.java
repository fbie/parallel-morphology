package dk.itu.parallel.morphology.filter;

import java.util.ArrayList;
import java.util.List;

import dk.itu.parallel.morphology.filter.base.Filter;
import dk.itu.parallel.morphology.filter.base.FilterThread;
import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.CountingSort;

public class Sequential extends Filter {

	public Sequential(final UnionFind u) {
		this(u, 1);
	}

	public Sequential(final UnionFind u, final int threads) {
		super(u, threads);
	}

	@Override
	public List<FilterThread> makeThreads(final int lambda) {
		return new ArrayList<FilterThread>();
	}

	@Override
	public void filter (final int lambda) {
		/*---filter0---*/
		// Sort using counting sort.
		final int[] sorted = CountingSort.sort(0, image().size(), image());

		// Filter pixels
		for (int i = sorted.length - 1; i >= 0; --i) {
			final int c = sorted[i];
			uniteNeighbors(c, lambda);
		}
		/*---filter1---*/
	}

	@Override
	public String name() {
		return "sequential";
	}
}
