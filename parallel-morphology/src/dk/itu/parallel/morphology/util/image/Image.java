package dk.itu.parallel.morphology.util.image;

/**
 * An extremely simple image data container. It
 * consists only of an array of values and a given
 * width and height. There are two convenience functions
 * to access pixels via (x,y) coordinates.
 *
 * This class is not optimized for anything. It is only
 * a very simple image representation to avoid using
 * java.awt.BufferedImage in core sections of the code.
 *
 * @author Florian Biermann
 *
 */
public class Image {

	public final int width;
	public final int height;

	public final int values;

	public final int[] pixel;

	public Image(final int width, final int height, final int values, final int[] pixel) {
		assert (width * height == pixel.length);

		this.width = width;
		this.height = height;
		this.values = values;
		this.pixel = pixel;
	}

	public Image(final int width, final int height, final int values) {
		this.width = width;
		this.height = height;
		this.values = values;

		// An empty image is basically black.
		pixel = new int[width * height];
	}

	/**
	 * Deep-copy constructor.
	 *
	 * @param other Another image to copy from.
	 */
	public Image(final Image other) {
		this(other.width, other.height, other.values);
		for (int i = 0; i < pixel.length; ++i) {
			pixel[i] = other.pixel[i];
		}
	}

	/**
	 * @return The size of this image in pixels.
	 */
	public final int size() {
		return pixel.length;
	}

	/**
	 * Gets the pixel value at the given x,y coordinates.
	 * Throws a ArrayOutOfBounds exception if x,y lies
	 * outside the image.
	 *
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @return The 32-bit integer pixel value at x,y.
	 */
	public final int get(final int x, final int y) {
		return pixel[y * width + x];
	}

	/**
	 * Set the pixel value at the given x,y coordinates.
	 * Throws a ArrayOutOfBounds exception if x,y lies
	 * outside the image.
	 *
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param value The new 32-bit integer pixel value at x,y.
	 */
	public final void set(final int x, final int y, final int value) {
		this.pixel[y * width + x] = value;
	}

	/**
	 * Compute a negative of this image in-place.
	 * Returns this for convenience. Linear-time
	 * operation.
	 *
	 * @return This image.
	 */
	public final Image negate() {
		for (int i = 0; i < pixel.length; ++i) {
			pixel[i] = 255 - pixel[i];
		}
		return this;
	}
}
