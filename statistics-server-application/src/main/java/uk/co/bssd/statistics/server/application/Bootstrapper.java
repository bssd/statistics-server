package uk.co.bssd.statistics.server.application;

import java.util.concurrent.TimeUnit;

import uk.co.bssd.common.scheduler.JdkTimerScheduler;
import uk.co.bssd.statistics.server.application.rpc.handler.TimingPointServiceHandlers;
import uk.co.bssd.statistics.server.application.rpc.statistic.NettyStatisticsPublishingService;
import uk.co.bssd.statistics.server.application.service.InMemoryStatisticsServerFacade;
import uk.co.bssd.statistics.server.application.statistic.DefaultStatisticsPublisherJobFactory;
import uk.co.bssd.statistics.server.application.statistic.StatisticsPublishingService;
import uk.co.bssd.statistics.server.application.statistic.TimeIntervalCollectorsFactory;
import uk.co.bssd.statistics.server.application.timingpoint.TimingPointsCollector;
import uk.co.bssd.vesta.server.RpcServer;

public class Bootstrapper {

	public static void main(String[] args) {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8081;
		}
		RpcServer rpcServer = new RpcServer();
		rpcServer.start("localhost", port);
		
		StatisticsPublishingService publishingService = new NettyStatisticsPublishingService(rpcServer);
		
		TimeIntervalCollectorsFactory collectorsFactory = new TimeIntervalCollectorsFactory(new JdkTimerScheduler(), new DefaultStatisticsPublisherJobFactory(publishingService));
		TimingPointsCollector timingPointsCollector = collectorsFactory.forIntervals(TimeUnit.SECONDS);
		
		TimingPointServiceHandlers.bindHandlers(rpcServer, new InMemoryStatisticsServerFacade(timingPointsCollector));
	}
}