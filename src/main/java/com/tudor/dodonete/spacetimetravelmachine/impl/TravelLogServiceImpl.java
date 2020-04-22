package com.tudor.dodonete.spacetimetravelmachine.impl;

import com.tudor.dodonete.spacetimetravelmachine.customException.CollisionException;
import com.tudor.dodonete.spacetimetravelmachine.customException.ResourceNotFoundException;
import com.tudor.dodonete.spacetimetravelmachine.dto.TravelLogDTO;
import com.tudor.dodonete.spacetimetravelmachine.entity.Person;
import com.tudor.dodonete.spacetimetravelmachine.entity.TravelLog;
import com.tudor.dodonete.spacetimetravelmachine.repository.PersonRepository;
import com.tudor.dodonete.spacetimetravelmachine.repository.TravelLogRepository;
import com.tudor.dodonete.spacetimetravelmachine.service.TravelLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TravelLogServiceImpl implements TravelLogService {

    PersonRepository personRepository;
    TravelLogRepository travelLogRepository;

    Logger logger = LoggerFactory.getLogger(TravelLogServiceImpl.class);

    @Autowired
    public TravelLogServiceImpl(PersonRepository personRepository, TravelLogRepository travelLogRepository) {
        this.personRepository = personRepository;
        this.travelLogRepository = travelLogRepository;
    }

    @Override
    public List<TravelLog> retrieveTravelLogByPerson(String pgi) {
        Optional<Person> foundPerson = personRepository.findOneByPgi(pgi);
        if (foundPerson.isEmpty()) {
            logger.warn("No Person was found with the given {} PGI", pgi);
            throw new ResourceNotFoundException("Was not able to find a person with the given pgi: " + pgi);
        }
        logger.info("Successfully returned a list of Logs");
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
            logger.warn("There was no person found with the given {} PGI", travelLogDTO.getPgi());
            throw new ResourceNotFoundException("Was not able to find a person with the given pgi: " +
                    travelLogDTO.getPgi());
        }

        travelLog.setTravelDate(travelLogDTO.getTravelDate());
        travelLog.setTravelLocation(travelLogDTO.getTravelLocation());
        travelLog.setPerson(foundPerson.get());
        saveTravelLogWithExceptionCheck(travelLog);
        logger.info("Successfully created a new Travel Log");
        return travelLog;
    }

    @Override
    public TravelLog getTravelDetailsById(Long id) {
        Optional<TravelLog> foundLog = travelLogRepository.findById(id);
        if (foundLog.isEmpty()) {
            logger.warn("There was no travel log found with the given {} id", id);
            throw new ResourceNotFoundException("The log you with the id you are looking for does not exist");
        }
        logger.info("Successfully found the log with the {} id", id);
        return foundLog.get();
    }

    @Override
    public TravelLog updateTravelLogInfo(TravelLogDTO travelLogDTO, Long id) {
        TravelLog travelLog = new TravelLog();
        Optional<Person> foundPerson = personRepository.findOneByPgi(travelLogDTO.getPgi());
        Optional<TravelLog> foundTravelLog = travelLogRepository.findById(id);
        if (foundTravelLog.isEmpty() || foundPerson.isEmpty()) {
            logger.warn("There was no person found with the given {} PGI or {} id", travelLogDTO.getPgi(), id);
            throw new ResourceNotFoundException("The log you with the id you are looking for does not exist");
        }
        travelLog.setId(id);
        travelLog.setTravelLocation(travelLogDTO.getTravelLocation());
        travelLog.setTravelDate(travelLogDTO.getTravelDate());
        travelLog.setPerson(foundPerson.get());
        saveTravelLogWithExceptionCheck(travelLog);
        logger.info("Successfully updated the travel log with the {} id", id);
        return travelLog;
    }

    @Override
    public void deleteTravelLog(Long id) {
        Optional<TravelLog> travelLogToBeDeleted = travelLogRepository.findById(id);
        if (travelLogToBeDeleted.isEmpty()) {
            logger.warn("There was no travel log found with the given {} id", id);
            throw new ResourceNotFoundException("There is no historic evidence of that travel log");
        }
        logger.info("Successfully removed the log with {} id", id);
        travelLogRepository.delete(travelLogToBeDeleted.get());
    }

    private void saveTravelLogWithExceptionCheck(TravelLog travelLog) {
        if (travelLogRepository.existsByTravelLocationAndTravelDateAndPerson(
                travelLog.getTravelLocation(),
                travelLog.getTravelDate(),
                travelLog.getPerson())) {
            logger.warn("The data cannot be saved as there already is an entry with the same values");
            throw new CollisionException(
                    "You are breaking the laws of physics by travelling multiple times at the same destination");
        }
        travelLogRepository.save(travelLog);
    }
}
