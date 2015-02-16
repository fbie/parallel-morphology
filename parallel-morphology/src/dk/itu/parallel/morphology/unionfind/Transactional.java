package dk.itu.parallel.morphology.unionfind;

import org.multiverse.api.GlobalStmInstance;
import org.multiverse.api.Txn;
import org.multiverse.api.callables.TxnVoidCallable;
import org.multiverse.api.references.*;

import dk.itu.parallel.morphology.util.RetryListener;
import dk.itu.parallel.morphology.util.image.Image;
import static org.multiverse.api.StmUtils.*;

public class Transactional extends UnionFind {

	TxnInteger parent[];
	final RetryListener retry;

	public Transactional() {
		retry = new RetryListener();

		// Seriously?
		GlobalStmInstance.getGlobalStmInstance().
			getDefaultTxnExecutor().
			getTxnFactory().
			getTxnFactoryBuilder().setMaxRetries(Integer.MAX_VALUE);
	}

	@Override
	public void initArrays(final Image img) {
		im = img;
		parent = new TxnInteger[im.pixel.length];
		for (int i = 0; i < parent.length; ++i) {
			parent[i] = newTxnInteger(-1);
		}
	}

	@Override
	public int rootValue(final int i) {
		return value(find(i));
	}

	@Override
	public int parent(final int i) {
		return parent[i].atomicWeakGet();
	}

	@Override
	/*---find0---*/
	public int find(int c) {
		while (true) {
			final int p = parent[c].atomicWeakGet();
			if (p < 0) 	// Not being a root is invariant.
				break;
			final int q = parent[p].atomicWeakGet();
			if (q >= 0) // Not being a root is invariant.
				parent[c].atomicCompareAndSet(p, q);
			c = p;
		}
		return c;
	}
	/*---find1---*/

	@Override
	/*---union0---*/
	public void union(final int n, final int c, final int lambda) {
		atomic(new TxnVoidCallable() { // Wrap the atomic code.
			/*---union1---*/
			@Override
			/*---union2---*/
			public void call(final Txn txn) throws Exception {
				/*---union3---*/
				txn.register(retry);
				/*---union4---*/
				while (true) {
					final int nr = find(n);
					final int cr = find(c);
					if (nr == cr) // Already in the same set.
						return;

					// Read states consistently.
					final int ns = parent[nr].get();
					final int cs = parent[cr].get();

					// If both elements are roots, unite them.
					if (ns < 0 && cs < 0) {
						if (value(nr) == value(c) || -ns < lambda) {
							parent[cr].set(ns + cs); // Grow component size.
							parent[nr].set(cr); // Unite elements.
						} else if (-cs < lambda){ // Disable only if still active.
							parent[cr].set(-lambda);
						}
						return;
					}
				}
			}
		});
	}
	/*---union5---*/

	@Override
	public long getTriesAndReset() {
		final long r = retry.retries();
		retry.reset();
		return r;
	}

	@Override
	public String toString() {
		return "stm";
	}
}
