package dk.itu.parallel.morphology.util.sort;

import dk.itu.parallel.morphology.util.image.Image;

/**
 * Factory class for thread-safe concurrent sorting algorithms.
 *
 * @author Florian Biermann
 */
public abstract class LinearPixelSortBuilder {

	/**
	 * Factory function to build a new instance of
	 * a LinearPixelSort implementation-
	 *
	 * @param size Number of pixels.
	 * @param values Number of gray-scale levels.
	 * @param UnionFind Union-find instance used.
	 * @param threads Number of threads.
	 *
	 * @return A new instance of an implementation of LinearPixelSort.
	 */
	public abstract LinearPixelSort create(final Image img, final int threads);

	/**
	 * @return A descriptive name for the type this builder generates.
	 */
	public abstract String name();

	@Override
	public String toString() {
		return name();
	}

	/**
	 * @return A builder creating instances of BucketPixelSort.
	 */
	public static LinearPixelSortBuilder bucket() {
		return new LinearPixelSortBuilder() {

			@Override
			public String name() {
				return "bucket";
			}

			@Override
			public LinearPixelSort create(final Image img, final int threads) {
				return new BucketPixelSort(img, threads);
			}
		};
	}

	/**
	 * @return A builder creating instances of CountingPixelSort.
	 */
	public static LinearPixelSortBuilder counting() {
		return new LinearPixelSortBuilder() {

			@Override
			public String name() {
				return "counting";
			}

			@Override
			public LinearPixelSort create(final Image img, final int threads) {
				return new CountingPixelSort(img, threads);
			}
		};
	}
}
