package uk.co.bssd.statistics.server.application.timingpoint;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import uk.co.bssd.statistics.server.api.dto.StartTimingPointMessage;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointMessage;

public class InProgressTimingPoints {

	private final Map<UUID, TimingPointBuilder> timingPoints;
	
	public InProgressTimingPoints() {
		this.timingPoints = new HashMap<UUID, TimingPointBuilder>();
	}
	
	public void startTimingPoint(StartTimingPointMessage request) {
		TimingPointBuilder builder = TimingPointBuilder.from(request);
		this.timingPoints.put(request.id(), builder);
	}
	
	public boolean containsStartedTimingPoint(StopTimingPointMessage request) {
		return this.timingPoints.containsKey(request.id());
	}
	
	public TimingPoint stopTimingPointRequest(StopTimingPointMessage request) {
		if (!containsStartedTimingPoint(request)) {
			throw new IllegalStateException(String.format("No timing point started with id [%s] and name [%s]", request.id(), request.name()));
		}
		TimingPointBuilder builder = this.timingPoints.remove(request.id());
		return builder.withMillisecondEndTime(request.millisecondTime()).build();
	}
}