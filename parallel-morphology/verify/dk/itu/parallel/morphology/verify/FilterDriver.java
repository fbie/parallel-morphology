package dk.itu.parallel.morphology.verify;

import gov.nasa.jpf.symbc.Symbolic;
import gov.nasa.jpf.vm.Verify;

import dk.itu.parallel.morphology.filter.base.Filter;
import dk.itu.parallel.morphology.unionfind.Optimistic;
import dk.itu.parallel.morphology.unionfind.SequentialUnionFind;
import dk.itu.parallel.morphology.unionfind.Transactional;
import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.Line;
import dk.itu.parallel.morphology.util.image.Image;
import dk.itu.parallel.morphology.util.queues.QueuesBuilder;
import dk.itu.parallel.morphology.util.sort.LinearPixelSortBuilder;
import dk.itu.parallel.morphology.verify.filter.BlockGrayLevelVerify;
import dk.itu.parallel.morphology.verify.filter.BogusFilter;
import dk.itu.parallel.morphology.verify.filter.QueueLinesVerify;
import dk.itu.parallel.morphology.verify.filter.QueuePixelsVerify;
import dk.itu.parallel.morphology.verify.filter.SequentialVerify;
import dk.itu.parallel.morphology.verify.filter.SplitCountingVerify;
import dk.itu.parallel.morphology.verify.filter.SplitVerify;
import dk.itu.parallel.morphology.verify.unionfind.BogusUnionFind;
import dk.itu.parallel.morphology.verify.unionfind.UnionFindVerifyHarness;

/**
 * JPF Driver for verification of Filter and UnionFind implementations.
 *
 * @author Florian Biermann
 */
public class FilterDriver {

	/**
	 * Initialize a union-find algorithm by name.
	 *
	 * @param name Name of the union-find algorithm.
	 * @return The new union-find instance.
	 */
	public static UnionFind initUnionFind(final String name) {
		if (name.equals("stm")) {
			return new UnionFindVerifyHarness(new Transactional());
		} else if (name.equals("opt")) {
			return new UnionFindVerifyHarness(new Optimistic());
		} else if (name.equals("seq")) {
			return new UnionFindVerifyHarness(new SequentialUnionFind());
		} else if (name.equals("bogus")) {
			return new UnionFindVerifyHarness(new BogusUnionFind());
		}
		System.err.println("No such union-find algorithm: " + name);
		System.exit(1);
		return null;
	}

	/**
	 * Initialize a new area opening algorithm by name.
	 *
	 * @param name Name of the area opening algorithm.
	 * @param threads Number of threads to operate on.
	 * @return The new algorithm instance.
	 */
	public static Filter initFilter(final String name, final int threads) {
		final String[] args = name.split("-");

		final UnionFind u = initUnionFind(args[0]);
		if (args[1].equals("block")) {
			if (args[2].equals("bucket")) {
				return new BlockGrayLevelVerify(u, threads, LinearPixelSortBuilder.bucket());
			} else if (args[2].equals("counting")) {
				return new BlockGrayLevelVerify(u, threads, LinearPixelSortBuilder.counting());
			}
		} else if (args[1].equals("line")) {
			if (args[3].equals("msq")) {
				return new QueueLinesVerify(u, threads, QueuesBuilder.<Line>msq());
			} else if (args[3].equals("array")) {
				return new QueueLinesVerify(u, threads, QueuesBuilder.<Line>array());
			}
		} else if (args[1].equals("pixel")) {
			if (args[3].equals("msq")) {
				return new QueuePixelsVerify(u, threads, QueuesBuilder.<Long>msq());
			} else if (args[3].equals("array")) {
				return new QueuePixelsVerify(u, threads, QueuesBuilder.<Long>array());
			}
		} else if (args[1].equals("split")) {
			if (args.length == 2) {
				return new SplitVerify(u, threads);
			} else if (args.length == 3 && args[2].equals("counting")) {
				return new SplitCountingVerify(u, threads);
			}
		} else if (args[1].equals("sequential")) {
			return new SequentialVerify(u);
		} else if (args[1].equals("bogus")) {
			return new BogusFilter(u);
		}

		System.err.println("No such filtering algorithm: " + name);
		System.exit(1);
		return null;
	}

	// Change parameters here.
	// Optimally, values should be set to 3, 3, 3.
	final static int COLORS = 3;
	final static int WIDTH = 3;
	final static int HEIGHT = 3;

	/**
	 * Actually performs the verification.
	 *
	 * @param algorithm Filter algorithm to verify.
	 */
	public static void verify(final Filter algorithm) {
		final Image i = new Image(WIDTH, HEIGHT, COLORS);
		for (int p = 0; p < i.pixel.length; ++p) {
			@Symbolic("true")
			final int pixel = Verify.getInt(0, COLORS);
			i.pixel[p] = pixel;
		}
		algorithm.areaOpen(i, Verify.getInt(0, WIDTH * HEIGHT));
	}

	public static void main(final String[] args) {
		if (args.length == 0) {
			System.err.println("Required arguments: algorithm name (e.g. stm-block-counting)");
			System.exit(1);
		}
		final Filter algorithm = initFilter(args[0], 2);
		verify(algorithm);
	}

}
