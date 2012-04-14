package uk.co.bssd.statistics.server.api.dto;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseTimingPointRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private final UUID id;
	private final String name;
	private final long millisecondTime;
	
	public BaseTimingPointRequest(UUID id, String name, long time) {
		this.id = id;
		this.name = name;
		this.millisecondTime = time;
	}
	
	public UUID id() {
		return this.id;
	}
	
	public String name() {
		return this.name;
	}
	
	public long millisecondTime() {
		return this.millisecondTime;
	}
}