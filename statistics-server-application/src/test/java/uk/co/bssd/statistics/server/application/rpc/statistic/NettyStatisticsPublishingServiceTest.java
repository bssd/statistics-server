package uk.co.bssd.statistics.server.application.rpc.statistic;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static uk.co.bssd.statistics.server.api.StatisticsServerConstants.NETTY_BROADCAST_CHANNEL_AGGREGATED_STATISTICS;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessage;
import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessageBuilder;
import uk.co.bssd.statistics.server.application.statistic.StatisticsPublishingService;
import uk.co.bssd.vesta.client.RpcClient;
import uk.co.bssd.vesta.server.RpcServer;
import uk.co.bssd.vesta.server.SubscribeListener;

public class NettyStatisticsPublishingServiceTest {

	private static final int SHORT_MESSAGE_WAIT_TIMEOUT = 50;
	private static final int LONG_MESSAGE_WAIT_TIMEOUT = 1000;

	private static final String HOSTNAME = "127.0.0.1";
	private static final int PORT = 5676;
	private static final long CLIENT_CONNECTION_TIMEOUT_MS = 1000;

	private RpcServer server;
	private RpcClient client;

	private CountDownLatch subscribeLatch;

	private AggregatedStatisticsMessage aggregatedStatisticsMessage;

	private StatisticsPublishingService publishingService;

	@Before
	public void before() {
		this.server = new RpcServer();
		this.server.start(HOSTNAME, PORT);
		registerSubscribeLatch();

		this.client = new RpcClient();
		this.client.start(HOSTNAME, PORT, CLIENT_CONNECTION_TIMEOUT_MS);

		this.aggregatedStatisticsMessage = new AggregatedStatisticsMessageBuilder()
				.build();

		this.publishingService = new NettyStatisticsPublishingService(
				this.server);
	}

	@After
	public void after() {
		this.client.stop();
		this.server.stop();
	}

	@Test
	public void testClientThatHasNotSubcribedToAggregatedStatisticsDoesNotReceiveAggregatedStatisticsMessageWhenBroadcast() {
		this.publishingService.publish(this.aggregatedStatisticsMessage);
		Serializable message = this.client
				.awaitMessage(SHORT_MESSAGE_WAIT_TIMEOUT);
		assertThat(message, is(nullValue()));
	}

	@Test
	public void testClientThatHasSubscribedToAggregatedStatisticsDoesReceiveAggregatedStatisticsMessageWhenBroadcast() {
		subscribeToAggregatedStatistics();
		this.publishingService.publish(this.aggregatedStatisticsMessage);
		Serializable message = this.client
				.awaitMessage(LONG_MESSAGE_WAIT_TIMEOUT);
		assertThat(message, is(notNullValue()));
	}

	private void registerSubscribeLatch() {
		this.subscribeLatch = new CountDownLatch(1);
		this.server.registerSubscribeListener(new SubscribeListener() {
			@Override
			public void onSubscribe(SocketAddress arg0, String arg1) {
				NettyStatisticsPublishingServiceTest.this.subscribeLatch
						.countDown();
			}
		});
	}

	private void subscribeToAggregatedStatistics() {
		this.client.subscribe(NETTY_BROADCAST_CHANNEL_AGGREGATED_STATISTICS);
		awaitSubscriptionToComplete();
	}

	private void awaitSubscriptionToComplete() {
		try {
			this.subscribeLatch.await(LONG_MESSAGE_WAIT_TIMEOUT,
					TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}

		if (this.subscribeLatch.getCount() > 0) {
			fail("Subscription didn't complete in expected time");
		}
	}
}