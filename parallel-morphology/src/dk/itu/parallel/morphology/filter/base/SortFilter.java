package dk.itu.parallel.morphology.filter.base;

import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.sort.LinearPixelSortBuilder;

/**
 * Interface class for sorting based algorithms.
 *
 * @author Florian Biermann
 */
public abstract class SortFilter extends Filter {

	protected final LinearPixelSortBuilder builder;

	public SortFilter(final UnionFind u, final LinearPixelSortBuilder builder) {
		super(u);
		this.builder = builder;
	}

	public SortFilter(
			final UnionFind u,
			final int threads,
			final LinearPixelSortBuilder builder) {
		super(u, threads);
		this.builder = builder;
	}

	@Override
	public String toString() {
		return u + "-" + name() + "-" + builder;
	}
}
