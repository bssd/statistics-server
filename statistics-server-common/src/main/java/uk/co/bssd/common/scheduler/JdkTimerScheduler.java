package uk.co.bssd.common.scheduler;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class JdkTimerScheduler implements Scheduler {

	private final Timer timer;

	private static final class RunnableTimerTask extends TimerTask {
		private final Runnable runnable;
		
		public RunnableTimerTask(Runnable r) {
			this.runnable = r;
		}
		
		@Override
		public void run() {
			this.runnable.run();
		}
	}
	
	public JdkTimerScheduler() {
		this(new Timer(true));
	}
	
	public JdkTimerScheduler(Timer timer) {
		this.timer = timer;
	}
	
	public void scheduleAtFixedRate(Runnable task, long interval, TimeUnit unit) {
		long millisecondInterval = millisecondInterval(interval, unit);
		TimerTask timerTask = new RunnableTimerTask(task);
		this.timer.scheduleAtFixedRate(timerTask, millisecondInterval, millisecondInterval);
	}
	
	private long millisecondInterval(long interval, TimeUnit sourceUnit) {
		return TimeUnit.MILLISECONDS.convert(interval, sourceUnit);
	}
}