package com.tudor.dodonete.spacetimetravelmachine.impl;

import com.tudor.dodonete.spacetimetravelmachine.customException.ResourceNotFoundException;
import com.tudor.dodonete.spacetimetravelmachine.entity.Person;
import com.tudor.dodonete.spacetimetravelmachine.repository.PersonRepository;
import com.tudor.dodonete.spacetimetravelmachine.utils.PgiGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PersonServiceImplTest {

    private PersonServiceImpl personService;

    public static final String PGI = "abc123def";
    public static final String FIRST_NAME = "Test";
    public static final String LAST_NAME = "Name";

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PgiGenerator pgiGenerator;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createPersonWithValidationPassing() {
        //Given
        Person person = new Person(FIRST_NAME, LAST_NAME);
        when(pgiGenerator.createNewRandomUniquePGI()).thenReturn(PGI);
        when(personRepository.existsByPgi(anyString())).thenReturn(false);
        personService = new PersonServiceImpl(personRepository);
        personService.setPgiGenerator(pgiGenerator);
        //When
        personService.createPerson(person);
        //Then
        Person expectedPerson = new Person();
        expectedPerson.setFirstName(FIRST_NAME);
        expectedPerson.setLastName(LAST_NAME);
        expectedPerson.setPgi(PGI);
        verify(personRepository).save(expectedPerson);
    }

    @Test
    public void getListOfAllPeopleAndReturnList() {
        //Given
        Person person = createDefaultPerson();
        List<Person> personList = Collections.singletonList(person);
        when(personRepository.findAll()).thenReturn(personList);
        personService = new PersonServiceImpl(personRepository);
        //When
        List<Person> generatedPeopleList = personService.getAllPeople();
        //Then
        Person expectedPerson = new Person();
        expectedPerson.setId(1L);
        expectedPerson.setPgi("abc123def");
        expectedPerson.setFirstName("Test");
        expectedPerson.setLastName("Name");
        List<Person> expectedPeopleList = Collections.singletonList(expectedPerson);
        assertEquals(expectedPeopleList, generatedPeopleList);
    }

    @Test
    public void getListOfAllPeopleAndReturnEmptyList() {
        //Given
        List<Person> peopleList = new ArrayList<>();
        when(personRepository.findAll()).thenReturn(peopleList);
        personService = new PersonServiceImpl(personRepository);
        //When
        List<Person> generatedPeopleList = personService.getAllPeople();
        //Then
        assertEquals(0, generatedPeopleList.size());
    }

    @Test
    public void deletePersonByPgi() {
        //Given
        Person person = createDefaultPerson();
        when(personRepository.findOneByPgi(anyString())).thenReturn(Optional.of(person));
        personService = new PersonServiceImpl(personRepository);
        //When
        personService.deletePerson(PGI);
        //Then
        verify(personRepository).delete(person);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletePersonButReceiveErrorAsPgiCannotBeFound() {
        //Given
        String PGI = "b23c95CA";
        when(personRepository.findOneByPgi(PGI)).thenReturn(Optional.empty());
        personService = new PersonServiceImpl(personRepository);
        //When
        personService.deletePerson(PGI);
        //Then throw Resource not found exception
    }

    @Test
    public void getPersonDetailsByPgi() {
        //Given
        Person person = createDefaultPerson();
        when(personRepository.findOneByPgi(anyString())).thenReturn(Optional.of(person));

        personService = new PersonServiceImpl(personRepository);
        //When
        Person foundPerson = personService.getPersonDetails(PGI);
        //Then
        Person expectedPerson = new Person(1L, PGI, "Test", "Name");
        assertEquals(expectedPerson, foundPerson);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getPersonDetailsByPGIButThrowException() {
        //Given
        when(personRepository.findOneByPgi(anyString())).thenReturn(Optional.empty());
        personService = new PersonServiceImpl(personRepository);
        //When
        personService.getPersonDetails(PGI);
        //Then expected Resource not Found exception to be thrown
    }

    @Test
    public void updatePersonDetails() {
        //Given
        String UPD_FIRST_NAME = "Other First Name";
        String UPD_LAST_NAME = "Other Last Name";
        Person person = createDefaultPerson();
        when(personRepository.findOneByPgi(anyString())).thenReturn(Optional.of(person));
        person.setFirstName(UPD_FIRST_NAME);
        person.setLastName(UPD_LAST_NAME);
        personService = new PersonServiceImpl(personRepository);
        //When
        Person foundPerson = personService.updatePersonDetails(PGI, person);
        //Then
        Person expectedPerson = new Person(1L, PGI, UPD_FIRST_NAME, UPD_LAST_NAME);
        assertEquals(expectedPerson, foundPerson);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updatePersonDetailsButThrowException() {
        //Given
        when(personRepository.findOneByPgi(anyString())).thenReturn(Optional.empty());
        personService = new PersonServiceImpl(personRepository);
        //When
        personService.updatePersonDetails(PGI, createDefaultPerson());
        //Then expected Resource not Found exception to be thrown
    }

    private Person createDefaultPerson() {
        Person person = new Person(FIRST_NAME, LAST_NAME);
        person.setPgi(PGI);
        person.setId(1L);
        return person;
    }
}