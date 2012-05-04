package uk.co.bssd.statistics.server.application.statistic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.co.bssd.statistics.server.api.dto.AggregatedStatisticsMessage;
import uk.co.bssd.statistics.server.application.timingpoint.TimingPoint;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsPublisherJobTest {

	private StatisticsCollector collector;

	@Mock
	private StatisticsPublishingService mockPublisingService;
	
	@Captor
	private ArgumentCaptor<AggregatedStatisticsMessage> messageCaptor;
	
	private StatisticsPublisherJob job;
	
	@Before 
	public void before() {
		this.collector = new StatisticsCollector();
		this.job = new StatisticsPublisherJob(this.collector, this.mockPublisingService);
	}
	
	@Test
	public void testWhenJobIsRunMessageIsSentToPublishingService() {
		this.job.run();
		verify(this.mockPublisingService).publish(any(AggregatedStatisticsMessage.class));
	}
	
	@Test
	public void testWhenJobIsRunItTakesStatisticsInSelector() {
		this.collector.addTimingPoint(new TimingPoint(UUID.randomUUID(), "name", 0, 1));
		this.job.run();
		
		verify(this.mockPublisingService).publish(this.messageCaptor.capture());
		
		AggregatedStatisticsMessage message = this.messageCaptor.getValue();
		assertThat(message.aggregatedStatisticsCount(), is(1));
	}
}