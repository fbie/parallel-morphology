package dk.itu.parallel.unionfind;

import org.junit.Before;

public class TransactionalWhileTest extends IUnionFindTest {

	@Before
	public void setUp() throws Exception {
		algorithm = new TransactionalWhile();
		algorithm.makeRecords(size);
	}

}
