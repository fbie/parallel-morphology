package dk.itu.parallel.morphology.unionfind;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import dk.itu.parallel.morphology.util.image.Image;

public class WaitFree extends UnionFind {

	AtomicIntegerArray parent;
	final AtomicInteger retry;

	public WaitFree() {
		retry = new AtomicInteger(0);
	}

	@Override
	public void initArrays(final Image img) {
		im = img;
		parent = new AtomicIntegerArray(im.pixel.length);
		for (int i = 0; i < parent.length(); ++i) {
			parent.set(i, -1);
		}
	}

	@Override
	public int rootValue(final int i) {
		return value(find(i));
	}

	@Override
	public int parent(final int i) {
		return parent.get(i);
	}

	@Override
	public int find(int c) {
		while (parent.get(c) >= 0) {
			final int q = parent.get(c);
			final int p = parent.get(q);
			if (p >= 0)							// Compress only if q is no root.
				parent.compareAndSet(c, q, p);
			c = q;								// p >= 0 is invariant, hence no check.
		}
		return c;
	}

	/**
	 * Computes if x and y are in the same set. Only
	 * returns if a definite answer can be given, retries
	 * otherwise.
	 *
	 * @param x Some index.
	 * @param y Some other index.
	 * @return True if x and y are in the same set, false otherwise.
	 */
	protected boolean sameSet(int x, int y) {
		while (true) {
			x = find(x);
			y = find(y);
			if (x == y)
				return true;
			if (parent.get(x) < 0)
				return false;
		}
	}

	/**
	 * Tries to update the root from any state to p.
	 * Returns true if no other thread interfered,
	 * false otherwise.
	 *
	 * @param i Index of which the root should be updated.
	 * @param r The new root value.
	 * @return True if update succeeded, false otherwise.
	 */
	public boolean updateRoot(final int i, final int r) {
		final int old = parent.get(i);
		if (old >= 0)					// i is no root.
			return false;
		if (r >= 0 && sameSet(i, r))	// r is an index, check for same set.
			return true;
		return parent.compareAndSet(i, old, r);
	}

	@Override
	public void union(final int n, final int c, final int lambda) {

		while (true) {
			final int nr = find(n);
			final int cr = find(c);
			if (sameSet(nr, cr))
				return;

			final int ns = parent.get(nr);
			final int cs = parent.get(cr);

			if (ns < 0 && cs < 0) {									// Re-try if elements are no roots.
				if (value(nr) == value(c) || -ns < lambda) {
					if (parent.compareAndSet(cr, cs, cs + ns)) {	// First, update size as guard.
						while (!updateRoot(find(nr), cr));			// Updating root succeeds eventually.
						return;
					}
				} else {
					if (-cs >= lambda || updateRoot(cr, -lambda))	// Disable only if necessary.
						return;
				}
			}
			retry.incrementAndGet();
		}
	}

	@Override
	public long getTriesAndReset() {
		return retry.getAndSet(0);
	}

	@Override
	public String toString() {
		return "wf";
	}
}
