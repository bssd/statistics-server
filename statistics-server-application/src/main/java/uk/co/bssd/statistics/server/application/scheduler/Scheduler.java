package uk.co.bssd.statistics.server.application.scheduler;

import java.util.concurrent.TimeUnit;

public interface Scheduler {

	void scheduleAtFixedRate(Runnable task, long interval, TimeUnit unit);
}