package dk.itu.parallel.morphology.unionfind;

import dk.itu.parallel.morphology.util.image.Image;

public abstract class UnionFind {

	protected Image im;

	/**
	 * Initialize arrays for given image, such as gray-level
	 * arrays, parent index arrays and so on.
	 *
	 * @param img Image to initialize arrays for.
	 */
	public abstract void initArrays(final Image img);

	/**
	 * Get the current gray-scale value of the pixel at index i.
	 *
	 * @param i Index of pixel.
	 * @return Gray-scale value of pixel at i.
	 */
	public final int value(final int i) {
		return im.pixel[i];
	}

	/**
	 * Convenience method to access image.
	 * @return The image this union find is initialized for.
	 */
	public final Image image() {
		return im;
	}

	/**
	 * Only for resolving if the tree does not keep the
	 * last visited pixel level as root. Behaves in most
	 * cases like value(find(i)).
	 *
	 * @param i Index of pixel.
	 * @return Gray-scale value to resolve to.
	 */
	public abstract int rootValue(final int i);

	/**
	 * Get the parent index of the pixel at index i.
	 *
	 * @param i Index of pixel.
	 * @return Parent index if i is not root, otherwise a negative number indicating its area.
	 */
	public abstract int parent(final int i);

	/**
	 * Find the root of the given element.
	 *
	 * @param c Element index to find the root of.
	 * @return The elements's root.
	 */
	public abstract int find(final int c);

	/**
	 * Unite two connected components conditionally.
	 *
	 * @param n A neighboring pixel index.
	 * @param c The current pixel index.
	 * @param lambda Minimum area.
	 */
	public abstract void union(final int n, final int c, final int lambda);

	/**
	 * Return the number of times union() had to
	 * re-try because of the roots not being roots
	 * any longer and re-set the counter to 0.
	 *
	 * @return The number of re-tries since instantiation or last call.
	 */
	public abstract long getTriesAndReset();

}
