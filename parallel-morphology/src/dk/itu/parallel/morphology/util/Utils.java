package dk.itu.parallel.morphology.util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Utilities for gray-scale images.
 */
public class Utils {

	/**
	 * Convert an image to gray scale in-place.
	 * Source: http://stackoverflow.com/questions/9131678/convert-a-rgb-image-to-grayscale-image-reducing-the-memory-in-java
	 *
	 * @param img Image to be converted to gray-scale.
	 */
	public static BufferedImage toGrayScale(final BufferedImage img)
	{
		final BufferedImage grayImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		final WritableRaster grayRaster = grayImage.getRaster();
	    for (int x = 0; x < grayImage.getWidth(); ++x) {
		    for (int y = 0; y < grayImage.getHeight(); ++y) {
		        final int rgb = img.getRGB(x, y);
		        final int r = (rgb >> 16) & 0xFF;
		        final int g = (rgb >> 8) & 0xFF;
		        final int b = (rgb & 0xFF);

		        final int gray = (r + g + b) / 3;
		        grayRaster.setSample(x, y, 0, gray);
		    }
	    }
	    return grayImage;
	}

	/**
	 * Convert a gray-scale image to its negative
	 * in-place.
	 *
	 * @param img Gray-scale image to be converted to its negative.
	 */
	public static void negative(final BufferedImage img) {
		final WritableRaster raster = img.getRaster();
		for (int x = 0; x < img.getWidth(); ++x) {
		    for (int y = 0; y < img.getHeight(); ++y)
		    {
		    	raster.setSample(x, y, 0, 255 - raster.getSample(x, y, 0));
		    }
		}
	}

	/**
	 * Display a JFrame containing image.
	 *
	 * @param name Title of the frame.
	 * @param img Image to display.
	 */
	public static void showImage(final String name, final BufferedImage img) {
		final JFrame frame = new JFrame(name);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Stop program when window is closed.
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Use to display image via label and icon.
		frame.add(new JLabel(new ImageIcon(img)));
		frame.setSize(img.getWidth(), img.getHeight());
		frame.setVisible(true);
	}

	/**
	 * Compute the pixel-wise difference of two images.
	 *
	 * @param a Left hand operand.
	 * @param b Right hand operand.
	 * @return Difference image.
	 */
	public static BufferedImage diff(final BufferedImage a, final BufferedImage b) {
		assert(a.getWidth() == b.getWidth());
		assert(a.getHeight() == b.getHeight());
		assert(a.getType() == b.getType());

		final BufferedImage d = new BufferedImage(a.getWidth(), a.getHeight(), a.getType());
		final WritableRaster ra = a.getRaster();
		final WritableRaster rb = b.getRaster();
		final WritableRaster rd = d.getRaster();

		for (int x = 0; x < a.getWidth(); ++x) {
		    for (int y = 0; y < a.getHeight(); ++y)
		    {
		    	rd.setSample(x, y, 0, ra.getSample(x, y, 0) - rb.getSample(x, y, 0));
		    }
		}
		return d;
	}

	/**
	 * Deep-copy an image.
	 *
	 * @param img To be copied.
	 * @return The copy of img.
	 */
	public static BufferedImage copy(final BufferedImage img) {
		return new BufferedImage(img.getColorModel(),
				img.copyData(null),
				img.getColorModel().isAlphaPremultiplied(),
				null);
	}

	/**
	 * Binarizes a gray-scale image in memory.
	 *
	 * @param img The image to binarize. This will be changed by the function!
	 * @param threshold Pixels with a value less than this will be set to 0, otherwise to 255.
	 * @return The binarized image.
	 */
	public static BufferedImage binarize(final BufferedImage img, final int threshold) {
		final WritableRaster raster = img.getRaster();
		for (int x = 0; x < img.getWidth(); ++x) {
		    for (int y = 0; y < img.getHeight(); ++y)
		    {
		    	raster.setSample(x, y, 0, raster.getSample(x, y, 0) < threshold ? 0 : 255);
		    }
		}
		return img;
	}
}
