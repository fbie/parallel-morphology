package dk.itu.parallel.unionfind;


import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import dk.itu.parallel.unionfind.IUnionFind;

public abstract class IUnionFindTest {

	protected static int size = 10;
	protected IUnionFind algorithm;

	@Test
	public void testFind() {
		assertEquals(algorithm.find(0), 0);
		assertEquals(algorithm.find(1), 1);
		assertEquals(algorithm.find(2), 2);
	}

	@Test
	public void testUnion() {
		algorithm.union(1, 2);
		assertEquals(algorithm.find(1), algorithm.find(2));

		algorithm.union(2, 3);
		assertEquals(algorithm.find(1), algorithm.find(2));
		assertEquals(algorithm.find(1), algorithm.find(3));
		assertEquals(algorithm.find(2), algorithm.find(3));

		algorithm.union(1, 4);
		assertEquals(algorithm.find(1), algorithm.find(2));
		assertEquals(algorithm.find(1), algorithm.find(3));
		assertEquals(algorithm.find(2), algorithm.find(3));
		assertEquals(algorithm.find(1), algorithm.find(4));
		assertEquals(algorithm.find(2), algorithm.find(4));
		assertEquals(algorithm.find(3), algorithm.find(4));
	}

	@Test
	public void testParallel() {

		final int[] numbers = new int[size];
		for (int i = 0; i < numbers.length; ++i) {
			numbers[i] = i;
		}

		// Populate threads
		{
			final Thread[] threads = new Thread[7];
			for (int i = 0; i < threads.length; ++i) {
				Collections.shuffle(Arrays.asList(numbers));
				threads[i] = new Thread() {

					@Override
					public void run() {
						for (int i = 0; i < numbers.length - 1; ++i) {
							algorithm.union(numbers[i], numbers[i + 1]);
						}
					}
				};
			}

			// Start all threads.
			for (Thread t: threads) {
				t.start();
			}

			// Block until all threads are joined.
			for (Thread t: threads) {
				try {
					t.join();
				} catch (Exception e) {
					fail(e.getMessage());
				}
			}
		}

		final int root = algorithm.find(0);
		for (int i : numbers) {
			assertEquals(algorithm.find(i), root);
		}
	}
}
