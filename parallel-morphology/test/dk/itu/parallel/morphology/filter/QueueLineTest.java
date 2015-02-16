package dk.itu.parallel.morphology.filter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.itu.parallel.morphology.filter.QueueLines;
import dk.itu.parallel.morphology.unionfind.Optimistic;
import dk.itu.parallel.morphology.unionfind.Transactional;
import dk.itu.parallel.morphology.unionfind.WaitFree;
import dk.itu.parallel.morphology.util.Line;
import dk.itu.parallel.morphology.util.queues.QueuesBuilder;

public class QueueLineTest extends FilterTest {

	QueuesBuilder<Line> msq;
	QueuesBuilder<Line> array;

	@Before
	public void initQueues() {
		msq = QueuesBuilder.<Line>msq();
		array = QueuesBuilder.<Line>array();
	}

	@Test
	public void testSplitLinesOptimisticMsq() {
		testFilter(new QueueLines(new Optimistic(), 7, msq));
	}

	@Test
	public void testSplitLinesOptimisticArray() {
		testFilter(new QueueLines(new Optimistic(), 7, array));
	}

	@Ignore("Wait-free union-find always fails.")
	@Test
	public void testSplitLinesWaitFreeMsq() {
		testFilter(new QueueLines(new WaitFree(), 7, msq));
	}

	@Ignore("Wait-free union-find always fails.")
	@Test
	public void testSplitLinesWaitFreeArray() {
		testFilter(new QueueLines(new WaitFree(), 7, array));
	}

	@Test
	public void testSplitLinesTransactionalMsq() {
		testFilter(new QueueLines(new Transactional(), 7, msq));
	}

	@Test
	public void testSplitLinesTransactionalArray() {
		testFilter(new QueueLines(new Transactional(), 7, array));
	}

	public static void main(final String[] args) {
		try  {
			final QueueLineTest t = new QueueLineTest();
			t.setUp();
			t.initQueues();
			t.testSplitLinesOptimisticMsq();
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
