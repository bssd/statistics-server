package uk.co.bssd.statistics.server.application.statistic;

import uk.co.bssd.statistics.server.api.dto.StatisticsMessage;

public interface StatisticsPublishingService {

	void publish(StatisticsMessage message);
}