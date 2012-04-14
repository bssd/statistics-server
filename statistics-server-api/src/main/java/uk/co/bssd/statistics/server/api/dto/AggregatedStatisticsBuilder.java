package uk.co.bssd.statistics.server.api.dto;

public class AggregatedStatisticsBuilder {

	private String timingPointName;
	private int timingPointCount;
	private long totalMillisecondTime;
	private double averageMillisecondTime;
	private long minimumMillisecondTime;
	private long maximumMillisecondTime;

	public AggregatedStatisticsBuilder() {
		super();
	}

	public AggregatedStatisticsBuilder withTimingPointName(String name) {
		this.timingPointName = name;
		return this;
	}

	public AggregatedStatisticsBuilder withTimingPointCount(int count) {
		this.timingPointCount = count;
		return this;
	}

	public AggregatedStatisticsBuilder withTotalMillisecondTime(long time) {
		this.totalMillisecondTime = time;
		return this;
	}

	public AggregatedStatisticsBuilder withAverageMillisecondTime(double time) {
		this.averageMillisecondTime = time;
		return this;
	}

	public AggregatedStatisticsBuilder withMinimumMillisecondTime(long time) {
		this.minimumMillisecondTime = time;
		return this;
	}

	public AggregatedStatisticsBuilder withMaximumMillisecondTime(long time) {
		this.maximumMillisecondTime = time;
		return this;
	}

	public AggregatedStatistics build() {
		return new AggregatedStatistics(this.timingPointName,
				this.timingPointCount, this.totalMillisecondTime,
				this.averageMillisecondTime, this.minimumMillisecondTime,
				this.maximumMillisecondTime);
	}
}