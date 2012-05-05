package uk.co.bssd.statistics.server.application.statistic;

import java.util.concurrent.TimeUnit;

import uk.co.bssd.common.scheduler.Scheduler;
import uk.co.bssd.statistics.server.application.timingpoint.SetTimingPointsCollector;
import uk.co.bssd.statistics.server.application.timingpoint.TimingPointsCollector;

public final class TimeIntervalCollectorsFactory {

	private static final int TIME_UNIT = 1;
	
	private final Scheduler scheduler;
	private final StatisticsPublisherJobFactory publisherJobFactory;
	
	public TimeIntervalCollectorsFactory(Scheduler scheduler, StatisticsPublisherJobFactory factory) {
		this.scheduler = scheduler;
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
			
			StatisticsPublisherJob publisherJob = this.publisherJobFactory.createJob(collector);
			this.scheduler.scheduleAtFixedRate(publisherJob, TIME_UNIT, interval);
		}
		
		return collectors;
	}
}