package uk.co.bssd.statistics.server.api.dto;

import java.io.Serializable;
import java.util.UUID;

public class StartTimingPointMessage extends BaseTimingPointMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	public StartTimingPointMessage(UUID id, String name, long time) {
		super(id, name, time);
	}
}