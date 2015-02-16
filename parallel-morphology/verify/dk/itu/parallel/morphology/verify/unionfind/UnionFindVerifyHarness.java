package dk.itu.parallel.morphology.verify.unionfind;

import gov.nasa.jpf.annotation.SandBox;
import gov.nasa.jpf.symbc.Concrete;
import gov.nasa.jpf.vm.Verify;
import dk.itu.parallel.morphology.unionfind.UnionFind;
import dk.itu.parallel.morphology.util.image.Image;

public class UnionFindVerifyHarness extends UnionFind {

	/**
	 * Represents the state of a node
	 * at some index.
	 */
	@SandBox
	class State {

		@Concrete("true")
		public final int index;

		@Concrete("true")
		public final int root;

		@Concrete("true")
		public final int size;

		public State(final int i) {
			index = i;
			root = find(i);
			size = -parent(root);
		}

		/**
		 * A non-compressing, recursive find.
		 * @param c Index of the node to find root of.
		 * @return The root of c.
		 */
                /*---findRec0---*/
		private int find(final int c) {
			if (parent(c) < 0)
				return c;
			return find(parent(c));
		}
                /*---findRec1---*/
	}

	/**
	 * A pair of states, one for each
	 * index. This makes it easier to
	 * get a pre- and a post-state, since
	 * we can control the atomicity of
	 * the operations in a single place.
	 *
	 * @author Florian Biermann
	 */
	@SandBox
	class States {
		final State n;
		final State c;

		public States(final int n, final int c) {
			Verify.beginAtomic();
			this.n = new State(n);
			this.c = new State(c);
			Verify.endAtomic();
		}
	}

	final UnionFind instance;

	public UnionFindVerifyHarness(final UnionFind instance) {
		this.instance = instance;
	}

	@Override
	public void initArrays(final Image img) {
		instance.initArrays(img);
		this.im = img;
	}

	@Override
	public int rootValue(final int i) {
		return instance.rootValue(i);
	}

	@Override
	public int parent(final int i) {
		return instance.parent(i);
	}

	@Override
	public int find(final int c) {
		return instance.find(c);
	}

	@Override
	public void union(final int n, final int c, final int lambda) {
		Verify.ignoreIf(n < 0 ||image().pixel.length <= n);
		Verify.ignoreIf(c < 0 ||image().pixel.length <= c);
		Verify.ignoreIf(lambda < 0 ||image().pixel.length <= lambda);

		// Verify pre-conditions.
		@Concrete("true")
		final States pre = new States(n, c);
		assert value(pre.n.index) >= value(pre.c.index)
				: "Gray-value order has been violated!";

		// Unite.
		instance.union(n, c, lambda);

		// Verify post-conditions
		@Concrete("true")
		final States post = new States(n, c);
		Verify.beginAtomic();
		if (pre.n.root == pre.c.root) { // Already united.
			assert post.n.root == post.c.root : "Pixels were separated!";
		} else { // Not yet united.
			if (value(pre.n.root) == value(pre.c.index) || pre.n.size < lambda) {
				assert post.n.root == post.c.root : "Pixels were NOT united!";
			} else {
				assert post.c.size >= lambda : "Tree has not been deactivated!";
				assert post.n.root != post.c.root : "Pixels were united when they shouldn't have been!";
			}
		}
		Verify.endAtomic();
	}

	@Override
	public long getTriesAndReset() {
		return instance.getTriesAndReset();
	}

}
