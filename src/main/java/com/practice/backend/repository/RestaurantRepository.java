package com.practice.backend.repository;

import com.practice.backend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByUserRatingGreaterThanEqual(BigDecimal minRating);

    // Напишу одну с Query что-ли
    @Query("SELECT r FROM Restaurant r WHERE r.userRating >= :minRating ORDER BY r.userRating DESC")
    List<Restaurant> findRestaurantsWithRatingAbove(@Param("minRating") BigDecimal minRating);
}
