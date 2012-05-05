package uk.co.bssd.statistics.server.application.statistic;

import java.util.concurrent.TimeUnit;

import uk.co.bssd.common.scheduler.Scheduler;
import uk.co.bssd.netty.server.RpcServer;
import uk.co.bssd.statistics.server.application.rpc.handler.TimingPointServiceHandlers;
import uk.co.bssd.statistics.server.application.rpc.statistic.NettyStatisticsPublishingService;
import uk.co.bssd.statistics.server.application.service.InMemoryStatisticsServerFacade;
import uk.co.bssd.statistics.server.application.service.StatisticsServerFacade;
import uk.co.bssd.statistics.server.application.timingpoint.TimingPointsCollector;

public class StatisticsServer {

	private final RpcServer rpcServer;
	private final String serverHost;
	private final int serverPort;
	
	private final TimingPointsCollector timingPointsCollector;
	
	public StatisticsServer(RpcServer server, String host, int port, Scheduler scheduler, TimeUnit... intervals) {
		this.rpcServer = server;
		this.serverHost = host;
		this.serverPort = port;
		
		TimeIntervalCollectorsFactory intervalCollectors = intervalCollectors(
				server, scheduler);
		
		this.timingPointsCollector = intervalCollectors.forIntervals(intervals);
		
		StatisticsServerFacade statisticsServerFacade = new InMemoryStatisticsServerFacade(this.timingPointsCollector);
		TimingPointServiceHandlers.bindHandlers(server, statisticsServerFacade);
	}

	public void start() {
		this.rpcServer.start(this.serverHost, this.serverPort);
	}

	public void stop() {
		this.rpcServer.stop();
	}
	
	private TimeIntervalCollectorsFactory intervalCollectors(RpcServer server,
			Scheduler scheduler) {
		StatisticsPublishingService publishingService = new NettyStatisticsPublishingService(server);
		StatisticsPublisherJobFactory jobFactory = new DefaultStatisticsPublisherJobFactory(publishingService);
		return new TimeIntervalCollectorsFactory(scheduler, jobFactory);
	}
}