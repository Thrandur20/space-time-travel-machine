package com.tudor.dodonete.spacetimetravelmachine.repository;

import com.tudor.dodonete.spacetimetravelmachine.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findOneByPgi(String pgi);

    boolean existsByPgi(String pgi);
}
