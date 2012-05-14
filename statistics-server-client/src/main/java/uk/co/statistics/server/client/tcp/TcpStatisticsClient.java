package uk.co.statistics.server.client.tcp;

import static uk.co.bssd.statistics.server.api.StatisticsServerConstants.NETTY_BROADCAST_CHANNEL_AGGREGATED_STATISTICS;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessage;
import uk.co.bssd.vesta.client.RpcClient;
import uk.co.statistics.server.client.AggregatedStatisticsListener;
import uk.co.statistics.server.client.Listeners;
import uk.co.statistics.server.client.StatisticsClient;

public class TcpStatisticsClient implements StatisticsClient {

	private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 1000;
	
	private final RpcClient client;
	private final String host;
	private final int port;
	private final Listeners<AggregatedStatisticsMessage, AggregatedStatisticsListener> aggregatedStatisticsListeners;
	private final MessageReceivingTask messageReceivingTask;
	private final Thread messageReceivingThread;

	private static class MessageReceivingTask implements Runnable {
		private final RpcClient c;
		private final Listeners<AggregatedStatisticsMessage, AggregatedStatisticsListener> l;
		private final AtomicBoolean running;
		
		public MessageReceivingTask(RpcClient c, Listeners<AggregatedStatisticsMessage, AggregatedStatisticsListener> l) {
			this.c = c;
			this.l = l;
			this.running = new AtomicBoolean(true);
		}

		@Override
		public void run() {
			while (this.running.get()) {
				Serializable message = c.awaitMessage(Long.MAX_VALUE);
				if (message != null) {
					l.notifyListeners((AggregatedStatisticsMessage)message);
				}
			}
		}
		
		public void stop() {
			this.running.set(false);
		}
	}

	public TcpStatisticsClient(RpcClient client, String host, int port) {
		this.client = client;
		this.host = host;
		this.port = port;
		this.aggregatedStatisticsListeners = new Listeners<AggregatedStatisticsMessage, AggregatedStatisticsListener>();
		this.messageReceivingTask = new MessageReceivingTask(this.client, this.aggregatedStatisticsListeners);
		this.messageReceivingThread = new Thread(messageReceivingTask);
		this.messageReceivingThread.setDaemon(true);
	}
	
	@Override
	public void start() {
		this.client.start(this.host, this.port, DEFAULT_CONNECTION_TIMEOUT_MS);
		this.messageReceivingThread.start();
	}
	
	@Override
	public void stop() {
		this.messageReceivingTask.stop();
		this.messageReceivingThread.interrupt();
		this.client.stop();
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