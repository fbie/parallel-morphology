package dk.itu.parallel.morphology.unionfind;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import dk.itu.parallel.morphology.util.image.Image;

/**
 * Optimistic fine-grained union-find. A lock is
 * provided for each pixel index. Find is a read-only
 * function while union compresses the paths.
 */
public class Optimistic extends SequentialUnionFind {

	protected ReentrantLock[] lock;
	protected final AtomicInteger retry;

	public Optimistic() {
		retry = new AtomicInteger(0);
	}

	/**
	 * Acquire a lock on a pair of indices.
	 *
	 * @param a Some index.
	 * @param b Some other index.
	 */
	protected final void lock(final int a, final int b) {
		while (true) {

			// Spin while lock is unavailable.
			while (lock[a].isLocked() || lock[b].isLocked());

			// Try to acquire lock and return on success.
			if (lock[a].tryLock() && lock[b].tryLock())
				return;
			else
				unlock(a, b);
		}
	}

	/**
	 * Release lock on a pair of indices
	 * if held by the current thread.
	 *
	 * @param a Some index.
	 * @param b Some other index.
	 */
	protected final void unlock(final int a, final int b) {
		if (lock[a].isHeldByCurrentThread())
			lock[a].unlock();

		if (lock[b].isHeldByCurrentThread())
			lock[b].unlock();
	}

	@Override
	public void initArrays(final Image img) {
		super.initArrays(img);
		lock = new ReentrantLock[im.pixel.length];
		for (int i = 0; i < lock.length; ++i) {
			lock[i] = new ReentrantLock();
		}
	}

	@Override
	/*---find0---*/
	public int find(int c) {
		while (parent[c] >= 0) {
			c = parent[c];
		}
		return c;
	}
	/*---find1---*/

	/**
	 * Compress path from bottom up to root.
	 *
	 * @param bottom index.
	 * @param root index.
	 */
	/*---compress0---*/
	protected void compress(int bottom, final int root) {
		while (parent[bottom] >= 0) {
			final int t = parent[bottom];
			parent[bottom] = root;
			bottom = t;
		}
	}
	/*---compress1---*/

	@Override
	/*---union0---*/
	public void union(final int n, final int c, final int lambda) {
		while (true) {
			int nr = find(n);
			int cr = find(c);
			if (nr == cr) // Already in the same set.
				return;
			lock(nr, cr);

			// If both elements are roots, unite them.
			if (parent[nr] < 0 && parent[cr] < 0) {
				if (value(nr) == value(c) || -parent[nr] < lambda) {
					parent[cr] += parent[nr];
					parent[nr] = cr;
					compress(n, cr); // Compress path to new root.
				} else {
					parent[cr] = -lambda;
					compress(n, nr); // Compress path to old root.
				}
				compress(c, cr); // Compress always unconditionally.

				// Unlock after success.
				unlock(nr, cr);
				return;
			}

			// Unlock and re-try.
			unlock(nr, cr);
			/*---union1---*/
			retry.incrementAndGet();
			/*---union2---*/
		}
	}
	/*---union3---*/

	@Override
	public long getTriesAndReset() {
		return retry.getAndSet(0);
	}

	@Override
	public String toString() {
		return "opt";
	}
}
