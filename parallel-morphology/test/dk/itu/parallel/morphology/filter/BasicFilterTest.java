package dk.itu.parallel.morphology.filter;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.itu.parallel.morphology.filter.base.Filter;
import dk.itu.parallel.morphology.unionfind.SequentialUnionFind;
import dk.itu.parallel.morphology.util.image.Image;

public class BasicFilterTest {

	public void testIndex(int idx, int length, int[] expected) {
		Filter f = new Sequential(new SequentialUnionFind());
		f.u.initArrays(new Image(10, 10, 256));

		int[] n = f.neighbors(idx);
		assertEquals(length, n.length);
		for (int i = 0; i < n.length; ++i) {
			assertEquals(expected[i], n[i]);
		}
	}

	@Test
	public void testNeighbors() {
		// Upper left corner
		testIndex(0, 3, new int[]{1, 10, 11});

		// Upper right corner
		testIndex(9, 3, new int[]{8, 18, 19});

		// Lower left corner
		testIndex(90, 3, new int[]{80, 81, 91});

		// Lower right corner
		testIndex(99, 3, new int[]{88, 89, 98});

		// Upper border
		testIndex(1, 5, new int[]{0, 2, 10, 11, 12});

		// Lower border
		testIndex(91, 5, new int[]{80, 81, 82, 90, 92});

		// Left border
		testIndex(50, 5, new int[]{40, 41, 51, 60, 61});

		// Right border
		testIndex(59, 5, new int[]{48, 49, 58, 68, 69});

		// Inner pixel
		testIndex(11, 8, new int[]{0, 1, 2, 10, 12, 20, 21, 22});
	}

}
