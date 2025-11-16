package com.practice.backend.service;

import com.practice.backend.entity.Rating;
import com.practice.backend.entity.Restaurant;
import com.practice.backend.repository.RatingRepository;
import com.practice.backend.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;

    public Rating save(Rating rating) {
        Rating savedRating = ratingRepository.save(rating);
        recalculateRestaurantRating(rating.getRestaurantId());
        return savedRating;
    }

    public void remove(Long id) {
        ratingRepository.remove(id);
        for (Restaurant restaurant : restaurantRepository.findAll()) {
            recalculateRestaurantRating(restaurant.getId());
        }
    }

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    private void recalculateRestaurantRating(Long restaurantId) {
        List<Rating> ratings = ratingRepository.findAll().stream()
                .filter(rating -> rating.getRestaurantId().equals(restaurantId))
                .toList();

        if (ratings.isEmpty()) {
            restaurantRepository.findAll().stream()
                    .filter(r -> r.getId().equals(restaurantId)).findFirst()
                    .ifPresent(restaurant -> restaurant.setUserRating(BigDecimal.ZERO));
            return;
        }

        BigDecimal sum = ratings.stream()
                .map(rating -> BigDecimal.valueOf(rating.getRating()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal average = sum.divide(BigDecimal.valueOf(ratings.size()), 2, RoundingMode.HALF_UP);

        restaurantRepository.findAll().stream()
                .filter(r -> r.getId().equals(restaurantId)).findFirst()
                .ifPresent(restaurant -> restaurant.setUserRating(average));
    }
}