package dk.itu.parallel.unionfind;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class Optimistic extends IUnionFind {

	final AtomicLong retries;

	int[] next;
	int[] rank;
	ReentrantLock[] lock;

	public Optimistic() {
		retries = new AtomicLong(0);
	}

	@Override
	public void makeRecords(int size) {
		next = new int[size];
		rank = new int[size];
		lock = new ReentrantLock[size];
		for (int i = 0; i < size; ++i) {
			next[i] = i;
			rank[i] = 0;
			lock[i] = new ReentrantLock();
		}
	}

	/**
	 * Acquire lock on two records.
	 *
	 * @param a A record to lock on.
	 * @param b Another record to lock on.
	 */
	/*---lock0---*/
	private void lock(final int a, final int b) {
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
	/*---lock1---*/

	/**
	 * Unlock two records.
	 *
	 * @param a A record to unlock.
	 * @param b Another record to unlock.
	 */
	/*---unlock0---*/
	private void unlock(final int a, final int b) {
		if (lock[a].isHeldByCurrentThread())
			lock[a].unlock();
		if (lock[b].isHeldByCurrentThread())
			lock[b].unlock();
	}
	/*---unlock1---*/

	@Override
	/*---union0---*/
	public void union(final int x, final int y) {
		while (true) {
			int xr = find(x);
			int yr = find(y);
			if (xr == yr) // Already in the same set.
				return;
			lock(xr, yr);

			// Proceed if locked nodes are still roots.
			if (next[xr] == xr && next[yr] == yr) {
				if (rank[xr] > rank[yr]) { // Maintain short chains.
					// Swap xr and yr...
					/*---union1---*/
					final int t = xr;
					xr = yr;
					yr = t;
					/*---union2---*/
				}
				next[xr] = yr; // Unite nodes.
				if (rank[xr] == rank[yr])
					++rank[yr]; // Increase rank to balance tree.

				// Compress paths while having lock.
				compress(x, yr);
				compress(y, yr);

				unlock(xr, yr); // Release lock before returning.
				return;
			}
			unlock(xr, yr); // Release lock before retrying.
			/*---union3---*/
			retries.incrementAndGet();
			/*---union4---*/
		}
	}
	/*---union5---*/

	/**
	 * Compress the path from bottom to root.
	 *
	 * @param bottom The bottom node.
	 * @param root The root node.
	 */
	/*---compress0---*/
	private void compress(int bottom, final int root) {
		while (bottom != root) {
			final int t = next[bottom];
			next[bottom] = root;
			bottom = t;
		}
	}
	/*---compress1---*/

	@Override
	/*---find0---*/
	public int find(int x) {
		while (next[x] != x) {
			x = next[x];
		}
		return x;
	}
	/*---find1---*/

	@Override
	public String toString() {
		return "optimistic";
	}

	@Override
	public long getTriesAndReset() {
		return retries.getAndSet(0);
	}

}
