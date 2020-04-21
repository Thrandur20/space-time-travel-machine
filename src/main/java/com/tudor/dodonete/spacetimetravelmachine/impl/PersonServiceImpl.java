package com.tudor.dodonete.spacetimetravelmachine.impl;

import com.tudor.dodonete.spacetimetravelmachine.customException.ResourceNotFoundException;
import com.tudor.dodonete.spacetimetravelmachine.entity.Person;
import com.tudor.dodonete.spacetimetravelmachine.repository.PersonRepository;
import com.tudor.dodonete.spacetimetravelmachine.service.PersonService;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PersonServiceImpl implements PersonService {

    PersonRepository personRepository;
    Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
    private static final Random randomSize = new Random();

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person createPerson(Person person) {
        String newPgi = createNewRandomUniquePGI();
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
            throw new ResourceNotFoundException("The PGI for the requested user does not exist");
        }
        personRepository.delete(personToBeDeleted.get());
    }

    @Override
    public Person getPersonDetails(String pgi) {
        Optional<Person> foundPerson = personRepository.findOneByPgi(pgi);
        if (foundPerson.isEmpty()) {
            throw new ResourceNotFoundException("No person was found with the following PGI: " + pgi);
        }
        return foundPerson.get();
    }

    @Override
    public Person updatePersonDetails(String pgi, Person person) {
        Optional<Person> foundPerson = personRepository.findOneByPgi(pgi);
        if (foundPerson.isEmpty()) {
            throw new ResourceNotFoundException("No person was found with the following PGI: " + pgi);
        }
        person.setPgi(pgi);
        person.setId(foundPerson.get().getId());
        personRepository.save(person);
        return person;
    }

    private String createNewRandomUniquePGI() {
        int randomLetter = (int) (Math.random() * 52);
        char base = (randomLetter < 26) ? 'A' : 'a';
        char randChar = (char) (base + randomLetter % 26);
        int min = 4;
        int max = 10;
        int result = randomSize.nextInt(max - min) + min;
        String value = RandomStringUtils.randomAlphanumeric(result);
        return randChar + value;
    }

    private boolean validateUniquePGI(String pgi) {
        if (personRepository.existsByPgi(pgi)) {
            logger.info("You might just be the luckiest man in the entire Galactic System!");
            pgi = createNewRandomUniquePGI();
            validateUniquePGI(pgi);
        }
        return true;
    }
}
