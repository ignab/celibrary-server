package com.actimel.celibrary.repositories;

import com.actimel.celibrary.models.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long placeId);

    Page<Comment> findByUsersId(Long userId, Pageable pageable);

    long countByUsersId(Long userId);

    List<Comment> findByPlaceIdIn(Long placeIds);

    List<Comment> findByPlaceIdIn(List<Long> placeIds);

    List<Comment> findByPlaceIdIn(List<Long> placeIds, Sort sort);

    // List<Comment> findByIdIn(List<Long> placeIds);

    // List<Comment> findByIdIn(List<Long> placeIds, Sort sort);
}