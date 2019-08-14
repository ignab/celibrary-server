// package com.actimel.celibrary.repositories;

// import com.actimel.celibrary.models.Place;

// import org.springframework.data.repository.CrudRepository;

// public interface PlaceRepository extends CrudRepository<Place, Long> {
   
// }

package com.actimel.celibrary.repositories;

import com.actimel.celibrary.models.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findById(Long placeId);

    Page<Place> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    List<Place> findByIdIn(List<Long> placeIds);

    List<Place> findByIdIn(List<Long> placeIds, Sort sort);
}
