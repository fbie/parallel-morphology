package dk.itu.parallel.morphology.util.queues;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dk.itu.parallel.morphology.util.queues.ConcurrentArrayQueue;

public class ConcurrentArrayQueueTest {

	ConcurrentArrayQueue<Integer> queue;

	@Before
	public void setUp() throws Exception {
		queue = new ConcurrentArrayQueue<Integer>(16);
	}

	@Test
	public void testArraySize() {
		assertEquals(1, new ConcurrentArrayQueue<Integer>(1).arraySize());
		assertEquals(2, new ConcurrentArrayQueue<Integer>(2).arraySize());
		assertEquals(4, new ConcurrentArrayQueue<Integer>(3).arraySize());
		assertEquals(512, new ConcurrentArrayQueue<Integer>(500).arraySize());
		assertEquals(1024, new ConcurrentArrayQueue<Integer>(1000).arraySize());
	}

	@Test
	public void testClear() {
		assertEquals(null, queue.poll());
		assertTrue(queue.isEmpty());
		for (int i = 0; i < queue.arraySize(); ++i)
			assertTrue(queue.add(i));
		assertFalse(queue.isEmpty());
		queue.clear();
		assertEquals(null, queue.poll());
		assertTrue(queue.isEmpty());
	}

	@Test
	public void testIsEmpty() {
		final Integer one = 1;
		assertEquals(null, queue.poll());
		assertTrue(queue.isEmpty());
		assertEquals(0, queue.size());
		queue.add(one);
		assertFalse(queue.isEmpty());
		assertEquals(1, queue.size());
		assertEquals(one, queue.poll());
		assertTrue(queue.isEmpty());
		assertEquals(0, queue.size());
		assertNull(queue.poll());
	}

	@Test
	public void testSize() {
		assertTrue(queue.isEmpty());
		for (int i = 0; i < queue.arraySize(); ++i) {
			assertTrue(queue.add(i));
			assertEquals(i + 1, queue.size());
		}

		int old = queue.size();
		while (!queue.isEmpty()) {
			assertNotNull(queue.poll());
			assertEquals(old - 1, queue.size());
			--old;
		}
	}

	@Test
	public void testAdd() {
		assertTrue(queue.isEmpty());
		for (int i = 0; i < queue.arraySize(); ++i)
			assertTrue(queue.add(i));
		assertFalse(queue.add(0));
		assertEquals(queue.arraySize(), queue.size());
		assertNotNull(queue.poll());
		assertEquals(queue.arraySize() - 1, queue.size());
		assertTrue(queue.add(0));
		assertEquals(queue.arraySize(), queue.size());
	}

	@Test
	public void testPoll() {
		assertTrue(queue.isEmpty());
		for (int i = 0; i < queue.arraySize(); ++i)
			assertTrue(queue.add(i));
		assertFalse(queue.isEmpty());
		while (queue.poll() != null); // Remove everything through poll.
		assertTrue(queue.isEmpty());
	}

}
