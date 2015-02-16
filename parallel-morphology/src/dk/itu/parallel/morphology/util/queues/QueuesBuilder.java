package dk.itu.parallel.morphology.util.queues;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A factory class to circumvent Java's endlessly
 * restrictive polymorphic type system. It enables
 * us to re-use some of the code base a bit better.
 *
 * @author Florian Biermann
 *
 * @param <T> The type which the queues object should hold.
 */
public abstract class QueuesBuilder<T> {

	public abstract Queues<T> create(final int size);

	protected abstract String type();

	public String toString() {
		return type();
	}

	/**
	 * @return A QueuesBuilder that constructs Queues based on ConcurrentLinkedQueue<T>
	 */
	final public static <T> QueuesBuilder<T> msq() {
		return new QueuesBuilder<T>(){
			@Override
			public Queues<T> create(int size) {
				return new Queues<T>(
						new ConcurrentLinkedQueue<T>(),
						new ConcurrentLinkedQueue<T>(),
						255);
			}

			@Override
			protected String type() {
				return "msq";
			}
		};
	}

	/**
	 * @return A QueuesBuilder that constructs Queues based on ConcurrentArrayQueue<T>
	 */
	final public static <T> QueuesBuilder<T> array() {
		return new QueuesBuilder<T>(){
			@Override
			public Queues<T> create(int size) {
				return new Queues<T>(
						new ConcurrentArrayQueue<T>(size),
						new ConcurrentArrayQueue<T>(size),
						255);
			}

			@Override
			protected String type() {
				return "array";
			}
		};
	}

}
