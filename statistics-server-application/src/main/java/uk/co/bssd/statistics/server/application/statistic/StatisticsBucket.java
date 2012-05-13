package uk.co.bssd.statistics.server.application.statistic;

import uk.co.bssd.statistics.server.application.timingpoint.TimingPoint;
import uk.co.bssd.statistics.server.application.timingpoint.TimingPointsCollector;

public class StatisticsBucket implements TimingPointsCollector {

	private final String timingPointName;
	private int timingPointCount;
	private Long minimumDuration;
	private Long maximumDuration;
	private long totalDuration;

	public StatisticsBucket(String timingPointName) {
		this.timingPointName = timingPointName;
	}

	public String timingPointName() {
		return this.timingPointName;
	}

	@Override
	public void addTimingPoint(TimingPoint timingPoint) {
		this.timingPointCount++;

		long duration = timingPoint.duration();
		this.totalDuration += duration;

		if (this.minimumDuration == null
				|| this.minimumDuration.longValue() > duration) {
			this.minimumDuration = duration;
		}

		if (this.maximumDuration == null
				|| this.maximumDuration.longValue() < duration) {
			this.maximumDuration = duration;
		}
	}

	public int timingPointCount() {
		return this.timingPointCount;
	}

	public long totalMillisecondTime() {
		return this.totalDuration;
	}

	public double averageMillisecondTime() {
		return this.totalDuration / timingPointCount();
	}

	public long minimumMillisecondTime() {
		return this.minimumDuration;
	}

	public long maximumMillisecondTime() {
		return this.maximumDuration;
	}
}