package com.tudor.dodonete.spacetimetravelmachine.impl;

import com.tudor.dodonete.spacetimetravelmachine.customException.CollisionException;
import com.tudor.dodonete.spacetimetravelmachine.customException.ResourceNotFoundException;
import com.tudor.dodonete.spacetimetravelmachine.dto.TravelLogDTO;
import com.tudor.dodonete.spacetimetravelmachine.entity.Person;
import com.tudor.dodonete.spacetimetravelmachine.entity.TravelLog;
import com.tudor.dodonete.spacetimetravelmachine.repository.PersonRepository;
import com.tudor.dodonete.spacetimetravelmachine.repository.TravelLogRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TravelLogServiceImplTest {
    private TravelLogServiceImpl travelLogService;
    private static final String DEFAULT_LOCATION = "HOME";
    private static final Long DEFAULT_ID = 1L;
    private static final String DEFAULT_PGI = "abc123def";
    private static final String DEFAULT_F_NAME = "Test";
    private static final String DEFAULT_L_NAME = "Name";
    private static final String DEFAULT_DATE_STR = "2020-04-20";
    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd");

    @Mock
    PersonRepository personRepository;

    @Mock
    TravelLogRepository travelLogRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRetrieveTravelLogByPersonWithPgi() throws ParseException {
        //Given
        Person person = createDefaultPerson();
        when(personRepository.findOneByPgi(anyString())).thenReturn(Optional.of(person));
        travelLogService = new TravelLogServiceImpl(personRepository, travelLogRepository);
        //When
        List<TravelLog> foundTravelLogList = travelLogService.retrieveTravelLogByPerson(DEFAULT_PGI);
        //Then
        TravelLog expectedTravelLog = new TravelLog(DEFAULT_ID, DEFAULT_LOCATION, dateFormat.parse(DEFAULT_DATE_STR));
        List<TravelLog> expectedTravelLogList = Collections.singletonList(expectedTravelLog);
        assertEquals(expectedTravelLogList, foundTravelLogList);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testRetrieveTravelLogByPersonWithPgiButThrowException() {
        //Given
        when(personRepository.findOneByPgi(anyString())).thenReturn(Optional.empty());
        travelLogService = new TravelLogServiceImpl(personRepository, travelLogRepository);
        //When
        travelLogService.retrieveTravelLogByPerson(DEFAULT_PGI);
        //Then throw Resource not Found exception
    }


    @Test
    public void testRetrieveAllTravelLogInformation() throws ParseException {
        //Given
        TravelLog travelLog = createDefaultTravelLog();
        List<TravelLog> travelLogList = Collections.singletonList(travelLog);
        when(travelLogRepository.findAll()).thenReturn(travelLogList);
        travelLogService = new TravelLogServiceImpl(personRepository, travelLogRepository);
        //When
        List<TravelLog> foundTravelLogList = travelLogService.retrieveAllTravelLogs();
        //Then
        TravelLog expectedTravelLog = new TravelLog(DEFAULT_ID, DEFAULT_LOCATION, dateFormat.parse(DEFAULT_DATE_STR));
        List<TravelLog> expectedTravelLogList = Collections.singletonList(expectedTravelLog);
        assertEquals(expectedTravelLogList, foundTravelLogList);
    }

    @Test
    public void testCreateTravelLogEntryForPerson() throws ParseException {
        //Given
        Person person = createDefaultPerson();
        TravelLogDTO travelLogDTO = createDefaultTravelLogDTO();
        when(personRepository.findOneByPgi(anyString())).thenReturn(Optional.of(person));
        when(travelLogRepository
                .existsByTravelLocationAndTravelDateAndPerson(anyString(), any(Date.class), any(Person.class)))
                .thenReturn(false);
        travelLogService = new TravelLogServiceImpl(personRepository, travelLogRepository);
        //When
        TravelLog foundTravelLog = travelLogService.createTravelLogForPerson(travelLogDTO);
        //Then
        TravelLog expectedTravelLog = new TravelLog();
        expectedTravelLog.setTravelLocation(DEFAULT_LOCATION);
        expectedTravelLog.setTravelDate(dateFormat.parse(DEFAULT_DATE_STR));
        expectedTravelLog.setPerson(person);
        assertEquals(expectedTravelLog, foundTravelLog);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testCreateTravelLogEntryForPersonAndThrowException() {
        //Given
        when(personRepository.findOneByPgi(anyString())).thenReturn(Optional.empty());
        when(travelLogRepository
                .existsByTravelLocationAndTravelDateAndPerson(anyString(), any(Date.class), any(Person.class)))
                .thenReturn(false);
        travelLogService = new TravelLogServiceImpl(personRepository, travelLogRepository);
        //When
        travelLogService.createTravelLogForPerson(createDefaultTravelLogDTO());
        //Then expected Resource not Found to be thrown
    }

    @Test(expected = CollisionException.class)
    public void testCreateTravelLogEntryPersonAndThrowCollisionException() {
        //Given
        TravelLog travelLog = createDefaultTravelLog();
        travelLog.setPerson(createDefaultPerson());
        Person person = createDefaultPerson();
        TravelLogDTO travelLogDTO = createDefaultTravelLogDTO();
        when(personRepository.findOneByPgi(anyString())).thenReturn(Optional.of(person));
        when(travelLogRepository
                .existsByTravelLocationAndTravelDateAndPerson(anyString(), any(Date.class), any(Person.class)))
                .thenReturn(true);
        travelLogService = new TravelLogServiceImpl(personRepository, travelLogRepository);
        //When
        travelLogService.createTravelLogForPerson(travelLogDTO);
        //Then expected Collision Exception to be thrown
    }

    @Test
    public void testGetTravelDetailsById() throws ParseException {
        //Given
        TravelLog travelLog = createDefaultTravelLog();
        when(travelLogRepository.findById(anyLong())).thenReturn(Optional.of(travelLog));
        travelLogService = new TravelLogServiceImpl(personRepository, travelLogRepository);
        //When
        TravelLog foundTravelLog = travelLogService.getTravelDetailsById(DEFAULT_ID);
        //Then
        TravelLog expectedTravelLog = new TravelLog(DEFAULT_ID, DEFAULT_LOCATION, dateFormat.parse(DEFAULT_DATE_STR));
        assertEquals(expectedTravelLog, foundTravelLog);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetTravelDetailsByIdButThrowException() {
        //Given
        when(travelLogRepository.findById(anyLong())).thenReturn(Optional.empty());
        travelLogService = new TravelLogServiceImpl(personRepository, travelLogRepository);
        //When
        travelLogService.getTravelDetailsById(DEFAULT_ID);
        //Then expected Resource not Found exception to be thrown
    }

    @Test
    public void testUpdateTravelLogInformation() throws ParseException {
        //Given
        String NEW_LOCATION = "ELSEWHERE";
        String NEW_DATE = "2020-04-21";
        TravelLogDTO travelLogDTO = new TravelLogDTO(
                NEW_LOCATION,
                dateFormat.parse(NEW_DATE),
                DEFAULT_PGI
        );
        Person person = createDefaultPerson();
        TravelLog travelLog = createDefaultTravelLog();
        travelLog.setPerson(person);
        when(personRepository.findOneByPgi(anyString())).thenReturn(Optional.of(person));
        when(travelLogRepository.findById(anyLong())).thenReturn(Optional.of(travelLog));
        when(travelLogRepository
                .existsByTravelLocationAndTravelDateAndPerson(anyString(), any(Date.class), any(Person.class)))
                .thenReturn(false);
        travelLogService = new TravelLogServiceImpl(personRepository, travelLogRepository);
        //When
        TravelLog foundTravelLog = travelLogService.updateTravelLogInfo(travelLogDTO, DEFAULT_ID);
        //Expected
        TravelLog expectedTravelLog = new TravelLog(
                DEFAULT_ID,
                NEW_LOCATION,
                dateFormat.parse(NEW_DATE)
        );
        expectedTravelLog.setPerson(person);
        assertEquals(expectedTravelLog, foundTravelLog);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateTravelLogInformationButThrowNotFoundException() throws ParseException {
        //Given
        String NEW_LOCATION = "ELSEWHERE";
        String NEW_DATE = "2020-04-21";
        TravelLogDTO travelLogDTO = new TravelLogDTO(
                NEW_LOCATION,
                dateFormat.parse(NEW_DATE),
                DEFAULT_PGI
        );
        when(personRepository.findOneByPgi(anyString())).thenReturn(Optional.empty());
        when(travelLogRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(travelLogRepository
                .existsByTravelLocationAndTravelDateAndPerson(anyString(), any(Date.class), any(Person.class)))
                .thenReturn(false);
        travelLogService = new TravelLogServiceImpl(personRepository, travelLogRepository);
        //When
        travelLogService.updateTravelLogInfo(travelLogDTO, DEFAULT_ID);
        //Then expected Resource not Found exception to be thrown
    }

    @Test(expected = CollisionException.class)
    public void testUpdateTravelLogInformationButThrowCollisionException() throws ParseException {
        //Given
        String NEW_LOCATION = "ELSEWHERE";
        String NEW_DATE = "2020-04-21";
        TravelLogDTO travelLogDTO = new TravelLogDTO(
                NEW_LOCATION,
                dateFormat.parse(NEW_DATE),
                DEFAULT_PGI
        );
        Person person = createDefaultPerson();
        TravelLog travelLog = createDefaultTravelLog();
        travelLog.setPerson(person);
        when(personRepository.findOneByPgi(anyString())).thenReturn(Optional.of(person));
        when(travelLogRepository.findById(anyLong())).thenReturn(Optional.of(travelLog));
        when(travelLogRepository
                .existsByTravelLocationAndTravelDateAndPerson(anyString(), any(Date.class), any(Person.class)))
                .thenReturn(true);
        travelLogService = new TravelLogServiceImpl(personRepository, travelLogRepository);
        //When
        travelLogService.updateTravelLogInfo(travelLogDTO, DEFAULT_ID);
        //Then expected Collision exception to be thrown
    }

    private Person createDefaultPerson() {
        Person person = new Person();
        person.setId(DEFAULT_ID);
        person.setPgi(DEFAULT_PGI);
        person.setFirstName(DEFAULT_F_NAME);
        person.setLastName(DEFAULT_L_NAME);
        person.setTravelLogList(Collections.singletonList(createDefaultTravelLog()));
        return person;
    }

    private TravelLog createDefaultTravelLog() {
        TravelLog travelLog = new TravelLog();
        travelLog.setId(DEFAULT_ID);
        travelLog.setTravelLocation(DEFAULT_LOCATION);
        try {
            travelLog.setTravelDate(dateFormat.parse(DEFAULT_DATE_STR));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return travelLog;
    }

    private TravelLogDTO createDefaultTravelLogDTO() {
        TravelLogDTO travelLogDTO = new TravelLogDTO();
        travelLogDTO.setPgi(DEFAULT_PGI);
        travelLogDTO.setTravelLocation(DEFAULT_LOCATION);
        try {
            travelLogDTO.setTravelDate(dateFormat.parse(DEFAULT_DATE_STR));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return travelLogDTO;
    }
}