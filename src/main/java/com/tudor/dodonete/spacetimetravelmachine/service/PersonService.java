package com.tudor.dodonete.spacetimetravelmachine.service;

import com.tudor.dodonete.spacetimetravelmachine.entity.Person;

import java.util.List;

public interface PersonService {
    Person createPerson(Person person);
    List<Person> getAllPeople();
    void deletePerson(String pgi);
    Person getPersonDetails(String pgi);
    Person updatePersonDetails(String pgi, Person person);
}
