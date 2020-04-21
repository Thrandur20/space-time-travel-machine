package com.tudor.dodonete.spacetimetravelmachine.controller;

import com.tudor.dodonete.spacetimetravelmachine.dto.TravelLogDTO;
import com.tudor.dodonete.spacetimetravelmachine.entity.TravelLog;
import com.tudor.dodonete.spacetimetravelmachine.service.TravelLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class TravelLogController {

    TravelLogService travelLogService;

    @Autowired
    public TravelLogController(TravelLogService travelLogService) {
        this.travelLogService = travelLogService;
    }

    @GetMapping("/api/travels")
    public List<TravelLog> retrieveAllTravelLogs() {
        return travelLogService.retrieveAllTravelLogs();
    }

    @GetMapping("/api/people/{pgi}/travels")
    public List<TravelLog> retrieveAllLogsByPerson(@PathVariable String pgi) {
        return travelLogService.retrieveTravelLogByPerson(pgi);
    }

    @PostMapping("/api/travels")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TravelLog> createTravelLog(@Valid @RequestBody TravelLogDTO travelLog) {
        TravelLog savedLog = travelLogService.createTravelLogForPerson(travelLog);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedLog.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/api/people/{pgi}/travels/{id}")
    public EntityModel<TravelLog> retrieveTravelLogFromPersonWithId(@PathVariable String pgi, @PathVariable Long id) {
        TravelLog foundTravel = travelLogService.getTravelDetailsById(id);
        EntityModel<TravelLog> travelEntityModel = EntityModel.of(foundTravel);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllLogsByPerson(pgi));
        travelEntityModel.add(linkTo.withRel("all-travel-logs-by-person"));
        return travelEntityModel;
    }

    @GetMapping("/api/travels/{id}")
    public EntityModel<TravelLog> retrieveTravelLogWithId(@PathVariable Long id) {
        TravelLog foundTravel = travelLogService.getTravelDetailsById(id);
        EntityModel<TravelLog> travelEntityModel = EntityModel.of(foundTravel);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllTravelLogs());
        travelEntityModel.add(linkTo.withRel("all-travel-logs"));
        return travelEntityModel;
    }

    @PutMapping("/api/travels/{id}")
    public EntityModel<TravelLog> updateTravelLogInformation(@Valid @RequestBody TravelLogDTO travelLog,
                                                             @PathVariable Long id) {

        TravelLog updatedTravelLog = travelLogService.updateTravelLogInfo(travelLog, id);

        EntityModel<TravelLog> travelEntityModel = EntityModel.of(updatedTravelLog);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllTravelLogs());
        travelEntityModel.add(linkTo.withRel("all-travel-logs"));
        return travelEntityModel;
    }
}
