package uk.co.bssd.statistics.server.test.integration.netty;

import java.util.concurrent.atomic.AtomicInteger;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import uk.co.bssd.statistics.server.api.dto.AggregatedStatistics;
import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessage;
import uk.co.statistics.server.client.AggregatedStatisticsListener;

public class CompleteTimingPointCounter implements AggregatedStatisticsListener {

	private final AtomicInteger counter;

	public CompleteTimingPointCounter() {
		this.counter = new AtomicInteger(0);
	}

	@Override
	public void onMessage(AggregatedStatisticsMessage message) {
		synchronized (this.counter) {
			for (AggregatedStatistics statistics : message) {
				this.counter.addAndGet(statistics.timingPoingCount());
			}
			this.counter.notify();
		}
	}

	public void awaitTimingPointCount(int count, int millisecondTimeout) {
		DateTime timeout = new DateTime().plusMillis(millisecondTimeout);

		synchronized (this.counter) {
			while (timeout.isAfterNow() && !countCorrect(count)) {
				try {
					this.counter.wait(maximumWaitTime(timeout));
				} catch (InterruptedException e) {
				}
			}
			if (!countCorrect(count)) {
				String message = String
						.format("Timed out waiting for timing point count to match, expected [%d], actual [%d]",
								count, this.counter.get());
				throw new IllegalStateException(message);
			}
		}
	}

	private boolean countCorrect(int count) {
		return this.counter.get() == count;
	}

	private long maximumWaitTime(DateTime timeout) {
		long wait = timeout.getMillis() - DateTimeUtils.currentTimeMillis();
		return wait > 0 ? wait : 0;
	}
}