package dk.itu.parallel.morphology.util;

/**
 * A simple class maintaining a lower and upper
 * index bound that enclose a pixel line.
 */
/*---line0---*/
public class Line {
	public final int[] sorted;
	public int p;

	public Line(final int[] pixel) {
		this.sorted = pixel;
		p = pixel.length - 1;
	}

	public final boolean advance() {
		return --p >= 0;
	}

	public final boolean workLeft() {
		return p >= 0;
	}

	public final int current() {
		return sorted[p];
	}
}
/*---line1---*/
