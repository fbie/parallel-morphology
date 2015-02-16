package dk.itu.parallel.unionfind;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class WaitFree extends IUnionFind {

	/*---record0---*/
	class Record {
		final int rank;
		final AtomicInteger next;

		public Record(final int rank, final int next) {
			this.rank = rank;
			this.next = new AtomicInteger(next);
		}

		public int next() {
			return next.get();
		}
	}
	/*---record1---*/

	final AtomicLong retries;
	AtomicReferenceArray<Record> record;

	public WaitFree() {
		retries = new AtomicLong(0);
	}

	@Override
	public void makeRecords(int size) {
		record = new AtomicReferenceArray<Record>(size);
		for (int i = 0; i < size; ++i) {
			record.set(i, new Record(0, i));
		}
	}

	private Record record(final int x) {
		return record.get(x);
	}

	@Override
	/*---union0---*/
	public void union(int x, int y) {
		while (true) {
			x = find(x);
			y = find(y);
			if (x == y) // Already in the same set.
				return;

			int rx = record(x).rank;
			int ry = record(y).rank;
			if (rx > ry) { // Maintain short chains.
				// Swap x, y and rx, ry...
				/*---union1---*/
				x = x + y;
				y = x - y;
				x = x - y;

				rx = rx + ry;
				ry = rx - ry;
				rx = rx - ry;
				/*---union2---*/
			}

			if (!updateRoot(x, rx, y, rx)) { // Try to update root.
				/*---union3---*/
				retries.incrementAndGet();
				/*---union4---*/
				continue; // Re-try if update failed.
			}

			if (rx == ry) { // Increase rank to balance tree.
				updateRoot(y, ry, y, ry + 1);
			}
			setRoot(x);
			return;
		}
	}
	/*---union5---*/

	@Override
	/*---find0---*/
	public int find(int x) {
		while (record(x).next() != x) {
			final int p = record(x).next();
			final int q = record(p).next();
			record(x).next.compareAndSet(p, q);
			x = q;
		}
		return x;
	}
	/*---find1---*/

	/**
	 * Updates a root and its rank only if no other thread updated it in the
	 * meantime.
	 *
	 * @param x Root to be updated.
	 * @param oldrank The root's old rank.
	 * @param y The new root.
	 * @param newrank The old roots new rank.
	 * @return True if the operation succeeded, false otherwise.
	 */
	/*---updateroot0---*/
	boolean updateRoot(int x, int oldrank, int y, int newrank) {
		final Record r = record(x);
		if (r.next() != x || r.rank != oldrank)
			return false;

		final Record n = new Record(y, newrank);
		return record.compareAndSet(x, r, n);
	}
	/*---updateroot1---*/

	/**
	 * Compress path and update root rank.
	 *
	 * @param x The bottom node.
	 */
	/*---setroot0---*/
	final void setRoot(final int x) {
		int y = x;
		while (record(y).next() != y) {
			final int p = record(y).next();
			final int q = record(p).next();
			record.get(y).next.compareAndSet(p, q);
			y = q;
		}
		updateRoot(y, record(x).rank, y, record(x).rank + 1);
	}
	/*---setroot1---*/

	@Override
	public String toString() {
		return "wait-free";
	}

	@Override
	public long getTriesAndReset() {
		return retries.getAndSet(0);
	}

}
