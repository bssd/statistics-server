package uk.co.bssd.statistics.server.application.statistic;

public class StatisticsPublisherJob implements Runnable {

	private final StatisticsPublisher publisher;

	public StatisticsPublisherJob(StatisticsCollector collector,
			StatisticsPublishingService publisher) {
		this.publisher = new StatisticsPublisher(collector, publisher);
	}

	@Override
	public void run() {
		this.publisher.publish();
	}
}