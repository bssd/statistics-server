package uk.co.bssd.statistics.server.application.statistic;

import java.util.concurrent.TimeUnit;

public interface StatisticsPublisherJobFactory {

	StatisticsPublisherJob createJob(TimeUnit interval, StatisticsCollector collector);
}