package uk.co.bssd.statistics.server.application.statistic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.co.bssd.statistics.server.application.scheduler.Scheduler;
import uk.co.bssd.statistics.server.application.timingpoint.SetTimingPointsCollector;
import uk.co.bssd.statistics.server.application.timingpoint.TimingPointsCollector;

@RunWith(MockitoJUnitRunner.class)
public class TimeIntervalCollectorsTest {

	private static final TimeUnit TIME_UNIT_1 = TimeUnit.HOURS;
	private static final TimeUnit TIME_UNIT_2 = TimeUnit.DAYS;
	
	@Mock
	private Scheduler mockScheduler;
	
	@Mock
	private StatisticsPublisherJobFactory mockJobFactory;

	private TimeIntervalCollectors collectors;

	@Before
	public void before() {
		this.collectors = new TimeIntervalCollectors(this.mockScheduler, this.mockJobFactory);
		when(this.mockJobFactory.createJob(any(StatisticsCollector.class))).thenReturn(mock(StatisticsPublisherJob.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNoTimeIntervalsThrowsException() { 
		this.collectors.forIntervals();
	}
	
	@Test
	public void testCollectorReturnedIsASetTimingPointsCollector() {
		TimingPointsCollector collector = this.collectors.forIntervals(TIME_UNIT_1);
		assertThat(collector, is(SetTimingPointsCollector.class));
	}
	
	@Test
	public void testCollectorReturnedHoldsTheCorrectNumberOfCollectors() {
		SetTimingPointsCollector collector = (SetTimingPointsCollector)this.collectors.forIntervals(TIME_UNIT_1, TIME_UNIT_2);
		assertThat(collector.countCollectors(), is(2));
	}
	
	@Test
	public void testJobIsCreatedForInterval() {
		this.collectors.forIntervals(TIME_UNIT_1);
		verify(this.mockJobFactory).createJob(any(StatisticsCollector.class));
	}
	
	@Test
	public void testJobIsScheduledWithInterval() {
		this.collectors.forIntervals(TIME_UNIT_1);
		verify(this.mockScheduler).scheduleAtFixedRate(notNull(StatisticsPublisherJob.class), eq(1L), eq(TIME_UNIT_1));
	}
}