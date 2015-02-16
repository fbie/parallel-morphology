package dk.itu.parallel.morphology.filter;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({BasicFilterTest.class,
	BlockGrayLevelTest.class,
	QueuePixelTest.class,
	SplitCountingTest.class,
	QueueLineTest.class,
	HashedLinesTest.class,
	SplitTest.class
	})
public class FilterSuite {
  // Purposely empty
}

