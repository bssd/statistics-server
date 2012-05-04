package uk.co.bssd.statistics.server.application.statistic;

public class DefaultStatisticsPublisherJobFactory implements
		StatisticsPublisherJobFactory {

	private final StatisticsPublishingService publishingService;

	public DefaultStatisticsPublisherJobFactory(
			StatisticsPublishingService publishingService) {
		this.publishingService = publishingService;
	}

	@Override
	public StatisticsPublisherJob createJob(StatisticsCollector collector) {
		return new StatisticsPublisherJob(collector, this.publishingService);
	}
}