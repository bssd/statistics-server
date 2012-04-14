package uk.co.bssd.statistics.server.application.rpc.handler;

import uk.co.bssd.netty.server.AsynchronousMessageHandler;
import uk.co.bssd.statistics.server.api.dto.StartTimingPointRequest;
import uk.co.bssd.statistics.server.application.service.StatisticsServerFacade;

public class StartTimingPointRequestHandler implements AsynchronousMessageHandler<StartTimingPointRequest>{

	private final StatisticsServerFacade service;
	
	public StartTimingPointRequestHandler(StatisticsServerFacade service) {
		this.service = service;
	}
	
	@Override
	public void onMessage(StartTimingPointRequest message) {
		this.service.startTimingPoint(message);
	}
}