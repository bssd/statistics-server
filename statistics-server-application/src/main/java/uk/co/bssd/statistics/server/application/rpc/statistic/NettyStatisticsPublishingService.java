package uk.co.bssd.statistics.server.application.rpc.statistic;

import uk.co.bssd.netty.server.RpcServer;
import uk.co.bssd.statistics.server.api.StatisticsServerConstants;
import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessage;
import uk.co.bssd.statistics.server.application.statistic.StatisticsPublishingService;

public class NettyStatisticsPublishingService implements
		StatisticsPublishingService {

	private final RpcServer rpcServer;
	
	public NettyStatisticsPublishingService(RpcServer server) {
		this.rpcServer = server;
	}
	
	@Override
	public void publish(AggregatedStatisticsMessage message) {
		this.rpcServer.broadcast(message, StatisticsServerConstants.NETTY_BROADCAST_CHANNEL_AGGREGATED_STATISTICS);

	}
}