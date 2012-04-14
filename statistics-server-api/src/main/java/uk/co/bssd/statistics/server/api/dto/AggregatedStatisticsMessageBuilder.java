package uk.co.bssd.statistics.server.api.dto;

import java.util.ArrayList;
import java.util.List;

public class AggregatedStatisticsMessageBuilder {

	private final List<AggregatedStatisticsBuilder> aggregatedStatisticsBuilders;
	
	public AggregatedStatisticsMessageBuilder() {
		this.aggregatedStatisticsBuilders = new ArrayList<AggregatedStatisticsBuilder>();
	}
	
	public AggregatedStatisticsMessageBuilder withAggregatedStatistics(AggregatedStatisticsBuilder builder) {
		this.aggregatedStatisticsBuilders.add(builder);
		return this;
	}
	
	public AggregatedStatisticsMessage build() {
		return new AggregatedStatisticsMessage(aggregatedStatistics());
	}
	
	private List<AggregatedStatistics> aggregatedStatistics() {
		List<AggregatedStatistics> aggregatedStatistics = new ArrayList<AggregatedStatistics>();
		
		for (AggregatedStatisticsBuilder builder : this.aggregatedStatisticsBuilders) {
			aggregatedStatistics.add(builder.build());
		}
		
		return aggregatedStatistics;
	}
}
