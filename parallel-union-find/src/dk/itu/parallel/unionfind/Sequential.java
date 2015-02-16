package dk.itu.parallel.unionfind;

public class Sequential extends IUnionFind {

	int[] next;
	int[] rank;

	@Override
	public void makeRecords(int size) {
		next = new int[size];
		rank = new int[size];
		for (int i = 0; i < size; ++i) {
			next[i] = i;
			rank[i] = 0;
		}
	}

	@Override
	public void union(int x, int y) {
		x = find(x);
		y = find(y);
		if (x == y)						// Already in the same set.
			return;

		if (rank[x]> rank[y]) {			// Swap to maintain short chains.
			x = x + y;
			y = x - y;
			x = x - y;
		}

		next[x] = y;
		if (rank[x] == rank[y])			// Increase rank to balance tree.
			++rank[y];
	}

	@Override
	public int find(final int x) {
		int c = x;
		while (next[c] != c)
			c = next[c];
		int b = x;
		while (b != c) {
			final int t = next[b];
			next[b] = c;
			b = t;
		}
		return c;
	}

	@Override
	public long getTriesAndReset() {
		return 0;
	}

}
