package dk.itu.parallel.morphology.filter.base;

import java.util.List;

import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.image.Image;

/**
 * Base class for morphological operations on
 * gray-scale images.
 *
 * @author Florian Biermann
 */
public abstract class Filter {

	public final UnionFind u;
	public final int threads;
	protected List<FilterThread> workers;

	/**
	 * Default constructor for one thread and
	 * some union-find algorithm.
	 *
	 * @param u Union-find algorithm to use when filtering.
	 */
	public Filter(final UnionFind u) {
		// Better safe than sorry:
		// only use one thread by default.
		this(u, 1);
	}

	/**
	 * Constructor for a custom number of threads
	 * and some union-find algorithm.
	 *
	 * @param u Union-find algorithm to use when filtering.
	 * @param threads Number of threads to run.
	 */
	public Filter(final UnionFind u, final int threads) {
		this.u = u;
		this.threads = threads;
	}

	/**
	 * Compute the index of a pixel.
	 *
	 * @param x coordinate of the pixel.
	 * @param y coordinate of the pixel-
	 * @param width of the image.
	 * @return Index of the pixel in pixel array.
	 */
	public static final int index(int x, int y, int width) {
		return x + y * width;
	}

	public final Image image() {
		return u.image();
	}

	/**
	 * Short-hand for u.value(i). Introduced because it
	 * looks nicer when the code is presented.
	 *
	 * @param i Index to get value for.
	 * @return Same as u.value(i).
	 */
	public final int value(final int i) {
		return u.value(i);
	}

	/**
	 * Short-hand for u.union(n, c, lambda). Introduced
	 * because it looks nicer when the code is presented.
	 *
	 * @param n Neighbor pixel of c.
	 * @param c Current pixel.
	 * @param lambda Maximum area.
	 */
	public final void union(final int n, final int c, final int lambda) {
		u.union(n, c, lambda);
	}

	/**
	 * Unite neighbors with the current pixel c.
	 *
	 * @param c Currently filtered pixel.
	 * @param lambda Minimum area.
	 */
	/*---uniteNeighbors0---*/
	public void uniteNeighbors(final int c, final int lambda) {
		for (final int n : neighbors(c)) {
			if (value(c) <= value(n))
				union(n, c, lambda);
		}
	}
	/*---uniteNeighbors1---*/

	/**
	 * Short-hand for u.find(c). Introduced because it
	 * looks nicer when the code is presented.
	 *
	 * @param c Current pixel.
	 * @return Root of the c.
	 */
	public final int find(final int c) {
		return u.find(c);
	}

	/**
	 * Compute all neighbors in scan-line order of the given index.
	 *
	 * @param idx Index to compute neighbors of.
	 * @param height of the image.
	 * @param width of the image.
	 * @return An array containing all neighbor indices.
	 */
	public final int[] neighbors(int idx) {

		final int x = idx % image().width;
		final int y = idx / image().width;

		// Find start indices.
		final int beginX = Math.max(x - 1, 0);
		final int endX   = Math.min(x + 1, image().width - 1);
		final int beginY = Math.max(y - 1, 0);
		final int endY   = Math.min(y + 1, image().height - 1);

		// Compute the size of the rectangle
		// without this pixel.
		final int[] neighbors = new int[(endX - beginX + 1) * (endY - beginY + 1) - 1];

		// Populate neighbor indices.
		int i = 0;
		for (int ny = beginY; ny <= endY; ++ny) {
			for (int nx = beginX; nx <= endX; ++nx) {
				int n = index(nx, ny, image().width);
				if (n != idx)
					neighbors[i++] = n;
			}
		}

		return neighbors;
	}

	/**
	 * Perform area opening on the provided image
	 * for lambda.
	 *
	 * @param img Gray-scale image.
	 * @param lambda Minimum area.
	 */
	public void areaOpen(final Image img, final int lambda) {
		initialize(img, lambda);

		// Compute connected sets.
		filter(lambda);

		// Resolve values and paint them into
		// the image.
		for (int i = 0; i < img.pixel.length; ++i) {
			if (u.parent(i) > 0) {
				img.pixel[i] = u.rootValue(i);
			}
		}
	}

	/**
	 * Perform area closing on the provided image
	 * for lambda.
	 *
	 * @param img Gray-scale image.
	 * @param lambda Minimum area.
	 */
	public final void areaClose(final Image img, final int lambda) {
		img.negate();
		areaOpen(img, lambda);
		img.negate();
	}

	/**
	 * Initializes all threads and returns them as a list.
	 *
	 * @param lambda Minimum area.
	 * @return Threads to be executed in parallel.
	 */
	public abstract List<FilterThread> makeThreads(final int lambda);

	/**
	 * Initializes all worker threads by calling makeThreads().
	 *
	 * @param lambda Minimum area.
	 */
	public void initialize(final Image image, final int lambda) {
		u.initArrays(image);
		workers = makeThreads(lambda);
	}

	/**
	 * Build connected components of size less than lambda
	 * from the pixel array.
	 *
	 * @param lambda Minimum area.
	 */
	public void filter(final int lambda) {
		for (final Thread t : workers)
			t.start();

		try {
			for (final Thread t : workers)
				t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return The name of this filter.
	 */
	public abstract String name();

	@Override
	public String toString() {
		return u + "-" + name();
	}
}
