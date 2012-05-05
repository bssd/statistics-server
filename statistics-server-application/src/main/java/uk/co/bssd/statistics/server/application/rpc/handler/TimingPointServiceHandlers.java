package uk.co.bssd.statistics.server.application.rpc.handler;

import uk.co.bssd.netty.server.RpcServer;
import uk.co.bssd.statistics.server.api.dto.StartTimingPointMessage;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointMessage;
import uk.co.bssd.statistics.server.application.service.StatisticsServerFacade;

public final class TimingPointServiceHandlers {

	private TimingPointServiceHandlers() {
		super();
	}

	public static void bindHandlers(RpcServer rpcServer,
			StatisticsServerFacade statisticsServerFacade) {
		rpcServer.registerAsynchronousMessageHandler(
				StartTimingPointMessage.class,
				new StartTimingPointRequestHandler(statisticsServerFacade));
		rpcServer.registerAsynchronousMessageHandler(
				StopTimingPointMessage.class,
				new StopTimingPointRequestHandler(statisticsServerFacade));
	}
}