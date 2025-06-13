package com.sii.collection_boxes.repository;

import com.sii.collection_boxes.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
