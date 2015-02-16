package dk.itu.parallel.morphology.util.queues;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * An array-backed concurrent wait-free implementation of
 * queue. The caller of the constructor needs to specify a
 * desired size. However, the queue will re-size accordingly
 * for performance reasons. The actual size will never be
 * lower than what the caller defined.
 *
 * @author Florian Biermann
 */
/*---queue0---*/
public class ConcurrentArrayQueue<E> implements Queue<E> {

	final AtomicLong head;
	final AtomicLong tail;

	final AtomicReferenceArray<E> array;
	final long mask;

	/*---queue1---*/
	final int size;

	/**
	 * Instantiate a fixed-size array backed queue.
	 *
	 * @param size
	 * 	The desired size. However, the constructor finds the
	 *  next greatest power of two, if size not already is a power of two.
	 */
	public ConcurrentArrayQueue(final int size) {
		head = new AtomicLong(0);
		tail = new AtomicLong(0);

		// Find the most significant bit set in O(n).
		if (size != 0 && (size & (size - 1)) == 0) {
			this.size = size;
		} else {
			int bits = 0;
			for (int n = size; n > 0; n >>= 1)
				++bits;
			this.size = 1 << bits;
		}

		// Actual array size is now a power of two.
		array = new AtomicReferenceArray<E>(this.size);

		// All bits below the most significant are
		// set to one. Bitwise-and with the mask is
		// the same as modulo but much faster.
		mask = this.size - 1;
	}

	@Override
	/*---queue2---*/
	public boolean isEmpty() {
		return head.get() == tail.get();
	}

	/*---queue3---*/

	@Override
	public int size() {
		return (int)(tail.get() - head.get());
	}

	@Override
	/*---queue4---*/
	public boolean add(final E arg0) {
		while (true) {
			final long i = tail.get();
			if (size() < array.length() - 1) {
				// If CAS fails, someone added in the meantime.
				if (tail.compareAndSet(i, i + 1)) {
					array.set((int)(i & mask), arg0);
					return true;
				}
			} else {
				// Queue was already full.
				return false;
			}
		}
	}

	/*---queue5---*/

	@Override
	/*---queue6---*/
	public E poll() {
		while (true) {
			final long i = head.get();
			if (!isEmpty()) {
				// If CAS fails, someone polled in the meantime.
				if (head.compareAndSet(i, i + 1))
					return array.get((int)(i & mask));
			} else {

				// The queue was already empty or
				// someone else took our element.
				return null;
			}
		}
	}

	/*---queue7---*/

	/**
	 * @return The actual size of the array.
	 */
	public int arraySize() {
		return size;
	}

	// Everything below this line is less important.

	@Override
	public boolean addAll(final Collection<? extends E> arg0) {
		boolean success = true;
		for (final E a : arg0) {
			success &= add(a);
		}
		return success;
	}

	@Override
	public void clear() {
		head.set(tail.get());
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean contains(final Object arg0) {
		final int h = (int)(head.get() & mask);
		final int t = (int)(tail.get() & mask);

		final E a = (E) arg0;
		for (int i = h; i < t; ++i) {
			if (array.get(i) == a)
				return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(final Collection<?> arg0) {
		for (Object a : arg0) {
			if (!contains(a))
				return false;
		}
		return true;
	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(final Object arg0) {
		return false;
	}

	@Override
	public boolean removeAll(final Collection<?> arg0) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return false;
	}

	@Override
	public Object[] toArray() {
		final int h = (int)(head.get() & mask);
		final int t = (int)(tail.get() & mask);

		final Object[] out = new Object[t - h];
		for (int i = h; i < t; ++i) {
			out[i - h] = array.get(i);
		}
		return out;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return arg0;
	}

	@Override
	public E element() {
		final E e = peek();
		if (e == null)
			throw new NoSuchElementException();
		return e;
	}

	@Override
	public boolean offer(final E arg0) {
		return add(arg0);
	}

	@Override
	public E peek() {
		final E e = array.get((int)(head.get() & mask));
		if (isEmpty())
			return null;
		return e;
	}

	@Override
	public E remove() {
		final E e = poll();
		if (e == null)
			throw new NoSuchElementException();
		return e;
	}
/*---queue8---*/

}
/*---queue9---*/
