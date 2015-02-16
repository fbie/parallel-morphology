package dk.itu.parallel.unionfind;

import org.junit.Before;

import dk.itu.parallel.unionfind.Optimistic;

public class OptimisticTest extends IUnionFindTest {

	@Before
	public void setUp() throws Exception {
		algorithm = new Optimistic();
		algorithm.makeRecords(size);
	}
}
