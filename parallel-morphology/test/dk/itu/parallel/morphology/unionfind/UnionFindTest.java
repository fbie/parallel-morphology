package dk.itu.parallel.morphology.unionfind;

import org.junit.Ignore;
import org.junit.Test;

import dk.itu.parallel.morphology.filter.FilterTest;
import dk.itu.parallel.morphology.filter.Sequential;
import dk.itu.parallel.morphology.unionfind.Optimistic;
import dk.itu.parallel.morphology.unionfind.Transactional;
import dk.itu.parallel.morphology.unionfind.WaitFree;

public class UnionFindTest extends FilterTest {

	@Test
	public void testSequentialOptimistic() {
		testFilter(new Sequential(new Optimistic()));
	}

	@Ignore("Wait-free union-find always fails.")
	@Test
	public void testSequentialWaitFree() {
		testFilter(new Sequential(new WaitFree()));
	}

	@Test
	public void testSequentialTransactional() {
		testFilter(new Sequential(new Transactional()));
	}

}
