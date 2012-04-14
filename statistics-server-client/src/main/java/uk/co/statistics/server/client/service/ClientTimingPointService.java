package uk.co.statistics.server.client.service;

import java.util.UUID;

import org.joda.time.DateTime;

import uk.co.bssd.netty.client.RpcClient;
import uk.co.bssd.statistics.server.api.dto.StartTimingPointRequest;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointRequest;
import uk.co.bssd.statistics.server.api.service.TimingPointService;

public class ClientTimingPointService implements TimingPointService {

	private final RpcClient client;
	
	public ClientTimingPointService(RpcClient client) {
		this.client = client;
	}

	@Override
	public void startTimingPoint(UUID id, String name) {
		StartTimingPointRequest request = new StartTimingPointRequest(id, name, nowInMillis());
		this.client.sendAsync(request);
	}
	
	@Override
	public void stopTimingPoint(UUID id, String name) {
		StopTimingPointRequest request = new StopTimingPointRequest(id, name, nowInMillis());
		this.client.sendAsync(request);
	}
	
	private long nowInMillis() {
		return new DateTime().getMillis();
	}
}