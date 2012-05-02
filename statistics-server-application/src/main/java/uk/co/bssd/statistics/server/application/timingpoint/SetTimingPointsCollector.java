package uk.co.bssd.statistics.server.application.timingpoint;

import java.util.HashSet;
import java.util.Set;

public class SetTimingPointsCollector implements TimingPointsCollector {

	private final Set<TimingPointsCollector> collectors;

	public SetTimingPointsCollector() {
		this.collectors = new HashSet<TimingPointsCollector>();
	}

	public void registerCollector(TimingPointsCollector collector) {
		this.collectors.add(collector);
	}

	@Override
	public void addTimingPoint(TimingPoint timingPoint) {
		for (TimingPointsCollector collector : this.collectors) {
			try {
				collector.addTimingPoint(timingPoint);
			} catch (RuntimeException e) {
				// TODO : log exception and carry on
			}
		}
	}
	
	public int countCollectors() {
		return this.collectors.size();
	}
}