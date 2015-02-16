package dk.itu.parallel.unionfind;

import org.multiverse.api.Txn;
import org.multiverse.api.callables.TxnVoidCallable;
import org.multiverse.api.references.TxnInteger;

import dk.itu.parallel.unionfind.util.RetryListener;
import static org.multiverse.api.StmUtils.*;

public class Transactional extends IUnionFind {

	final RetryListener retry;

	TxnInteger[] next;
	TxnInteger[] rank;

	public Transactional() {
		retry = new RetryListener();
	}

	@Override
	public void makeRecords(int size) {
		next = new TxnInteger[size];
		rank = new TxnInteger[size];
		for (int i = 0; i < size; ++i) {
			next[i] = newTxnInteger(i);
			rank[i] = newTxnInteger(0);
		}
	}

	@Override
	/*---union0---*/
	public void union(final int x, final int y) {
		atomic(new TxnVoidCallable() {

			@Override
			public void call(final Txn txn) throws Exception {
				/*---union1---*/
				txn.register(retry);
				/*---union2---*/
				int xr = find(x);
				int yr = find(y);
				if (xr == yr) // Already in the same set, abort.
					return;

				// Maintain short chains.
				if (rank[xr].atomicWeakGet() > rank[yr].atomicWeakGet()) {
					// Swap xr and yr...
					/*---union3---*/
					xr = xr + yr;
					yr = xr - yr;
					xr = xr - yr;
					/*---union4---*/
				}
				next[xr].set(yr); // Unite nodes.

				// Increase rank to balance tree.
				if (rank[xr].atomicWeakGet() == rank[yr].atomicWeakGet())
					rank[yr].increment();
			}
		});
	}
	/*---union5---*/

	@Override
	/*---find0---*/
	public int find(int x) {
		while (next[x].atomicWeakGet() != x) {
			final int p = next[x].atomicWeakGet();
			final int q = next[p].atomicWeakGet();
			next[x].atomicCompareAndSet(p, q);
			x = q;
		}
		return x;
	}
	/*---find1---*/

	@Override
	public String toString() {
		return "stm";
	}

	@Override
	public long getTriesAndReset() {
		final long retries = retry.retries();
		retry.reset();
		return retries;
	}
}
