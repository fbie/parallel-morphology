package dk.itu.parallel.unionfind;

public abstract class IUnionFind {

	/**
	 * Builds a reference map of items to atomic records.
	 *
	 * @param items List of items valid for this union find.
	 */
	public abstract void makeRecords(int size);

	/**
	 * Unite two disjoint sets.
	 *
	 * @param x
	 * @param y
	 */
	public abstract void union(int x, int y);

	/**
	 * Find the root of a set.
	 *
	 * @param x The set to find the root of.
	 * @return The root of x.
	 */
	public abstract int find(int x);

	/**
	 * Return the number of times union() had to
	 * re-try because of the roots not being roots
	 * any longer and re-set the counter to 0.
	 *
	 * @return The number of re-tries since instantiation or last call.
	 */
	public abstract long getTriesAndReset();
}
