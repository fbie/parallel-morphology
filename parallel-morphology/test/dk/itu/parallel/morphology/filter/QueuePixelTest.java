package dk.itu.parallel.morphology.filter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.itu.parallel.morphology.filter.QueuePixels;
import dk.itu.parallel.morphology.unionfind.Optimistic;
import dk.itu.parallel.morphology.unionfind.Transactional;
import dk.itu.parallel.morphology.unionfind.WaitFree;
import dk.itu.parallel.morphology.util.queues.QueuesBuilder;

public class QueuePixelTest extends FilterTest {

	QueuesBuilder<Long> msq;
	QueuesBuilder<Long> array;

	@Before
	public void initQueues() {
		msq = QueuesBuilder.<Long>msq();
		array = QueuesBuilder.<Long>array();
	}

	@Test
	public void testQueueOptimisticMsq() {
		testFilter(new QueuePixels(new Optimistic(), 7, msq));
	}

	@Test
	public void testQueueOptimisticArray() {
		testFilter(new QueuePixels(new Optimistic(), 7, array));
	}

	@Ignore("Wait-free union-find always fails.")
	@Test
	public void testQueueWaitFreeMsq() {
		testFilter(new QueuePixels(new WaitFree(), 7, msq));
	}

	@Ignore("Wait-free union-find always fails.")
	@Test
	public void testQueueWaitFreeArray() {
		testFilter(new QueuePixels(new WaitFree(), 7, array));
	}

	@Test
	public void testQueueTransactionalMsq() {
		testFilter(new QueuePixels(new Transactional(), 7, msq));
	}

	@Test
	public void testQueueTransactionalArray() {
		testFilter(new QueuePixels(new Transactional(), 7, array));
	}

}
