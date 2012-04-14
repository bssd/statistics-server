package uk.co.bssd.statistics.server.application.timingpoint;

import java.util.UUID;

import uk.co.bssd.statistics.server.api.dto.StartTimingPointRequest;

public class TimingPointBuilder {

	private UUID id;
	private String name;
	private long millisecondStartTime;
	private long millisecondEndTime;

	public TimingPointBuilder() {
		super();
	}

	public TimingPointBuilder withId(UUID id) {
		this.id = id;
		return this;
	}

	public TimingPointBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public TimingPointBuilder withMillisecondStartTime(long time) {
		this.millisecondStartTime = time;
		return this;
	}

	public TimingPointBuilder withMillisecondEndTime(long time) {
		this.millisecondEndTime = time;
		return this;
	}
	
	public TimingPoint build() {
		return new TimingPoint(this.id, this.name, this.millisecondStartTime, this.millisecondEndTime);
	}
	
	public static TimingPointBuilder from(StartTimingPointRequest request) {
		TimingPointBuilder builder = new TimingPointBuilder();
		builder.withId(request.id());
		builder.withName(request.name());
		builder.withMillisecondStartTime(request.millisecondTime());
		return builder;
	}
}