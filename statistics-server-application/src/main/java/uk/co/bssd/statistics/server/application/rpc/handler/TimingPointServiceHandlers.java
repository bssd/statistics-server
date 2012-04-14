package uk.co.bssd.statistics.server.application.rpc.handler;

import uk.co.bssd.netty.server.RpcServer;
import uk.co.bssd.statistics.server.api.dto.StartTimingPointRequest;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointRequest;
import uk.co.bssd.statistics.server.application.service.StatisticsServerFacade;

public final class TimingPointServiceHandlers {

	private TimingPointServiceHandlers() {
		super();
	}

	public static void bindHandlers(RpcServer rpcServer,
			StatisticsServerFacade statisticsServerFacade) {
		rpcServer.registerAsynchronousMessageHandler(
				StartTimingPointRequest.class,
				new StartTimingPointRequestHandler(statisticsServerFacade));
		rpcServer.registerAsynchronousMessageHandler(
				StopTimingPointRequest.class,
				new StopTimingPointRequestHandler(statisticsServerFacade));
	}
}