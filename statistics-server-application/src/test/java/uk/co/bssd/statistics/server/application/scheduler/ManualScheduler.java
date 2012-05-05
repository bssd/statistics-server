package uk.co.bssd.statistics.server.application.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ManualScheduler implements Scheduler {

	private final List<Runnable> tasks;

	public ManualScheduler() {
		this.tasks = new ArrayList<Runnable>();
	}

	@Override
	public void scheduleAtFixedRate(Runnable task, long interval, TimeUnit unit) {
		this.tasks.add(task);
	}
	
	public void executeAll() {
		for (Runnable task : this.tasks) {
			task.run();
		}
	}
}