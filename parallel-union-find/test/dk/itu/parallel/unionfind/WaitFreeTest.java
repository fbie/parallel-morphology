package dk.itu.parallel.unionfind;

import org.junit.Before;

import dk.itu.parallel.unionfind.WaitFree;

public class WaitFreeTest extends IUnionFindTest {

	@Before
	public void setUp() throws Exception {
		algorithm = new WaitFree();
		algorithm.makeRecords(size);
	}
}
