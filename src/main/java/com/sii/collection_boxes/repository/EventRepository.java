package com.sii.collection_boxes.repository;

import com.sii.collection_boxes.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByName(String name);
    Optional<Event> findByName(String name);
}
