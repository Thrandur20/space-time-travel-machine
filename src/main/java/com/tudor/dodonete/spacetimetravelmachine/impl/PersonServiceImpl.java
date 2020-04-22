package com.tudor.dodonete.spacetimetravelmachine.impl;

import com.tudor.dodonete.spacetimetravelmachine.customException.ResourceNotFoundException;
import com.tudor.dodonete.spacetimetravelmachine.entity.Person;
import com.tudor.dodonete.spacetimetravelmachine.entity.TravelLog;
import com.tudor.dodonete.spacetimetravelmachine.repository.PersonRepository;
import com.tudor.dodonete.spacetimetravelmachine.repository.TravelLogRepository;
import com.tudor.dodonete.spacetimetravelmachine.service.PersonService;
import com.tudor.dodonete.spacetimetravelmachine.utils.PgiGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    PersonRepository personRepository;
    TravelLogRepository travelLogRepository;
    PgiGenerator pgiGenerator;
    Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Autowired
    public void setPgiGenerator(PgiGenerator pgiGenerator) {
        this.pgiGenerator = pgiGenerator;
    }

    @Autowired
    public void setTravelLogRepository(TravelLogRepository travelLogRepository) {
        this.travelLogRepository = travelLogRepository;
    }

    @Override
    public Person createPerson(Person person) {
        String newPgi = pgiGenerator.createNewRandomUniquePGI();
        if (validateUniquePGI(newPgi)) {
            person.setPgi(newPgi);
            personRepository.save(person);
            return person;
        }
        return null;
    }

    @Override
    public List<Person> getAllPeople() {
        return personRepository.findAll();
    }

    @Override
    public void deletePerson(String pgi) {
        Optional<Person> personToBeDeleted = personRepository.findOneByPgi(pgi);
        if (personToBeDeleted.isEmpty()) {
            logger.warn("There was no person found with the given {} PGI", pgi);
            throw new ResourceNotFoundException("The PGI for the requested user does not exist");
        }
        List<TravelLog> travelLogsToBeRemoved = travelLogRepository.findAllByPerson(personToBeDeleted.get());
        travelLogRepository.deleteAll(travelLogsToBeRemoved);
        personRepository.delete(personToBeDeleted.get());
        logger.info("Successfully deleted the person with {} PGI", pgi);
    }

    @Override
    public Person getPersonDetails(String pgi) {
        Optional<Person> foundPerson = personRepository.findOneByPgi(pgi);
        if (foundPerson.isEmpty()) {
            logger.warn("There was no person found with the given {} PGI", pgi);
            throw new ResourceNotFoundException("No person was found with the following PGI: " + pgi);
        }
        logger.info("Returning person with {} PGI", pgi);
        return foundPerson.get();
    }

    @Override
    public Person updatePersonDetails(String pgi, Person person) {
        Optional<Person> foundPerson = personRepository.findOneByPgi(pgi);
        if (foundPerson.isEmpty()) {
            logger.warn("There was no person found with the given {} PGI", pgi);
            throw new ResourceNotFoundException("No person was found with the following PGI: " + pgi);
        }
        person.setPgi(pgi);
        person.setId(foundPerson.get().getId());
        personRepository.save(person);
        logger.info("Successfully updated the user with the {} PGI", pgi);
        return person;
    }

    private boolean validateUniquePGI(String pgi) {
        if (personRepository.existsByPgi(pgi)) {
            logger.info("You might just be the luckiest man in the entire Galactic System!");
            pgi = pgiGenerator.createNewRandomUniquePGI();
            logger.info("Created a new PGI {} to be validated...", pgi);
            validateUniquePGI(pgi);
        }
        logger.info("Successfully passed validation, this is a unique PGI!");
        return true;
    }
}
