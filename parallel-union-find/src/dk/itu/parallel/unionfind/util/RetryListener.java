package dk.itu.parallel.unionfind.util;

import java.util.concurrent.atomic.AtomicLong;

import org.multiverse.api.Txn;
import org.multiverse.api.lifecycle.TxnEvent;
import org.multiverse.api.lifecycle.TxnListener;

/**
 * Listens to the <b>PostAbort</b> event of a transaction object
 * and books each occurrence of such an event as a re-try.
 *
 * Thread safe since the internal counter is of type AtomicLong.
 */
public class RetryListener implements TxnListener {

	final AtomicLong retries;

	public RetryListener() {
		retries = new AtomicLong(0);
	}

	@Override
	public void notify(final Txn txn, final TxnEvent e) {
		if (e == TxnEvent.PostCommit)
			retries.addAndGet(txn.getAttempt() - 1);
	}

	public long retries() {
		return retries.get();
	}

	public void reset() {
		retries.set(0);
	}

}
