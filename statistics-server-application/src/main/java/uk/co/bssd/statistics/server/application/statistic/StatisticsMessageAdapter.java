package uk.co.bssd.statistics.server.application.statistic;

import java.util.Collection;

import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsBuilder;
import uk.co.bssd.statistics.server.api.dto.StatisticsMessage;
import uk.co.bssd.statistics.server.api.dto.StatisticsMessageBuilder;

public class StatisticsMessageAdapter {

	public static StatisticsMessage from(Collection<StatisticsBucket> buckets) {
		StatisticsMessageBuilder messageBuilder = new StatisticsMessageBuilder();
		
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