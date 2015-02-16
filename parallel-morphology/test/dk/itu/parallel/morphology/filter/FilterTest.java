package dk.itu.parallel.morphology.filter;

import static org.junit.Assert.*;
import org.junit.Before;

import dk.itu.parallel.morphology.filter.Sequential;
import dk.itu.parallel.morphology.filter.base.Filter;
import dk.itu.parallel.morphology.unionfind.SequentialUnionFind;
import dk.itu.parallel.morphology.util.image.Image;
import dk.itu.parallel.morphology.util.image.ImageGenerator;

public abstract class FilterTest {

	Image img;

	@Before
	public void setUp() throws Exception {
		img = ImageGenerator.get();
	}

	public static void assertImageEquals(final Image expected, final Image actual) {
		assertEquals(expected.height, actual.height);
		assertEquals(expected.width, actual.width);

		for (int i = 0; i < expected.pixel.length; ++i) {
			assertEquals(expected.pixel[i], actual.pixel[i]);
		}
	}

	public void testFilter(final Filter algorithm) {
		final Sequential seq = new Sequential(new SequentialUnionFind());

		for (int lambda = 100; lambda < 5000; lambda *= 2) {
			System.err.println("Computing area closing using "
					+ algorithm
					+ " for lambda = "
					+ lambda);

			final Image expected = new Image(img);
			seq.areaOpen(expected, lambda);

			final Image actual = new Image(img);
			algorithm.areaOpen(actual, lambda);

			assertImageEquals(expected, actual);
		}

	}
}

