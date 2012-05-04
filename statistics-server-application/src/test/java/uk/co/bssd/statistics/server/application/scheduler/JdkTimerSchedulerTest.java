package uk.co.bssd.statistics.server.application.scheduler;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JdkTimerSchedulerTest {

	private static final long ONE_MINUTE_IN_MILLISECONDS = 1 * 1000 * 60; 
	
	@Mock
	private Runnable mockTask;
	
	@Captor 
	private ArgumentCaptor<TimerTask> timerTaskCaptor;
	
	@Mock
	private Timer mockTimer;
	
	private Scheduler scheduler;
	
	@Before
	public void before() {
		this.scheduler = new JdkTimerScheduler(this.mockTimer);
	}
	
	@Test
	public void testScheduleAtFixedRateSchedulesTheTaskWithTheTimerCorrectly() {
		this.scheduler.scheduleAtFixedRate(this.mockTask, 1, TimeUnit.SECONDS);
		verify(this.mockTimer).scheduleAtFixedRate(notNull(TimerTask.class), anyLong(), anyLong());
	}
	
	@Test
	public void testScheduleAtFixedRateSchedulesTimerTaskWrappingOriginalTask() {
		this.scheduler.scheduleAtFixedRate(this.mockTask, 1, TimeUnit.SECONDS);
		verify(this.mockTimer).scheduleAtFixedRate(this.timerTaskCaptor.capture(), anyLong(), anyLong());
		
		verify(this.mockTask, never()).run();
		
		TimerTask timerTask = this.timerTaskCaptor.getValue();
		timerTask.run();
		
		verify(this.mockTask, times(1)).run();
	}
	
	@Test
	public void testScheduleAtFixedRateConvertsTheIntervalIntoMillisecondsCorrectly() {
		this.scheduler.scheduleAtFixedRate(this.mockTask, 1, TimeUnit.MINUTES);
		verify(this.mockTimer).scheduleAtFixedRate(notNull(TimerTask.class), eq(ONE_MINUTE_IN_MILLISECONDS), eq(ONE_MINUTE_IN_MILLISECONDS));
	}
}