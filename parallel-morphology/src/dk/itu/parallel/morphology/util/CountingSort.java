package dk.itu.parallel.morphology.util;

import dk.itu.parallel.morphology.util.image.Image;

/**
 * Utility class containing a static sort method.
 * The method performs counting sort on an interval
 * of pixels, using a union find instance to access
 * the gray-level values of each index.
 *
 * @author Florian Biermann
 */
public class CountingSort {

	/**
	 * Sort pixels using counting sort. This method is
	 * not re-entrant!
	 *
	 * @param lower The lower index (inclusive).
	 * @param upper The upper index (exclusive).
	 * @param u A union find instance providing access to gray-scale values.
	 * @return An array containing the pixels sorted from dark to bright.
	 */
	public static final int[] sort(final int lower, final int upper, final Image img) {

		final int[] count = new int[img.values];
		final int[] sorted = new int[upper - lower];

		// Count occurrences per pixel.
		for (int i = lower; i < upper; ++i)
			++count[img.pixel[i]];

		// Compute prefix sums.
		int total = 0;
		for (int i = 0; i < count.length; ++i) {
			final int o = count[i];
			count[i] = total;
			total += o;
		}

		// Sort pixels into respective positions.
		for (int i = lower; i < upper; ++i)
			sorted[count[img.pixel[i]]++] = i;

		return sorted;
	}

}
