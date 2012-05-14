package uk.co.bssd.statistics.server.test.integration.netty;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.co.bssd.common.scheduler.JdkTimerScheduler;
import uk.co.bssd.statistics.server.application.statistic.StatisticsServer;
import uk.co.bssd.vesta.client.RpcClient;
import uk.co.bssd.vesta.server.RpcServer;
import uk.co.statistics.server.client.StatisticsClient;
import uk.co.statistics.server.client.TimingPointClient;
import uk.co.statistics.server.client.tcp.TcpStatisticsClient;
import uk.co.statistics.server.client.tcp.TcpTimingPointClient;

public class NonFunctionalSystemTest {

	private static final String TIMING_POINT_NAME = "t";

	private static final TimeUnit TIME_INTERVAL = TimeUnit.SECONDS;

	private static final String SERVER_HOST = "127.0.0.1";
	private static final int SERVER_PORT = 6777;
	
	private StatisticsServer statisticsServer;

	private RpcClient rpcClient;

	private TimingPointClient timingPointClient;
	private StatisticsClient statisticsClient;

	private CompleteTimingPointCounter completeTimingPointCounter;

	@Before
	public void before() {
		this.statisticsServer = new StatisticsServer(new RpcServer(),
				SERVER_HOST, SERVER_PORT, new JdkTimerScheduler(),
				TIME_INTERVAL);

		this.statisticsServer.start();

		this.rpcClient = new RpcClient();

		this.completeTimingPointCounter = new CompleteTimingPointCounter();

		this.statisticsClient = new TcpStatisticsClient(this.rpcClient, SERVER_HOST, SERVER_PORT);
		this.statisticsClient.start();
		
		this.statisticsClient
				.registerAggregatedStatisticsListener(this.completeTimingPointCounter);

		this.timingPointClient = new TcpTimingPointClient(this.rpcClient);
	}

	@After
	public void after() {
		this.statisticsClient.stop();
		this.statisticsServer.stop();
	}

	@Test
	public void testWhenClientSendsLargeNumberOfTimingPointsInQuickSuccessionOrderIsPreseveredOnTheServerAndAllCompleteCorrectly() {
		int numberTimingPoints = 10000;
		for (int i = 0; i < numberTimingPoints; i++) {
			UUID id = UUID.randomUUID();
			this.timingPointClient.startTimingPoint(id, TIMING_POINT_NAME);
			this.timingPointClient.stopTimingPoint(id, TIMING_POINT_NAME);
		}
		this.completeTimingPointCounter.awaitTimingPointCount(numberTimingPoints, 10000);
	}
}