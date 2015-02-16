package dk.itu.parallel.morphology.filter.base;

import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.queues.Queues;
import dk.itu.parallel.morphology.util.queues.QueuesBuilder;

/**
 * Interface class for queues-based algorithms.
 * @author Florian Biermann
 *
 * @param <T> Type contained in queues.
 */
public abstract class QueuesFilter<T> extends Filter {

	protected final QueuesBuilder<T> builder;

	public QueuesFilter(final UnionFind u, final QueuesBuilder<T> builder) {
		super(u);
		this.builder = builder;
	}

	public QueuesFilter(
			final UnionFind u,
			final int threads,
			final QueuesBuilder<T> builder) {
		super(u, threads);
		this.builder = builder;
	}

	protected Queues<T> queues(final int size) {
		return builder.create(size);
	}

	@Override
	public String toString() {
		return u + "-" + name() + "-queues-" + builder;
	}
}
