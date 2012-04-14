package uk.co.bssd.statistics.server.api.dto;

import java.io.Serializable;
import java.util.UUID;

public class StartTimingPointRequest extends BaseTimingPointRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	public StartTimingPointRequest(UUID id, String name, long time) {
		super(id, name, time);
	}
}