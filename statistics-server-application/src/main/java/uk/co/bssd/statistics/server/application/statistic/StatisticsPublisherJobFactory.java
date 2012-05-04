package uk.co.bssd.statistics.server.application.statistic;

public interface StatisticsPublisherJobFactory {

	StatisticsPublisherJob createJob(StatisticsCollector collector);
}