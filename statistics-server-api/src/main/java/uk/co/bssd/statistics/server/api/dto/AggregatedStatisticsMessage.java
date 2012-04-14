package uk.co.bssd.statistics.server.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AggregatedStatisticsMessage implements Serializable, Iterable<AggregatedStatistics> {

	private static final long serialVersionUID = 1L;

	private final List<AggregatedStatistics> aggregatedStatistics;
	
	public AggregatedStatisticsMessage(List<AggregatedStatistics> statistics) {
		this.aggregatedStatistics = Collections.unmodifiableList(statistics);
	}

	@Override
	public Iterator<AggregatedStatistics> iterator() {
		return this.aggregatedStatistics.iterator();
	}
	
	public int aggregatedStatisticsCount() {
		return this.aggregatedStatistics.size();
	}
}