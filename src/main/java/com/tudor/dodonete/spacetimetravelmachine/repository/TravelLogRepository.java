package com.tudor.dodonete.spacetimetravelmachine.repository;

import com.tudor.dodonete.spacetimetravelmachine.entity.Person;
import com.tudor.dodonete.spacetimetravelmachine.entity.TravelLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TravelLogRepository extends JpaRepository<TravelLog, Long> {
    boolean existsByTravelLocationAndTravelDateAndPerson(String travelLocation, Date travelDate, Person person);
}
