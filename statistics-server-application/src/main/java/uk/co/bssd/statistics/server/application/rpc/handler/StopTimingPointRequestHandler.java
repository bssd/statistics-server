package uk.co.bssd.statistics.server.application.rpc.handler;

import uk.co.bssd.netty.server.AsynchronousMessageHandler;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointRequest;
import uk.co.bssd.statistics.server.api.service.TimingPointService;

public class StopTimingPointRequestHandler implements AsynchronousMessageHandler<StopTimingPointRequest>{

	private final TimingPointService service;
	
	public StopTimingPointRequestHandler(TimingPointService service) {
		this.service = service;
	}
	
	@Override
	public void onMessage(StopTimingPointRequest message) {
		this.service.stopTimingPoint(message.id(), message.name());
	}
}