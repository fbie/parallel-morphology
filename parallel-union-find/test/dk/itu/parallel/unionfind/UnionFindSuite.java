package dk.itu.parallel.unionfind;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	OptimisticTest.class,
	WaitFreeTest.class,
	TransactionalTest.class,
	TransactionalWhileTest.class
	})
public class UnionFindSuite {
  // Purposely empty
}

