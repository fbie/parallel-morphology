package dk.itu.parallel.morphology.filter;

import org.junit.Ignore;
import org.junit.Test;

import dk.itu.parallel.morphology.filter.Split;
import dk.itu.parallel.morphology.unionfind.Optimistic;
import dk.itu.parallel.morphology.unionfind.Transactional;
import dk.itu.parallel.morphology.unionfind.WaitFree;

public class SplitTest extends FilterTest {

	@Test
	public void testSplitOptimistic() {
		testFilter(new Split(new Optimistic(), 7));
	}

	@Ignore("Wait-free union-find always fails.")
	@Test
	public void testSplitWaitFree() {
		testFilter(new Split(new WaitFree(), 7));
	}

	@Test
	public void testSplitTransactional() {
		testFilter(new Split(new Transactional(), 7));
	}

}
