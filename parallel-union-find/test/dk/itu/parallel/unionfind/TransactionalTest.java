package dk.itu.parallel.unionfind;


import org.junit.Before;

import dk.itu.parallel.unionfind.Transactional;

public class TransactionalTest extends IUnionFindTest {

	@Before
	public void setUp() throws Exception {
		algorithm = new Transactional();
		algorithm.makeRecords(size);
	}

}
