package com.tudor.dodonete.spacetimetravelmachine.controller;

import com.tudor.dodonete.spacetimetravelmachine.entity.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class PersonControllerTest extends AbstractControllerTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void retrieveAllPeople() throws Exception {
        String uri = "/api/people";
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders
                        .get(uri)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        Person[] personList = super.mapFromJSON(content, Person[].class);
        assertTrue(personList.length > 0);
    }

    @Test
    public void createPerson() throws Exception {
        String uri = "/api/people";

        Person person = new Person("Mark", "Skywalker");
        person.setPgi("mockPgi");
        String inputJson = super.mapToJSON(person);

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders
                        .post(uri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        assertNotNull(mvcResult.getResponse().getHeader("Location"));
    }

    @Test
    public void retrievePersonByPGI() throws Exception {
        String PGI = "V564d32das";
        String uri = "/api/people/" + PGI;
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders
                        .get(uri)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(PGI));
    }

    @Test
    public void deletePerson() throws Exception {
        String uri = "/api/people/a1234saf";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(204, status);
    }

    @Test
    public void updatePerson() throws Exception {
        String PGI = "AdajjBN56a";
        String uri = "/api/people/" + PGI;
        Person person = new Person("Jean", "Picard");
        person.setPgi(PGI);
        String inputJson = super.mapToJSON(person);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Person updatedPerson = super.mapFromJSON(content, Person.class);
        assertEquals("Jean", updatedPerson.getFirstName());
    }
}