package uk.co.bssd.statistics.server.application.service;

import uk.co.bssd.statistics.server.api.dto.StartTimingPointMessage;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointMessage;

public interface StatisticsServerFacade {

	void startTimingPoint(StartTimingPointMessage request);
	
	void stopTimingPoint(StopTimingPointMessage request);
}