package uk.co.bssd.statistics.server.api.dto;

import java.io.Serializable;

public class AggregatedStatistics implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private final String timingPointName;
	private final int timingPountCount;
	private final long totalMillisecondTime;
	private final double averageMillisecondTime;
	private final long minimumMillisecondTime;
	private final long maximumMillisecondTime;
	
	public AggregatedStatistics(String name, int count, long total, double average, long minimum, long maximum) {
		this.timingPointName = name;
		this.timingPountCount = count;
		this.totalMillisecondTime = total;
		this.averageMillisecondTime = average;
		this.minimumMillisecondTime = minimum;
		this.maximumMillisecondTime = maximum;
	}
	
	public String timingPointName() {
		return this.timingPointName;
	}
	
	public int count() {
		return this.timingPountCount;
	}
	
	public long totalMillisecondTime() {
		return this.totalMillisecondTime;
	}
	
	public double averageMillisecondTime() {
		return this.averageMillisecondTime;
	}
	
	public long minimumMillisecondTime() {
		return this.minimumMillisecondTime;
	}

	public long maximumMillisecondTime() {
		return this.maximumMillisecondTime;
	}
}