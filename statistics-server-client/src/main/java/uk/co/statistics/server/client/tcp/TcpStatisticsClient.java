package uk.co.statistics.server.client.tcp;

import static uk.co.bssd.statistics.server.api.StatisticsServerConstants.NETTY_BROADCAST_CHANNEL_AGGREGATED_STATISTICS;

import java.io.Serializable;

import uk.co.bssd.netty.client.RpcClient;
import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessage;
import uk.co.statistics.server.client.AggregatedStatisticsListener;
import uk.co.statistics.server.client.Listeners;
import uk.co.statistics.server.client.StatisticsClient;

public class TcpStatisticsClient implements StatisticsClient {

	private final RpcClient client;
	private final Listeners<AggregatedStatisticsMessage, AggregatedStatisticsListener> aggregatedStatisticsListeners;

	private static class MessageReceivingTask implements Runnable {
		private final RpcClient c;
		private final Listeners<AggregatedStatisticsMessage, AggregatedStatisticsListener> l;

		public MessageReceivingTask(RpcClient c, Listeners<AggregatedStatisticsMessage, AggregatedStatisticsListener> l) {
			this.c = c;
			this.l = l;
		}

		@Override
		public void run() {
			while (true) {
				Serializable message = c.awaitMessage(Long.MAX_VALUE);
				if (message != null) {
					l.notifyListeners((AggregatedStatisticsMessage)message);
				}
			}
		}
	}

	public TcpStatisticsClient(RpcClient client) {
		this.client = client;
		this.aggregatedStatisticsListeners = new Listeners<AggregatedStatisticsMessage, AggregatedStatisticsListener>();
		Thread t = new Thread(new MessageReceivingTask(this.client, this.aggregatedStatisticsListeners));
		t.start();
	}

	@Override
	public void registerAggregatedStatisticsListener(
			AggregatedStatisticsListener listener) {
		registerForAggregatedStatisticsIfNeccessary();
		this.aggregatedStatisticsListeners.registerListener(listener);
	}

	@Override
	public void unregisterAggregatedStatisticsListener(
			AggregatedStatisticsListener listener) {
		this.aggregatedStatisticsListeners.unregisterListener(listener);
		unregisterForAggregatedStatisticsIfNoListenersLeft();
	}

	private void registerForAggregatedStatisticsIfNeccessary() {
		if (this.aggregatedStatisticsListeners.countListeners() == 0) {
			this.client
					.subscribe(NETTY_BROADCAST_CHANNEL_AGGREGATED_STATISTICS);
		}
	}

	private void unregisterForAggregatedStatisticsIfNoListenersLeft() {
		if (this.aggregatedStatisticsListeners.countListeners() == 0) {
			this.client
					.unsubscribe(NETTY_BROADCAST_CHANNEL_AGGREGATED_STATISTICS);
		}
	}
}