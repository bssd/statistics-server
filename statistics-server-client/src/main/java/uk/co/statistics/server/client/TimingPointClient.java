package uk.co.statistics.server.client;

import java.util.UUID;

public interface TimingPointClient {

	void startTimingPoint(UUID id, String name); 
	
	void stopTimingPoint(UUID id, String name);
}