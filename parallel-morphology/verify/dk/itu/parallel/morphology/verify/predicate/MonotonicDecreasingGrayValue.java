package dk.itu.parallel.morphology.verify.predicate;

import dk.itu.parallel.morphology.filter.base.Filter;
import gov.nasa.jpf.aprop.ContractException;
import gov.nasa.jpf.aprop.Predicate;

/**
 * Verifies that the passed in gray value is monotonically decreasing
 * with regard to the last seen gray value.
 *
 * @author Florian Biermann
 */
public class MonotonicDecreasingGrayValue implements Predicate {

	private Integer theta;

	/**
	 * Convert some object to a Filter instance safely.
	 *
	 * @param o Test object.
	 * @return The same object cast to Filter safely.
	 */
	private final Filter filter(final Object o) {
		if (!(o instanceof Filter))
			throw new ContractException("Object must be of type Filter, not " + o.getClass().getName());
		return (Filter)o;
	}

	/**
	 * Convert some array of Object to an array of int safely.
	 *
	 * @param arr Argument array of Object of size 1.
	 * @return The integer value of Object.
	 */
	private final int pixels(final Object[] arr) {
		if (arr.length != 1)
			throw new ContractException("Expected exactly one argument.");

		if (!(arr[0] instanceof Integer))
			throw new ContractException("Arguments must be of type int.");
		return (int)arr[0];
	}

	@Override
	public String evaluate(final Object testObj, final Object[] args) {
		System.out.println("theta = " + theta);

		final Filter f = filter(testObj);
		final int c = pixels(args);

		if (theta != null && theta > f.value(c))
			return "Gray value is not monotonically decreasing!";

		theta = f.value(c);
		System.out.println("theta = " + theta);
		assert theta != null;
		return null;
	}

}
