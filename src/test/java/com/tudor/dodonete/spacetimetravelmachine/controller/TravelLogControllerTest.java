package com.tudor.dodonete.spacetimetravelmachine.controller;

import com.tudor.dodonete.spacetimetravelmachine.dto.TravelLogDTO;
import com.tudor.dodonete.spacetimetravelmachine.entity.TravelLog;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class TravelLogControllerTest extends AbstractControllerTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void retrieveAllTravelLogs() throws Exception {
        String uri = "/api/travels";
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders
                        .get(uri)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        TravelLog[] travelLogList = super.mapFromJSON(content, TravelLog[].class);
        assertTrue(travelLogList.length > 0);
    }

    @Test
    public void retrieveAllLogsByPerson() throws Exception {
        String uri = "/api/people/C1sad32c4q/travels";
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders
                        .get(uri)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        TravelLog[] travelLogList = super.mapFromJSON(content, TravelLog[].class);
        assertTrue(travelLogList.length > 0);
    }

    @Test
    public void createTravelLog() throws Exception {
        String uri = "/api/travels";

        TravelLogDTO travelLogDTO = new TravelLogDTO(
                "Venus",
                new Date(),
                "nvia90a9u");

        String inputJson = super.mapToJSON(travelLogDTO);

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
    public void retrieveTravelLogFromPersonWithId() throws Exception {
        String PGI = "V564d32das";
        long ID = 3L;
        String uri = "/api/people/" + PGI + "/travels/" + ID;
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders
                        .get(uri)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(Long.toString(ID)));
    }

    @Test
    public void retrieveTravelLogWithId() throws Exception {
        long ID = 3L;
        String uri = "/api/travels/" + ID;
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders
                        .get(uri)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains(Long.toString(ID)));
    }

    @Test
    public void deleteTravelLog() throws Exception {
        String uri = "/api/travels/14";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(204, status);
    }

    @Test
    public void updateTravelLogInformation() throws Exception {
        long ID = 3L;
        String uri = "/api/travels/" + ID;
        TravelLogDTO travelLogDTO = new TravelLogDTO(
                "WookiePlanet",
                new Date(),
                "V564d32das"
        );
        String inputJson = super.mapToJSON(travelLogDTO);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("WookiePlanet"));
    }
}