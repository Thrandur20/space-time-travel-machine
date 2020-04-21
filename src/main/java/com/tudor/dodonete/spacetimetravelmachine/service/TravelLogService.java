package com.tudor.dodonete.spacetimetravelmachine.service;

import com.tudor.dodonete.spacetimetravelmachine.entity.TravelLog;

import java.util.List;

public interface TravelLogService {
    List<TravelLog> retrieveTravelLogByPerson(String pgi);

    List<TravelLog> retrieveAllTravelLogs();

    TravelLog createTravelLogForPerson(String pgi, TravelLog travelLog);

    TravelLog getTravelDetailsById(Long id);
}
