package uk.co.bssd.statistics.server.application.statistic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.co.bssd.statistics.server.api.dto.StatisticsMessage;
import uk.co.bssd.statistics.server.application.timingpoint.TimingPoint;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsPublisherTest {
	
	private static final TimingPoint TIMING_POINT = new TimingPoint(UUID.randomUUID(), "Timing Point", 1, 23);
	
	private StatisticsCollector statisticsCollector;

	@Mock
	private StatisticsPublishingService mockPublishingService;
	
	private StatisticsPublisher publisher;
	
	@Captor
	private ArgumentCaptor<StatisticsMessage> messageCaptor;

	@Before
	public void before() {
		this.statisticsCollector = new StatisticsCollector();
		this.publisher = new StatisticsPublisher(this.statisticsCollector, this.mockPublishingService);
	}
	
	@Test
	public void testPublishOnACollectorWithNoTimingPointsResultsInAMessageWithNoAggregatedStatistics() {
		this.publisher.publish();
		StatisticsMessage message = assertSingleStatisticsMessagePublished();
		assertThat(message.aggregatedStatisticsCount(), is(0));
	}
	
	@Test
	public void testPublishOnACollectorWithATimingPointResultsInAMessageWithAggregatedStatistics() {
		this.statisticsCollector.addTimingPoint(TIMING_POINT);
		this.publisher.publish();
		
		StatisticsMessage message = assertSingleStatisticsMessagePublished();
		assertThat(message.aggregatedStatisticsCount(), is(1));
	}
	
	@Test
	public void testPublishingClearsTheStatisticsCollectorAndTheStatisticsForATimingPointAreSentOnlyOnce() {
		this.statisticsCollector.addTimingPoint(TIMING_POINT);
		this.publisher.publish();
		this.publisher.publish();
		
		List<StatisticsMessage> messages = assertMultipleStatisticsMessagesPublished(2);
		assertThat(messages.get(0).aggregatedStatisticsCount(), is(1));
		assertThat(messages.get(1).aggregatedStatisticsCount(), is(0));
	}
	
	private List<StatisticsMessage> assertMultipleStatisticsMessagesPublished(int expectedNumber) {
		verifyNumberStatisticMessagesPublished(expectedNumber);
		return this.messageCaptor.getAllValues();
	}
	
	private StatisticsMessage assertSingleStatisticsMessagePublished() {
		verifyNumberStatisticMessagesPublished(1);
		return this.messageCaptor.getValue();
	}
	
	private void verifyNumberStatisticMessagesPublished(int expectedNumber) {
		verify(this.mockPublishingService, times(expectedNumber)).publish(this.messageCaptor.capture());
	}
}