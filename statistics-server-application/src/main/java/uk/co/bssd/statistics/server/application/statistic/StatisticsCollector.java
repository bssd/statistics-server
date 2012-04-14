package uk.co.bssd.statistics.server.application.statistic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import uk.co.bssd.statistics.server.application.timingpoint.TimingPoint;
import uk.co.bssd.statistics.server.application.timingpoint.TimingPointsCollector;

public class StatisticsCollector implements TimingPointsCollector {

	private final Map<String, StatisticsBucket> statisticsBuckets;

	public StatisticsCollector() {
		this.statisticsBuckets = new HashMap<String, StatisticsBucket>();
	}

	@Override
	public synchronized void addTimingPoint(TimingPoint timingPoint) {
		if (!containsStatisticsBucket(timingPoint)) {
			addStatisticsBucket(timingPoint);
		}
		statisticsBucket(timingPoint).addTimingPoint(timingPoint);
	}

	public synchronized Set<StatisticsBucket> clearBuckets() {
		Set<StatisticsBucket> buckets = new HashSet<StatisticsBucket>(this.statisticsBuckets.values());
		this.statisticsBuckets.clear();
		return buckets;
	}

	private StatisticsBucket statisticsBucket(TimingPoint timingPoint) {
		return this.statisticsBuckets.get(timingPoint.name());
	}

	private void addStatisticsBucket(TimingPoint timingPoint) {
		String timingPointName = timingPoint.name();
		this.statisticsBuckets.put(timingPointName, new StatisticsBucket(
				timingPointName));
	}

	private boolean containsStatisticsBucket(TimingPoint timingPoint) {
		return this.statisticsBuckets.containsKey(timingPoint.name());
	}
}
