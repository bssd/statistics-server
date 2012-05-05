package uk.co.bssd.statistics.server.application.statistic;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static uk.co.bssd.statistics.server.api.StatisticsServerConstants.NETTY_BROADCAST_CHANNEL_AGGREGATED_STATISTICS;

import java.net.SocketAddress;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.co.bssd.common.scheduler.ManualScheduler;
import uk.co.bssd.netty.client.RpcClient;
import uk.co.bssd.netty.server.RpcServer;
import uk.co.bssd.netty.server.SubscribeListener;
import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessage;
import uk.co.bssd.statistics.server.api.dto.StartTimingPointMessage;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointMessage;

public class StatisticsServerTest {

	private static final TimeUnit TIME_INTERVAL = TimeUnit.SECONDS;

	private static final String SERVER_HOST = "127.0.0.1";
	private static final int SERVER_PORT = 6777;
	
	private static final long CLIENT_CONNECTION_TIMEOUT_MS = 1000;
	private static final int LONG_MESSAGE_WAIT_TIMEOUT = 1000;
	
	private static final UUID TIMING_POINT_ID = UUID.randomUUID();
	private static final String TIMING_POINT_NAME = "name";

	private RpcServer rpcServer;
	private RpcClient rpcClient;

	private ManualScheduler scheduler;
	
	private StatisticsServer server;

	private CountDownLatch subscribeLatch;

	@Before
	public void before() {
		this.rpcServer = new RpcServer();
		registerSubscribeLatch();

		this.scheduler = new ManualScheduler();
		
		this.server = new StatisticsServer(this.rpcServer, SERVER_HOST,
				SERVER_PORT, this.scheduler, TIME_INTERVAL);
		this.server.start();

		this.rpcClient = new RpcClient();
		this.rpcClient.start(SERVER_HOST, SERVER_PORT,
				CLIENT_CONNECTION_TIMEOUT_MS);
		
		subscribeToAggregatedStatistics();
		awaitSubscriptionToComplete();
	}

	@After
	public void after() {
		this.rpcClient.stop();
		this.server.stop();
	}

	@Test
	public void testWhenSchedulerFiresClientReceivesAggregatedStatisticMessage() {
		this.scheduler.executeAll();
		assertThat(awaitStatisticsMessage(), is(notNullValue()));
	}
	
	@Test
	public void testWhenServerHasACompleteTimingPointAndSchedulerFiresAggregatedStatisticMessageContainsTimingPoint() {
		this.rpcClient.sendAsync(new StartTimingPointMessage(TIMING_POINT_ID, TIMING_POINT_NAME, 1));
		this.rpcClient.sendAsync(new StopTimingPointMessage(TIMING_POINT_ID, TIMING_POINT_NAME, 2));
		// TODO : need to get rid of this, must wait until its been received and processed server side.
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		this.scheduler.executeAll();
		assertThat(awaitStatisticsMessage().aggregatedStatisticsCount(), is(1));
	}
	
	private AggregatedStatisticsMessage awaitStatisticsMessage() {
		return (AggregatedStatisticsMessage)this.rpcClient.awaitMessage(LONG_MESSAGE_WAIT_TIMEOUT);
	}

	private void registerSubscribeLatch() {
		this.subscribeLatch = new CountDownLatch(1);
		this.rpcServer.registerSubscribeListener(new SubscribeListener() {
			@Override
			public void onSubscribe(SocketAddress arg0, String arg1) {
				StatisticsServerTest.this.subscribeLatch.countDown();
			}
		});
	}

	private void subscribeToAggregatedStatistics() {
		this.rpcClient.subscribe(NETTY_BROADCAST_CHANNEL_AGGREGATED_STATISTICS);
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