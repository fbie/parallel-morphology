package dk.itu.parallel.morphology.util.sort;

import static org.junit.Assert.*;

import gov.nasa.jpf.vm.Verify;

import org.junit.Before;
import org.junit.Test;

import dk.itu.parallel.morphology.util.image.Image;
import dk.itu.parallel.morphology.util.sort.BucketPixelSort;
import dk.itu.parallel.morphology.util.sort.CountingPixelSort;
import dk.itu.parallel.morphology.util.sort.LinearPixelSort;

public class LinearPixelSortTest {

	Image img;
	LinearPixelSort sort;
	int threads;

	@Before
	public void setUp() throws Exception {
		img = new Image(Verify.getInt(0, 1024), Verify.getInt(0, 1024), 256);
		for (int i = 0; i < img.pixel.length; ++i)
			img.pixel[i] = Verify.getInt(0, img.values);
		threads = 3;
	}

	public void testLinearPixelSort() {

		// Initialize threads.
		final Thread[] workers = new Thread[threads];
		for (int i = 0; i < workers.length; ++i) {
			workers[i] = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						sort.sort();
					} catch (Exception e) {
						fail(e.toString());
					}
				}
			});
		}

		// Execute threads.
		for (final Thread w : workers)
			w.start();

		// Terminate threads.
		for (final Thread w : workers) {
			try {
				w.join();
			} catch (InterruptedException e) {
				fail(e.toString());
			}
		}

		// Count, how many pixels there are per level.
		final int size = img.width * img.height;
		final int[] count = new int[256];
		for (int i = 0; i < size; ++i)
			count[img.pixel[i]]++;

		// Assert that the sorting algorithm returns the
		// correct amount of pixels per level.
		int level = 255;
		while (level >= 0) {
			int c = 0;
			while (sort.next(level) != null)
				++c;
			assertEquals(count[level], c);
			--level;
		}
	}

	@Test
	public void testBucketSort() {
		sort = new BucketPixelSort(img, threads);
		testLinearPixelSort();
	}

	@Test
	public void testCountingSort() {
		sort = new CountingPixelSort(img, threads);
		testLinearPixelSort();
	}

}
