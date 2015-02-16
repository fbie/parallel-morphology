package dk.itu.parallel.morphology.unionfind;

import java.util.Arrays;

import dk.itu.parallel.morphology.util.image.Image;

public class SequentialUnionFind extends UnionFind {

	protected int[] parent;

	@Override
	public void initArrays(final Image img) {
		im = img;
		parent = new int[im.pixel.length];
		Arrays.fill(parent, -1);
	}

	@Override
	public int rootValue(final int i) {
		return value(find(i));
	}

	@Override
	public int parent(final int i) {
		return parent[i];
	}

	@Override
	/*---find0---*/
	public int find(final int c) {
		int root = c;
		while (parent[root] >= 0) {
			root = parent[root];
		}

		// Compress path.
		int bottom = c;
		while (bottom != root) {
			int t = parent[bottom];
			parent[bottom] = root;
			bottom = t;
		}
		return root;
	}
	/*---find1---*/

	@Override
	/*---union0---*/
	public void union(final int n, final int c, final int lambda) {
		final int nr = find(n);
		final int cr = find(c);

		// Already in the same set.
		if (nr == cr)
			return;

		// Join level and peak components.
		if (value(nr) == value(c) || -parent[nr] < lambda) {
			parent[cr] += parent[nr];
			parent[nr] = cr;
		} else {
			// De-activate tree.
			parent[cr] = -lambda;
		}
	}
	/*---union1---*/

	@Override
	public long getTriesAndReset() {
		return 0;
	}

	@Override
	public String toString() {
		return "union-find";
	}
}
