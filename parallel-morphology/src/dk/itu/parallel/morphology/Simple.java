package dk.itu.parallel.morphology;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import dk.itu.parallel.morphology.filter.Sequential;
import dk.itu.parallel.morphology.filter.base.Filter;
import dk.itu.parallel.morphology.unionfind.Optimistic;
import dk.itu.parallel.morphology.util.Utils;
import dk.itu.parallel.morphology.util.image.Image;
import dk.itu.parallel.morphology.util.image.ImageUtils;

/**
 * Simple program to demonstrate the effects of area opening.
 */
public class Simple {

	public static Image close(final Filter algorithm, final Image img, final int lambda) {
		final Image gray = new Image(img);
		algorithm.areaClose(gray, lambda);
		return gray;
	}

	public static void showClose(final Filter algorithm, final Image img, final int lambda) {
		final Image gray = close(algorithm, img, lambda);
		Utils.showImage("closing " + algorithm + " lambda = " + lambda, ImageUtils.toBufferedImage(gray));
	}

	public static Image open(final Filter algorithm, final Image img, final int lambda) {
		final Image gray = new Image(img);
		algorithm.areaOpen(gray, lambda);
		return gray;
	}

	public static void showOpen(final Filter algorithm, final Image img, final int lambda) {
		final Image gray = open(algorithm, img, lambda);
		Utils.showImage("opening " + algorithm + " lambda = " + lambda, ImageUtils.toBufferedImage(gray));
	}

	public static void main(String[] args) {

		if (args.length != 3) {
			System.err.println("Required parameters: path-to-image, lambda, target-path.");
			System.exit(1);
		}

		final String imgSource = args[0];
		final String imgTarget = args[2];
		final int lambda = Integer.parseInt(args[1]);
		final String target = imgTarget + "/area-closing-" + lambda + ".jpg";
		System.out.println("Storing image to " + target);
		try {
			// Read
			final BufferedImage img = ImageIO.read(new File(imgSource));
			final Image gray = ImageUtils.toImage(Utils.toGrayScale(img));

			// Compute
			final Image closing = close(new Sequential(new Optimistic()), gray, lambda);

			// Write
			final BufferedImage bi = ImageUtils.toBufferedImage(closing);
			final File outputfile = new File(target);
			ImageIO.write(bi, "jpg", outputfile);


			showClose(new Sequential(new Optimistic()), gray, lambda);
			while (true)
				Thread.sleep(100);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}

}
