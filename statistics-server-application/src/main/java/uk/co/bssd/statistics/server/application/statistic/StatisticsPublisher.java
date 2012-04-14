package uk.co.bssd.statistics.server.application.statistic;

import java.util.Collection;

import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessage;

public class StatisticsPublisher {

	private final StatisticsCollector statisticsCollector;
	private final StatisticsPublishingService publishingService;
	
	public StatisticsPublisher(StatisticsCollector collector, StatisticsPublishingService service) {
		this.statisticsCollector = collector;
		this.publishingService = service;
	}
	
	public void publish() {
		Collection<StatisticsBucket> buckets = this.statisticsCollector.clearBuckets();
		AggregatedStatisticsMessage message = StatisticsMessageAdapter.from(buckets);
		this.publishingService.publish(message);
	}
}