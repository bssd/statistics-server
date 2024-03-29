package uk.co.bssd.statistics.server.application.rpc.handler;

import uk.co.bssd.statistics.server.api.dto.StopTimingPointMessage;
import uk.co.bssd.statistics.server.application.service.StatisticsServerFacade;
import uk.co.bssd.vesta.server.AsynchronousMessageHandler;

public class StopTimingPointRequestHandler implements AsynchronousMessageHandler<StopTimingPointMessage>{

	private final StatisticsServerFacade service;
	
	public StopTimingPointRequestHandler(StatisticsServerFacade service) {
		this.service = service;
	}
	
	@Override
	public void onMessage(StopTimingPointMessage message) {
		this.service.stopTimingPoint(message);
	}
}