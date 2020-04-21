package com.tudor.dodonete.spacetimetravelmachine.impl;

import com.tudor.dodonete.spacetimetravelmachine.customException.ExceptionResponse;
import com.tudor.dodonete.spacetimetravelmachine.customException.ResourceNotFoundException;
import com.tudor.dodonete.spacetimetravelmachine.entity.Person;
import com.tudor.dodonete.spacetimetravelmachine.entity.TravelLog;
import com.tudor.dodonete.spacetimetravelmachine.repository.PersonRepository;
import com.tudor.dodonete.spacetimetravelmachine.repository.TravelLogRepository;
import com.tudor.dodonete.spacetimetravelmachine.service.TravelLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.Date;
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
    public TravelLog createTravelLogForPerson(String pgi, TravelLog travelLog) {
        Optional<Person> foundPerson = personRepository.findOneByPgi(pgi);
        if (foundPerson.isEmpty()) {
            throw new ResourceNotFoundException("Was not able to find a person with the given pgi: " + pgi);
        }
        try {
            travelLogRepository.save(travelLog);
        } catch (ConstraintViolationException e) {
            throw new RuntimeException(
                    "You are breaking the laws of physics by travelling multiple times at the same destination");
        }
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
}
