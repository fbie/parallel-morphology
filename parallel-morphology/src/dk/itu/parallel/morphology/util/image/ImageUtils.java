package dk.itu.parallel.morphology.util.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * A collection of functions converting Image instances
 * from and to BufferedImage instances.
 *
 * @author Florian Biermann
 */
public class ImageUtils {

	/**
	 * Constructs an Image instance from a BufferedImage.
	 * Data will not be implicitly shared.
	 *
	 * @param img Source image.
	 * @return An independent Image representation.
	 */
	public static Image toImage(final BufferedImage img) {
		final Image res = new Image(img.getWidth(), img.getHeight(), 256);

		final WritableRaster raster = img.getRaster();
		for (int x = 0; x < img.getWidth(); ++x) {
			for (int y = 0; y < img.getHeight(); ++y) {
				res.set(x, y, raster.getSample(x, y, 0));
			}
		}
		return res;
	}

	/**
	 * Constructs a BufferedImage instance from an Image.
	 * Data will not be implicitly shared.
	 *
	 * @param img Source image.
	 * @return An independent BufferedImage representation.
	 */
	public static BufferedImage toBufferedImage(final Image img) {
		final BufferedImage res = new BufferedImage(img.width, img.height, BufferedImage.TYPE_BYTE_GRAY);
		writeTo(img, res);
		return res;
	}

	/**
	 * Copy data from an Image to a BufferedImage.
	 *
	 * @param src Source image.
	 * @param tgt Target image.
	 */
	public static void writeTo(final Image src, final BufferedImage tgt) {
		final WritableRaster raster = tgt.getRaster();
		for (int x = 0; x < tgt.getWidth(); ++x) {
			for (int y = 0; y < tgt.getHeight(); ++y) {
				raster.setSample(x, y, 0, src.get(x, y));
			}
		}
	}
}
