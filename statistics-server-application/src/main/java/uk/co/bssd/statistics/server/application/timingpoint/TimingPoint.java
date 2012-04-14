package uk.co.bssd.statistics.server.application.timingpoint;

import java.util.UUID;

public class TimingPoint {

	private final UUID id;
	private final String name;
	private final long millisecondStartTime;
	private final long millisecondEndTime;
	
	public TimingPoint(UUID id, String name, long startTime, long endTime) {
		this.id = id;
		this.name = name;
		this.millisecondStartTime = startTime;
		this.millisecondEndTime = endTime;
	}
	
	public UUID id() {
		return this.id;
	}
	
	public String name() {
		return this.name;
	}
	
	public long millisecondStartTime() {
		return this.millisecondStartTime;
	}
	
	public long millisecondEndTime() {
		return this.millisecondEndTime;
	}
	
	public long duration() {
		return millisecondEndTime() - millisecondStartTime();
	}
}