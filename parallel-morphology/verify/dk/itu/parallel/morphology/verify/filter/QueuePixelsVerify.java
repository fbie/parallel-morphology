package dk.itu.parallel.morphology.verify.filter;

import gov.nasa.jpf.vm.Verify;
import dk.itu.parallel.morphology.filter.QueuePixels;
import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.image.Image;
import dk.itu.parallel.morphology.util.queues.QueuesBuilder;
import dk.itu.parallel.morphology.verify.predicate.MonotonicDecreasingGrayValueHelper;

public class QueuePixelsVerify extends QueuePixels {

	public QueuePixelsVerify(final UnionFind u, final QueuesBuilder<Long> builder) {
		super(u, builder);
	}

	public QueuePixelsVerify(final UnionFind u, final int threads,
			final QueuesBuilder<Long> builder) {
		super(u, threads, builder);
	}

	@Override
	public void uniteNeighbors(final int c, final int lambda) {

		// Dismiss invalid cases.
		Verify.ignoreIf(c < 0);
		Verify.ignoreIf(lambda < 0);
		Verify.ignoreIf(c >= image().pixel.length);
		Verify.ignoreIf(lambda >= image().pixel.length);

		MonotonicDecreasingGrayValueHelper.verify(value(c));
		super.uniteNeighbors(c, lambda);
		MonotonicDecreasingGrayValueHelper.verify(value(c));
	}

	@Override
	public void areaOpen(final Image image, final int lambda) {

		// Dismiss invalid cases.
		Verify.ignoreIf(image == null);
		Verify.ignoreIf(image.pixel == null);
		Verify.ignoreIf(image.pixel.length == 0);
		Verify.ignoreIf(lambda < 0);
		Verify.ignoreIf(lambda >= image.pixel.length);

		super.areaOpen(image, lambda);
	}

}
