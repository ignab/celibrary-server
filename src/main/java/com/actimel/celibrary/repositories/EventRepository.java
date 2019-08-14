package com.actimel.celibrary.repositories;

import com.actimel.celibrary.models.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findById(Long placeId);

    Page<Event> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    List<Event> findByIdIn(List<Long> placeIds);

    List<Event> findByIdIn(List<Long> placeIds, Sort sort);
   
}
