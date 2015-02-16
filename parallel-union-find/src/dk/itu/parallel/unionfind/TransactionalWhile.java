package dk.itu.parallel.unionfind;

import static org.multiverse.api.StmUtils.*;

public class TransactionalWhile extends Transactional {

	@Override
	/*---filter0---*/
	public void union(final int x, final int y) {
		atomic(new Runnable() {

			@Override
			public void run() {
				while (true) {
					int xr = find(x);
					int yr = find(y);
					if (xr == yr) // Already in the same set.
						return;

					// Unite if notes are roots.
					if (next[xr].atomicWeakGet() == xr
						&& next[yr].atomicWeakGet() == yr) {

						// Maintain short chains.
						if (rank[xr].atomicWeakGet() > rank[yr].atomicWeakGet()) {
							// Swap xr and yr...
							/*---filter1---*/
							final int t = xr;
							xr = yr;
							yr = t;
							/*---filter2---*/
						}
						next[xr].set(yr); // Unite nodes.

						// Increase rank to balance tree.
						if (rank[xr].atomicWeakGet() == rank[yr].atomicWeakGet())
							rank[yr].increment();
						return;
					}
				}
			}
		});
	}
	/*---filter3---*/

	@Override
	public String toString() {
		return "stm-while";
	}
}
