package com.tudor.dodonete.spacetimetravelmachine.service;

import com.tudor.dodonete.spacetimetravelmachine.dto.TravelLogDTO;
import com.tudor.dodonete.spacetimetravelmachine.entity.TravelLog;

import java.util.List;

public interface TravelLogService {
    List<TravelLog> retrieveTravelLogByPerson(String pgi);

    List<TravelLog> retrieveAllTravelLogs();

    TravelLog createTravelLogForPerson(TravelLogDTO travelLogDTO);

    TravelLog getTravelDetailsById(Long id);

    TravelLog updateTravelLogInfo(TravelLogDTO travelLogDTO, Long id);

    void deleteTravelLog(Long id);
}
