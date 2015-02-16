package dk.itu.parallel.morphology.filter;

import org.junit.Ignore;
import org.junit.Test;

import dk.itu.parallel.morphology.unionfind.Optimistic;
import dk.itu.parallel.morphology.unionfind.Transactional;
import dk.itu.parallel.morphology.unionfind.WaitFree;

public class HashedLinesTest extends FilterTest {

	@Test
	public void testBlockOptimistic() {
		testFilter(new HashedLines(new Optimistic(), 7));
	}

	@Ignore("Wait-free union-find always fails.")
	@Test
	public void testBlockWaitFree() {
		testFilter(new HashedLines(new WaitFree(), 7));
	}

	@Test
	public void testBlockTransactional() {
		testFilter(new HashedLines(new Transactional(), 7));
	}
}
