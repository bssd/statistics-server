package uk.co.bssd.statistics.server.application.timingpoint;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import uk.co.bssd.statistics.server.api.dto.StartTimingPointRequest;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointRequest;

public class InProgressTimingPointsManagerTest {

	private static final UUID ID = UUID.randomUUID();
	private static final String NAME = "timing_point";

	private static final StartTimingPointRequest START_REQUEST = new StartTimingPointRequest(
			ID, NAME, 23);
	private static final StopTimingPointRequest STOP_REQUEST = new StopTimingPointRequest(
			ID, NAME, 47);

	private static final StopTimingPointRequest STOP_REQUEST_WITH_DIFFERENT_NAME = new StopTimingPointRequest(
			ID, "different", 57);
	private static final StopTimingPointRequest STOP_REQUEST_WITH_DIFFERENT_ID = new StopTimingPointRequest(
			UUID.randomUUID(), NAME, 56);

	private InProgressTimingPointsManager manager;

	@Before
	public void before() {
		this.manager = new InProgressTimingPointsManager();
		this.manager.startTimingPoint(START_REQUEST);
	}

	@Test
	public void testContainsStartRequestForStopWithSameId() {
		assertThat(this.manager.containsStartedTimingPoint(STOP_REQUEST),
				is(true));
	}

	@Test
	public void testDoesNotContainStartRequestForStopWithDifferentId() {
		assertThat(
				this.manager
						.containsStartedTimingPoint(STOP_REQUEST_WITH_DIFFERENT_ID),
				is(false));
	}
	
	@Test
	public void testDoesNotContainStartRequestForStopWithDifferentName() {
		assertThat(
				this.manager
				.containsStartedTimingPoint(STOP_REQUEST_WITH_DIFFERENT_NAME),
				is(false));
	}

	@Test
	public void testStopTimingPointForAnIdThatHasBeenStartedReturnsTimingPoint() {
		assertThat(this.manager.stopTimingPointRequest(STOP_REQUEST),
				is(notNullValue()));
	}

	@Test(expected = IllegalStateException.class)
	public void testStopTimingPointForAnIdThatHasNotBeenStartedThrowsAnException() {
		this.manager.stopTimingPointRequest(STOP_REQUEST_WITH_DIFFERENT_ID);
	}
}