package uk.co.statistics.server.client;

public interface StatisticsClient {

	void start();
	
	void stop();
	
	void registerAggregatedStatisticsListener(AggregatedStatisticsListener listener);
	
	void unregisterAggregatedStatisticsListener(AggregatedStatisticsListener listener);
}