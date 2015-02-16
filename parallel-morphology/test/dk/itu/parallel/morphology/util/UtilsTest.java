package dk.itu.parallel.morphology.util;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import dk.itu.parallel.morphology.util.Utils;

public class UtilsTest {

	BufferedImage src;

	@Before
	public void setUp() {
		src = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);
		final WritableRaster raster = src.getRaster();
		final Random rand = new Random();
		for (int y = 0; y < src.getHeight(); ++y) {
			for (int x = 0; x < src.getWidth(); ++x) {
				raster.setSample(x, y, 0, rand.nextInt(256));
			}
		}
	}
	@Test
	public void testToGrayScale() {
		fail("Not yet implemented");
	}

	@Test
	public void testNegative() {
		final BufferedImage neg = Utils.copy(src);

		Utils.negative(neg);
		Utils.negative(neg);

		final WritableRaster sr = src.getRaster();
		final WritableRaster nr = neg.getRaster();

		for (int y = 0; y < src.getHeight(); ++y) {
			for (int x = 0; x < src.getWidth(); ++x) {
				assertEquals(sr.getSample(x, y, 0), nr.getSample(x, y, 0));
			}
		}

	}

	@Test
	public void testDiff() {
		fail("Not yet implemented");
	}

}
