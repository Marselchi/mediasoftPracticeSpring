package com.practice.backend.service;

import com.practice.backend.dto.request.RatingRequestDto;
import com.practice.backend.dto.response.RatingResponseDto;
import com.practice.backend.entity.Rating;
import com.practice.backend.entity.RatingId;
import com.practice.backend.entity.Restaurant;
import com.practice.backend.mapper.RatingMapper;
import com.practice.backend.repository.RatingRepository;
import com.practice.backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    private final RatingMapper ratingMapper;

    public RatingResponseDto save(RatingRequestDto requestDto) {
        Rating rating = ratingMapper.toEntity(requestDto);
        Rating savedRating = ratingRepository.save(rating);
        recalculateRestaurantRating(requestDto.getRestaurantId());
        return ratingMapper.toResponseDto(savedRating);
    }

    public void remove(Long visitorId, Long restaurantId) {
        RatingId ratingId = new RatingId(visitorId, restaurantId);
        ratingRepository.deleteById(ratingId);
        recalculateRestaurantRating(restaurantId);
    }

    public List<RatingResponseDto> findAll() {
        return ratingRepository.findAll().stream()
                .map(ratingMapper::toResponseDto)
                .toList();
    }

    public Optional<RatingResponseDto> findById(Long visitorId, Long restaurantId) {
        RatingId ratingId = new RatingId(visitorId, restaurantId);
        Optional<Rating> rating = ratingRepository.findById(ratingId);
        return rating.map(ratingMapper::toResponseDto);
    }

    public Page<RatingResponseDto> findAllWithPagination(Pageable pageable) {
        Page<Rating> ratingsPage = ratingRepository.findAll(pageable);
        return ratingsPage.map(ratingMapper::toResponseDto);
    }

    public Page<RatingResponseDto> findRatingsByRestaurantId(Long restaurantId, Pageable pageable) {
        Page<Rating> ratingsPage = ratingRepository.findByRestaurantId(restaurantId, pageable);
        return ratingsPage.map(ratingMapper::toResponseDto);
    }

    private void recalculateRestaurantRating(Long restaurantId) {
        List<Rating> ratings = ratingRepository.findByRestaurantId(restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));
        if (ratings.isEmpty()) {
            restaurant.setUserRating(BigDecimal.ZERO);
            return;
        }

        BigDecimal sum = ratings.stream()
                .map(rating -> BigDecimal.valueOf(rating.getRating()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal average = sum.divide(BigDecimal.valueOf(ratings.size()), 2, RoundingMode.HALF_UP);

        restaurant.setUserRating(average);
        restaurantRepository.save(restaurant);
    }
}