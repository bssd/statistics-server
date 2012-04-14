package uk.co.bssd.statistics.server.api.dto;

import java.util.ArrayList;
import java.util.List;

public class StatisticsMessageBuilder {

	private final List<AggregatedStatisticsBuilder> aggregatedStatisticsBuilders;
	
	public StatisticsMessageBuilder() {
		this.aggregatedStatisticsBuilders = new ArrayList<AggregatedStatisticsBuilder>();
	}
	
	public StatisticsMessageBuilder withAggregatedStatistics(AggregatedStatisticsBuilder builder) {
		this.aggregatedStatisticsBuilders.add(builder);
		return this;
	}
	
	public StatisticsMessage build() {
		return new StatisticsMessage(aggregatedStatistics());
	}
	
	private List<AggregatedStatistics> aggregatedStatistics() {
		List<AggregatedStatistics> aggregatedStatistics = new ArrayList<AggregatedStatistics>();
		
		for (AggregatedStatisticsBuilder builder : this.aggregatedStatisticsBuilders) {
			aggregatedStatistics.add(builder.build());
		}
		
		return aggregatedStatistics;
	}
}
