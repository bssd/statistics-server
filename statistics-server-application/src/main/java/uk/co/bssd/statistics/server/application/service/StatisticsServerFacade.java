package uk.co.bssd.statistics.server.application.service;

import uk.co.bssd.statistics.server.api.dto.StartTimingPointRequest;
import uk.co.bssd.statistics.server.api.dto.StopTimingPointRequest;

public interface StatisticsServerFacade {

	void startTimingPoint(StartTimingPointRequest request);
	
	void stopTimingPoint(StopTimingPointRequest request);
}