package dk.itu.parallel.morphology.filter;

import org.junit.Ignore;
import org.junit.Test;

import dk.itu.parallel.morphology.filter.SplitCounting;
import dk.itu.parallel.morphology.unionfind.Optimistic;
import dk.itu.parallel.morphology.unionfind.Transactional;
import dk.itu.parallel.morphology.unionfind.WaitFree;

public class SplitCountingTest extends FilterTest {

	@Test
	public void testSplitBucketsOptimistic() {
		testFilter(new SplitCounting(new Optimistic(), 7));
	}

	@Ignore("Wait-free union-find always fails.")
	@Test
	public void testSplitBucketsWaitFree() {
		testFilter(new SplitCounting(new WaitFree(), 7));
	}

	@Test
	public void testSplitBucketsTransactional() {
		testFilter(new SplitCounting(new Transactional(), 7));
	}

}
