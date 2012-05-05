package uk.co.statistics.server.client;

import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessage;

public interface AggregatedStatisticsListener extends
		Listener<AggregatedStatisticsMessage> {

	void onMessage(AggregatedStatisticsMessage message);
}