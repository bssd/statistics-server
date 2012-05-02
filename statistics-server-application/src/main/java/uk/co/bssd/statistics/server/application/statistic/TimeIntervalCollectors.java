package uk.co.bssd.statistics.server.application.statistic;

import java.util.concurrent.TimeUnit;

import javax.xml.bind.ValidationEvent;

import uk.co.bssd.statistics.server.application.timingpoint.SetTimingPointsCollector;
import uk.co.bssd.statistics.server.application.timingpoint.TimingPointsCollector;

public final class TimeIntervalCollectors {

	private StatisticsPublisherJobFactory publisherJobFactory;
	
	public TimeIntervalCollectors(StatisticsPublisherJobFactory factory) {
		this.publisherJobFactory = factory;
	}
	
	public TimingPointsCollector forIntervals(TimeUnit... intervals) {
		if (intervals.length == 0) {
			throw new IllegalArgumentException("At least one time interval must be specified");
		}

		SetTimingPointsCollector collectors = new SetTimingPointsCollector();
		
		for (TimeUnit interval : intervals) {
			StatisticsCollector collector = new StatisticsCollector();
			collectors.registerCollector(collector);
			
			this.publisherJobFactory.createJob(interval, collector);
		}
		
		return collectors;
	}
}