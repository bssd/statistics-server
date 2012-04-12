package uk.co.bssd.statistics.server.api.dto;

import java.io.Serializable;
import java.util.UUID;

public class StopTimingPointRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private final UUID id;
	private final String name;
	
	public StopTimingPointRequest(UUID id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public UUID id() {
		return this.id;
	}
	
	public String name() {
		return this.name;
	}
}