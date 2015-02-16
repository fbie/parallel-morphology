package dk.itu.parallel.morphology.util.sort;

import dk.itu.parallel.morphology.util.image.Image;

/**
 * Interface for linear pixel sorting.
 *
 * @author Florian Biermann
 */
public abstract class LinearPixelSort {

	protected final Image img;

	public LinearPixelSort(final Image img, final int threads) {
		this.img = img;
	}

	protected int value(final int c) {
		return img.pixel[c];
	}

	/**
	 * Sort pixels in linear time.
	 *
	 * @throws Exception Exceptions might occur depending on implementation.
	 */
	public abstract void sort() throws Exception;

	/**
	 * Gets and removes the next pixel from the sorted list
	 * if and only if the internal level pointer is equal to
	 * level.
	 *
	 * @param level Gray-scale level to get next pixel for.
	 * @return The next pixel for level.
	 */
	public abstract Integer next(final int level);

}
