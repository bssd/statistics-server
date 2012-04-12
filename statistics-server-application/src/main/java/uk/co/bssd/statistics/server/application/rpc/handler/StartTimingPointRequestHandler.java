package uk.co.bssd.statistics.server.application.rpc.handler;

import uk.co.bssd.netty.server.AsynchronousMessageHandler;
import uk.co.bssd.statistics.server.api.dto.StartTimingPointRequest;
import uk.co.bssd.statistics.server.api.service.TimingPointService;

public class StartTimingPointRequestHandler implements AsynchronousMessageHandler<StartTimingPointRequest>{

	private final TimingPointService service;
	
	public StartTimingPointRequestHandler(TimingPointService service) {
		this.service = service;
	}
	
	@Override
	public void onMessage(StartTimingPointRequest message) {
		this.service.startTimingPoint(message.id(), message.name());
	}
}