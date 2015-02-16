package dk.itu.parallel.morphology.verify.filter;

import gov.nasa.jpf.vm.Verify;

import java.util.List;

import dk.itu.parallel.morphology.filter.base.Filter;
import dk.itu.parallel.morphology.filter.base.FilterThread;
import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.CountingSort;
import dk.itu.parallel.morphology.util.image.Image;
import dk.itu.parallel.morphology.verify.predicate.MonotonicDecreasingGrayValueHelper;

/**
 * A bogus filter to check that wrong implementations are found.
 *
 * @author Florian Biermann
 */
public class BogusFilter extends Filter {

	public BogusFilter(final UnionFind u) {
		super(u);
	}

	public BogusFilter(final UnionFind u, final int threads) {
		super(u, threads);
	}

	@Override
	public void filter(final int lambda) {

		// Generate new input, disregard the old one.
		int[] pixels = CountingSort.sort(0, image().pixel.length, image());

		// Pixels are filtered in the wrong order!
		for (final int p : pixels) {
			uniteNeighbors(p, lambda);
		}
	}

	@Override
	public List<FilterThread> makeThreads(int lambda) {
		return null;
	}

	@Override
	public String name() {
		return "bogus";
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
