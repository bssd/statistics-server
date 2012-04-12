package org.statistics.server.api.service;

import java.util.UUID;

public interface TimingPointService {

	void startTimingPoint(UUID id, String name); 
}