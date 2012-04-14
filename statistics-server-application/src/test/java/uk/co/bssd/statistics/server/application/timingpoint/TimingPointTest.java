package uk.co.bssd.statistics.server.application.timingpoint;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

public class TimingPointTest {

	private static final UUID ID = UUID.randomUUID();
	private static final String NAME = "timing_point";
	private static final long START_TIME = 150;
	private static final long END_TIME = 227;
	private static final long DURATION = 77;
	
	private static TimingPoint timingPoint;
	
	@BeforeClass
	public static void beforeClass() {
		timingPoint = new TimingPoint(ID, NAME, START_TIME, END_TIME);
	}
	
	@Test
	public void testId() {
		assertThat(timingPoint.id(), is(ID));
	}
	
	@Test
	public void testName() {
		assertThat(timingPoint.name(), is(NAME));
	}
	
	@Test
	public void testMillisecondStartTime() {
		assertThat(timingPoint.millisecondStartTime(), is(START_TIME));
	}
	
	@Test
	public void testMillisecondEndTime() {
		assertThat(timingPoint.millisecondEndTime(), is(END_TIME));
	}
	
	@Test
	public void testDuration() {
		assertThat(timingPoint.duration(), is(DURATION));
	}
}