package dk.itu.parallel.morphology.filter.base;

/**
 * Base class for filtering threads. Catches all exceptions
 * and also all Throwables not explicitly being caught.
 *
 * @author Florian Biermann
 *
 */
public abstract class FilterThread extends Thread {

	/**
	 * Prints the stack traces of all uncaught exceptions.
	 *
	 * @author Florian Biermann
	 */
	static class Printer implements UncaughtExceptionHandler {

		@Override
		public void uncaughtException(final Thread t, final Throwable e) {
			e.printStackTrace();
		}
	}

	// A single static instance of this should be enough.
	// uncaughtException() is pure anyway, so no synchronization
	// needed.
	static Printer uncaughtHandler = new Printer();

	@Override
	public void run() {
		setUncaughtExceptionHandler(uncaughtHandler);
		try {
			filter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function does the actual filtering each
	 * thread is supposed to perform.
	 *
	 * By defining an interface that throws, we can get
	 * rid of all the annoying try-catch constructs. This
	 * gives cleaner code and is better for presentation.
	 *
	 * @throws Exception Whatever might come up underways.
	 */
	public abstract void filter() throws Exception;
}
