package com.tudor.dodonete.spacetimetravelmachine.repository;

import com.tudor.dodonete.spacetimetravelmachine.entity.TravelLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelLogRepository extends JpaRepository<TravelLog, Long> {
}
