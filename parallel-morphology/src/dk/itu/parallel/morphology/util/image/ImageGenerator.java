package dk.itu.parallel.morphology.util.image;

import java.util.Random;

/**
 * Simple random image generator.
 *
 * @author Florian Biermann
 */
public class ImageGenerator {

	/**
	 * Generates image based on a Random instance.
	 *
	 * @param rand The random instance to use for data generation.
	 * @return A random image.
	 */
	private static Image get(final Random rand) {
		final int width  = (rand.nextInt(1024)) + 1;
		final int height = (rand.nextInt(1024)) + 1;

		final Image img = new Image(width, height, 256);
		for (int i = 0; i < img.pixel.length; ++i)
			img.pixel[i] = rand.nextInt(256);
		return img;
	}

	/**
	 * Generates a fully random image.
	 *
	 * @return Fully random image.
	 */
	public static Image get() {
		return get(new Random());
	}

	/**
	 * Generates a pseudo-random image for a
	 * given seed.
	 *
	 * @param seed Seed to use when generating the image.
	 * @return Pseudo-random image.
	 */
	public static Image get(final int seed) {
		return get(new Random(seed));
	}

}
