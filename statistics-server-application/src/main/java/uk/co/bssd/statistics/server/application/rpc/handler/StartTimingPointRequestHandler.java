package uk.co.bssd.statistics.server.application.rpc.handler;

import uk.co.bssd.statistics.server.api.dto.StartTimingPointMessage;
import uk.co.bssd.statistics.server.application.service.StatisticsServerFacade;
import uk.co.bssd.vesta.server.AsynchronousMessageHandler;

public class StartTimingPointRequestHandler implements AsynchronousMessageHandler<StartTimingPointMessage>{

	private final StatisticsServerFacade service;
	
	public StartTimingPointRequestHandler(StatisticsServerFacade service) {
		this.service = service;
	}
	
	@Override
	public void onMessage(StartTimingPointMessage message) {
		this.service.startTimingPoint(message);
	}
}