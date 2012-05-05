package uk.co.statistics.server.client;

public interface StatisticsClient {

	void registerAggregatedStatisticsListener(AggregatedStatisticsListener listener);
	
	void unregisterAggregatedStatisticsListener(AggregatedStatisticsListener listener);
}