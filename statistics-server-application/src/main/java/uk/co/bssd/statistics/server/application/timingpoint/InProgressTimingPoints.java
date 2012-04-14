package uk.co.bssd.statistics.server.application.timingpoint;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import uk.co.bssd.statistics.server.api.dto.StartTimingPointRequest;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointRequest;

public class InProgressTimingPoints {

	private final Map<UUID, TimingPointBuilder> timingPoints;
	
	public InProgressTimingPoints() {
		this.timingPoints = new HashMap<UUID, TimingPointBuilder>();
	}
	
	public void startTimingPoint(StartTimingPointRequest request) {
		TimingPointBuilder builder = TimingPointBuilder.from(request);
		this.timingPoints.put(request.id(), builder);
	}
	
	public boolean containsStartedTimingPoint(StopTimingPointRequest request) {
		return this.timingPoints.containsKey(request.id());
	}
	
	public TimingPoint stopTimingPointRequest(StopTimingPointRequest request) {
		if (!containsStartedTimingPoint(request)) {
			throw new IllegalStateException(String.format("No timing point started with id [%s] and name [%s]", request.id(), request.name()));
		}
		TimingPointBuilder builder = this.timingPoints.remove(request.id());
		return builder.withMillisecondEndTime(request.millisecondTime()).build();
	}
}