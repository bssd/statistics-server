package uk.co.bssd.statistics.server.application.statistic;

import java.util.Collection;

import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsBuilder;
import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessage;
import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessageBuilder;

public class StatisticsMessageAdapter {

	public static AggregatedStatisticsMessage from(Collection<StatisticsBucket> buckets) {
		AggregatedStatisticsMessageBuilder messageBuilder = new AggregatedStatisticsMessageBuilder();
		
		for (StatisticsBucket bucket : buckets) {
			messageBuilder.withAggregatedStatistics(toBuilder(bucket));
		}
		return messageBuilder.build();
	}

	private static AggregatedStatisticsBuilder toBuilder(StatisticsBucket bucket) {
		AggregatedStatisticsBuilder builder = new AggregatedStatisticsBuilder();
		builder.withTimingPointName(bucket.timingPointName());
		builder.withTimingPointCount(bucket.timingPointCount());
		builder.withTotalMillisecondTime(bucket.totalMillisecondTime());
		builder.withAverageMillisecondTime(bucket.averageMillisecondTime());
		builder.withMinimumMillisecondTime(bucket.minimumMillisecondTime());
		builder.withMaximumMillisecondTime(bucket.maximumMillisecondTime());
		return builder;
	}
}