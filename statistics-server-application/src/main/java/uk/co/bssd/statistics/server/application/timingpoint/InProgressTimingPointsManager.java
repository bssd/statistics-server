package uk.co.bssd.statistics.server.application.timingpoint;

import java.util.HashMap;
import java.util.Map;

import uk.co.bssd.statistics.server.api.dto.StartTimingPointMessage;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointMessage;

public class InProgressTimingPointsManager {

	private final Map<String, InProgressTimingPoints> timingPoints;

	public InProgressTimingPointsManager() {
		this.timingPoints = new HashMap<String, InProgressTimingPoints>();
	}

	public void startTimingPoint(StartTimingPointMessage request) {
		timingPoints(request.name()).startTimingPoint(request);
	}

	public boolean containsStartedTimingPoint(StopTimingPointMessage request) {
		String name = request.name();
		return hasTimingPoints(name)
				&& timingPoints(name).containsStartedTimingPoint(request);
	}

	public TimingPoint stopTimingPointRequest(StopTimingPointMessage request) {
		if (!containsStartedTimingPoint(request)) {
			throw new IllegalStateException(String.format("No timing point started with id [%s] and name [%s]", request.id(), request.name()));
		}
		return timingPoints(request.name()).stopTimingPointRequest(request);
	}

	private InProgressTimingPoints timingPoints(String name) {
		if (!hasTimingPoints(name)) {
			this.timingPoints.put(name, new InProgressTimingPoints());
		}
		return this.timingPoints.get(name);
	}

	private boolean hasTimingPoints(String name) {
		return this.timingPoints.containsKey(name);
	}
}