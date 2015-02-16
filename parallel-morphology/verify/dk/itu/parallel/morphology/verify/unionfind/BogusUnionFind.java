package dk.itu.parallel.morphology.verify.unionfind;

import dk.itu.parallel.morphology.unionfind.SequentialUnionFind;

/**
 * A bogus union-find to check that wrong implementations are found.
 *
 * @author Florian Biermann
 */
public class BogusUnionFind extends SequentialUnionFind {

	/*---union0---*/
	public void union(final int n, final int c, final int lambda) {
		final int nr = find(n);
		final int cr = find(c);
		if (nr == cr) // Already in the same set.
			return;

		// Otherwise, always unite. This is wrong!
		parent[cr] += parent[nr];
		parent[nr] = cr;
	}
	/*---union1---*/

	@Override
	public String toString() {
		return "bogus";
	}

}
