package dk.itu.parallel.morphology.filter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import dk.itu.parallel.morphology.filter.BlockGrayLevel;
import dk.itu.parallel.morphology.unionfind.Optimistic;
import dk.itu.parallel.morphology.unionfind.Transactional;
import dk.itu.parallel.morphology.unionfind.WaitFree;
import dk.itu.parallel.morphology.util.sort.LinearPixelSortBuilder;

public class BlockGrayLevelTest extends FilterTest {

	LinearPixelSortBuilder count;
	LinearPixelSortBuilder bucket;

	@Before
	public void initSorting() {
		count = LinearPixelSortBuilder.counting();
		bucket = LinearPixelSortBuilder.bucket();
	}

	@Test
	public void testBlockOptimistic() {
		testFilter(new BlockGrayLevel(new Optimistic(), 7, count));
		testFilter(new BlockGrayLevel(new Optimistic(), 7, bucket));
	}

	@Ignore("Wait-free union-find always fails.")
	@Test
	public void testBlockWaitFree() {
		testFilter(new BlockGrayLevel(new WaitFree(), 7, count));
		testFilter(new BlockGrayLevel(new WaitFree(), 7, bucket));
	}

	@Test
	public void testBlockTransactional() {
		testFilter(new BlockGrayLevel(new Transactional(), 7, count));
		testFilter(new BlockGrayLevel(new Transactional(), 7, bucket));
	}
}
