package uk.co.bssd.statistics.server.application.rpc.handler;

import uk.co.bssd.netty.server.AsynchronousMessageHandler;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointRequest;
import uk.co.bssd.statistics.server.application.service.StatisticsServerFacade;

public class StopTimingPointRequestHandler implements AsynchronousMessageHandler<StopTimingPointRequest>{

	private final StatisticsServerFacade service;
	
	public StopTimingPointRequestHandler(StatisticsServerFacade service) {
		this.service = service;
	}
	
	@Override
	public void onMessage(StopTimingPointRequest message) {
		this.service.stopTimingPoint(message);
	}
}