package uk.co.bssd.statistics.server.application.statistic;

import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessage;

public interface StatisticsPublishingService {

	void publish(AggregatedStatisticsMessage message);
}