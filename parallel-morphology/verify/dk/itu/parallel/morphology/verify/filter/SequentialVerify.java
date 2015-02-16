package dk.itu.parallel.morphology.verify.filter;

import gov.nasa.jpf.vm.Verify;
import dk.itu.parallel.morphology.filter.Sequential;
import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.image.Image;
import dk.itu.parallel.morphology.verify.predicate.MonotonicDecreasingGrayValueHelper;

/**
 * Verification harness for sequential area opening. Since the
 * worker threads internally call the filter's uniteNeighbors
 * method, encapsulation does not work here and we need to
 * resort to explicit inheritance.
 *
 * @author Florian Biermann
 */
public final class SequentialVerify extends Sequential {

	public SequentialVerify(final UnionFind u) {
		super(u);
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
