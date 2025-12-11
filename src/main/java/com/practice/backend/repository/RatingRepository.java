package com.practice.backend.repository;

import com.practice.backend.entity.Rating;
import com.practice.backend.entity.RatingId;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    @Override
    @NonNull
    //Сортировка внутри контроллера настраивается
    Page<Rating> findAll(@NonNull Pageable pageable);

    Page<Rating> findByRestaurantId(Long restaurantId, Pageable pageable);

    List<Rating> findByRestaurantId(Long restaurantId);
}
