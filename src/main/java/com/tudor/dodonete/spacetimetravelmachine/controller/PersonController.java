package com.tudor.dodonete.spacetimetravelmachine.controller;

import com.tudor.dodonete.spacetimetravelmachine.entity.Person;
import com.tudor.dodonete.spacetimetravelmachine.service.PersonService;
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
public class PersonController {
    PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(path = "/api/people")
    public List<Person> retrieveAllPeople() {
        return personService.getAllPeople();
    }

    @PostMapping(path = "/api/people")
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person person) {
        Person savedPerson = personService.createPerson(person);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPerson.getPgi())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/api/people/{pgi}")
    public EntityModel<Person> retrievePersonByPGI(@PathVariable String pgi) {
        Person foundPerson = personService.getPersonDetails(pgi);
        EntityModel<Person> personEntityModel = EntityModel.of(foundPerson);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllPeople());
        personEntityModel.add(linkTo.withRel("all-people"));
        return personEntityModel;
    }

    @DeleteMapping(path = "/api/people/{pgi}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable String pgi) {
        personService.deletePerson(pgi);
    }

    @PutMapping(path = "/api/people/{pgi}")
    public Person updatePerson(@PathVariable String pgi, @Valid @RequestBody Person person) {
        return personService.updatePersonDetails(pgi, person);
    }
}
