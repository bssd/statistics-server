package uk.co.bssd.statistics.server.application.timingpoint;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.co.bssd.statistics.server.api.dto.StartTimingPointRequest;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointRequest;
import uk.co.bssd.statistics.server.application.service.InMemoryStatisticsServerFacade;
import uk.co.bssd.statistics.server.application.service.StatisticsServerFacade;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryStatisticsServerFacadeTest {

	private static final UUID ID = UUID.randomUUID();
	private static final String NAME = "timing_point";
	private static final long START_TIME = 34;
	private static final long END_TIME = 67;
	
	private static final StartTimingPointRequest START_REQUEST = new StartTimingPointRequest(ID, NAME, START_TIME);
	private static final StopTimingPointRequest STOP_REQUEST = new StopTimingPointRequest(ID, NAME, END_TIME);
	private static final StopTimingPointRequest STOP_REQUEST_WITH_DIFFERENT_ID = new StopTimingPointRequest(UUID.randomUUID(), NAME, END_TIME);
	
	private StatisticsServerFacade facade;

	@Mock
	private TimingPointsCollector mockTimingPointsCollector;

	@Before
	public void before() {
		this.facade = new InMemoryStatisticsServerFacade(
				this.mockTimingPointsCollector);
	}
	
	@Test
	public void testStartingTimingPointDoesNotResultInTimingPointBeingCollected() {
		this.facade.startTimingPoint(START_REQUEST);
		assertNoTimingPointCollected();
	}
	
	@Test
	public void testStoppingTimingPointThatWasNotStartedDoesNotResultInTimingPointBeingCollected() {
		this.facade.stopTimingPoint(STOP_REQUEST);
		assertNoTimingPointCollected();
	}
	
	@Test
	public void testStoppingTimingPointThatWasStartedResultsInTimingPointBeingCollected() {
		this.facade.startTimingPoint(START_REQUEST);
		this.facade.stopTimingPoint(STOP_REQUEST);
		assertTimingPointCollected();
	}
	
	@Test
	public void testStoppingTimingPointWithSameNameButDifferentIdToThatStartedDoesNotResultInTimingPointBeingCollected() {
		this.facade.startTimingPoint(START_REQUEST);
		this.facade.stopTimingPoint(STOP_REQUEST_WITH_DIFFERENT_ID);
		assertNoTimingPointCollected();
	}
	
	private void assertTimingPointCollected() {
		verify(this.mockTimingPointsCollector, times(1)).addTimingPoint(any(TimingPoint.class));
	}

	private void assertNoTimingPointCollected() {
		verify(this.mockTimingPointsCollector, never()).addTimingPoint(any(TimingPoint.class));
	}
}