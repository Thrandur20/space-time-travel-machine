package com.tudor.dodonete.spacetimetravelmachine.impl;

import com.tudor.dodonete.spacetimetravelmachine.customException.CollisionException;
import com.tudor.dodonete.spacetimetravelmachine.customException.ResourceNotFoundException;
import com.tudor.dodonete.spacetimetravelmachine.dto.TravelLogDTO;
import com.tudor.dodonete.spacetimetravelmachine.entity.Person;
import com.tudor.dodonete.spacetimetravelmachine.entity.TravelLog;
import com.tudor.dodonete.spacetimetravelmachine.repository.PersonRepository;
import com.tudor.dodonete.spacetimetravelmachine.repository.TravelLogRepository;
import com.tudor.dodonete.spacetimetravelmachine.service.TravelLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TravelLogServiceImpl implements TravelLogService {

    PersonRepository personRepository;
    TravelLogRepository travelLogRepository;

    @Autowired
    public TravelLogServiceImpl(PersonRepository personRepository, TravelLogRepository travelLogRepository) {
        this.personRepository = personRepository;
        this.travelLogRepository = travelLogRepository;
    }

    @Override
    public List<TravelLog> retrieveTravelLogByPerson(String pgi) {
        Optional<Person> foundPerson = personRepository.findOneByPgi(pgi);
        if (foundPerson.isEmpty()) {
            throw new ResourceNotFoundException("Was not able to find a person with the given pgi: " + pgi);
        }
        return foundPerson.get().getTravelLogList();
    }

    @Override
    public List<TravelLog> retrieveAllTravelLogs() {
        return travelLogRepository.findAll();
    }

    @Override
    public TravelLog createTravelLogForPerson(TravelLogDTO travelLogDTO) {
        TravelLog travelLog = new TravelLog();
        Optional<Person> foundPerson = personRepository.findOneByPgi(travelLogDTO.getPgi());
        if (foundPerson.isEmpty()) {
            throw new ResourceNotFoundException("Was not able to find a person with the given pgi: " +
                    travelLogDTO.getPgi());
        }

        travelLog.setTravelDate(travelLogDTO.getTravelDate());
        travelLog.setTravelLocation(travelLogDTO.getTravelLocation());
        travelLog.setPerson(foundPerson.get());
        saveTravelLogWithExceptionCheck(travelLog);
        return travelLog;
    }

    @Override
    public TravelLog getTravelDetailsById(Long id) {
        Optional<TravelLog> foundLog = travelLogRepository.findById(id);
        if (foundLog.isEmpty()) {
            throw new ResourceNotFoundException("The log you with the id you are looking for does not exist");
        }
        return foundLog.get();
    }

    @Override
    public TravelLog updateTravelLogInfo(TravelLogDTO travelLogDTO, Long id) {
        TravelLog travelLog = new TravelLog();
        Optional<Person> foundPerson = personRepository.findOneByPgi(travelLogDTO.getPgi());
        Optional<TravelLog> foundTravelLog = travelLogRepository.findById(id);
        if (foundTravelLog.isEmpty() || foundPerson.isEmpty()) {
            throw new ResourceNotFoundException("The log you with the id you are looking for does not exist");
        }
        travelLog.setId(id);
        travelLog.setTravelLocation(travelLogDTO.getTravelLocation());
        travelLog.setTravelDate(travelLogDTO.getTravelDate());
        travelLog.setPerson(foundPerson.get());
        saveTravelLogWithExceptionCheck(travelLog);
        return travelLog;
    }

    @Override
    public void deleteTravelLog(Long id) {
        Optional<TravelLog> travelLogToBeDeleted = travelLogRepository.findById(id);
        if (travelLogToBeDeleted.isEmpty()) {
            throw new ResourceNotFoundException("There is no historic evidence of that travel log");
        }
        travelLogRepository.delete(travelLogToBeDeleted.get());
    }

    private void saveTravelLogWithExceptionCheck(TravelLog travelLog) {
        if (travelLogRepository.existsByTravelLocationAndTravelDateAndPerson(
                travelLog.getTravelLocation(),
                travelLog.getTravelDate(),
                travelLog.getPerson())) {
            throw new CollisionException(
                    "You are breaking the laws of physics by travelling multiple times at the same destination");
        }
        travelLogRepository.save(travelLog);
    }
}
