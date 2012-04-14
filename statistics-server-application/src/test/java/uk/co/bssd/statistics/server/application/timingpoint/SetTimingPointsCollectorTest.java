package uk.co.bssd.statistics.server.application.timingpoint;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import uk.co.bssd.statistics.server.application.timingpoint.SetTimingPointsCollector;
import uk.co.bssd.statistics.server.application.timingpoint.TimingPoint;
import uk.co.bssd.statistics.server.application.timingpoint.TimingPointsCollector;

public class SetTimingPointsCollectorTest {

	private static final TimingPoint TIMING_POINT = new TimingPoint(UUID.randomUUID(), "timing_point", 1, 28);
	
	private SetTimingPointsCollector setCollectors;
	
	@Before
	public void before() {
		this.setCollectors = new SetTimingPointsCollector();
	}
	
	@Test
	public void testAddTimingPointWithNoCollectorsReturnsOk() {
		this.setCollectors.addTimingPoint(TIMING_POINT);
	}
	
	@Test
	public void testAddTimingPointNotifiesSingleRegisteredCollector() {
		TimingPointsCollector collector = mock(TimingPointsCollector.class);
		this.setCollectors.registerCollector(collector);
		this.setCollectors.addTimingPoint(TIMING_POINT);
		verify(collector, times(1)).addTimingPoint(TIMING_POINT);
	}
	
	@Test
	public void testAddTimingPointNotifiesTwoRegisteredCollectors() {
		TimingPointsCollector collector1 = mock(TimingPointsCollector.class);
		TimingPointsCollector collector2 = mock(TimingPointsCollector.class);
		this.setCollectors.registerCollector(collector1);
		this.setCollectors.registerCollector(collector2);
		
		this.setCollectors.addTimingPoint(TIMING_POINT);
		
		verify(collector1, times(1)).addTimingPoint(TIMING_POINT);
		verify(collector2, times(1)).addTimingPoint(TIMING_POINT);
	}
	
	@Test
	public void testExceptionThrownByOneCollectorDoesNotAffectNotificationOfTheOther() {
		TimingPointsCollector collector1 = mock(TimingPointsCollector.class);
		TimingPointsCollector collector2 = mock(TimingPointsCollector.class);
		this.setCollectors.registerCollector(collector1);
		this.setCollectors.registerCollector(collector2);
		
		doThrow(RuntimeException.class).when(collector1).addTimingPoint(TIMING_POINT);
		doThrow(RuntimeException.class).when(collector2).addTimingPoint(TIMING_POINT);
		
		this.setCollectors.addTimingPoint(TIMING_POINT);
		
		verify(collector1, times(1)).addTimingPoint(TIMING_POINT);
		verify(collector2, times(1)).addTimingPoint(TIMING_POINT);
	}	
}