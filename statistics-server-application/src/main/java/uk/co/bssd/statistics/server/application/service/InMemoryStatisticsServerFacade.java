package uk.co.bssd.statistics.server.application.service;

import uk.co.bssd.statistics.server.api.dto.StartTimingPointMessage;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointMessage;
import uk.co.bssd.statistics.server.application.timingpoint.InProgressTimingPointsManager;
import uk.co.bssd.statistics.server.application.timingpoint.TimingPoint;
import uk.co.bssd.statistics.server.application.timingpoint.TimingPointsCollector;

public class InMemoryStatisticsServerFacade implements StatisticsServerFacade {

	private final TimingPointsCollector timingPointsCollector;
	private final InProgressTimingPointsManager timingPointsManager;

	public InMemoryStatisticsServerFacade(TimingPointsCollector collector) {
		this.timingPointsCollector = collector;
		this.timingPointsManager = new InProgressTimingPointsManager();
	}

	@Override
	public void startTimingPoint(StartTimingPointMessage request) {
		this.timingPointsManager.startTimingPoint(request);
	}

	@Override
	public void stopTimingPoint(StopTimingPointMessage request) {
		if (this.timingPointsManager.containsStartedTimingPoint(request)) {
			TimingPoint timingPoint = this.timingPointsManager
					.stopTimingPointRequest(request);
			this.timingPointsCollector.addTimingPoint(timingPoint);
		} else {
			// TODO : log no timing point started
		}
	}
}