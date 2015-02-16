package dk.itu.parallel.morphology.verify.predicate;

import gov.nasa.jpf.annotation.SandBox;
import gov.nasa.jpf.symbc.Concrete;
import gov.nasa.jpf.vm.Verify;

/**
 * This class is a workaround for jpf-aprop's ContractListener
 * not being able to execute MonotonicDecreasingGrayValue.evaluate()
 * properly.
 *
 * There is a thread about that on the JPF mailing list:
 * https://groups.google.com/forum/#!topic/java-pathfinder/a-iKi2z6L8I
 *
 * @author Florian Biermann
 *
 */
@SandBox
public class MonotonicDecreasingGrayValueHelper {

	// Last observed gray-scale value.
	@Concrete("true")
	private static volatile Integer theta = null;

	/**
	 * Verifies that the gray-scale values filtered
	 * are monotonically decreasing.
	 *
	 * @param v Gray-scale value of the last filtered pixel.
	 */
	@Concrete("true")
	public static void verify(final int v) {
		Verify.beginAtomic();
		if (theta != null) {
			Verify.interesting(theta >= v);
			assert theta >= v : "Gray-value is not monotonically decreasing!";
//			assert theta <= v : "Gray-value is not strictly increasing!"; // Just for testing.
		}
		theta = v;
		Verify.endAtomic();
	}
}
