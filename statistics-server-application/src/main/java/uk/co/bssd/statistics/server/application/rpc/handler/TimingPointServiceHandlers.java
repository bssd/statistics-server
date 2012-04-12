package uk.co.bssd.statistics.server.application.rpc.handler;

import uk.co.bssd.netty.server.RpcServer;
import uk.co.bssd.statistics.server.api.dto.StartTimingPointRequest;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointRequest;
import uk.co.bssd.statistics.server.api.service.TimingPointService;

public final class TimingPointServiceHandlers {

	private TimingPointServiceHandlers() {
		super();
	}

	public static void bindHandlers(RpcServer rpcServer,
			TimingPointService timingPointService) {
		rpcServer.registerAsynchronousMessageHandler(
				StartTimingPointRequest.class,
				new StartTimingPointRequestHandler(timingPointService));
		rpcServer.registerAsynchronousMessageHandler(
				StopTimingPointRequest.class,
				new StopTimingPointRequestHandler(timingPointService));
	}
}