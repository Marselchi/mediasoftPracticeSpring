package com.practice.backend.service;

import com.practice.backend.dto.request.RatingRequestDto;
import com.practice.backend.dto.response.RatingResponseDto;
import com.practice.backend.entity.Rating;
import com.practice.backend.entity.Restaurant;
import com.practice.backend.mapper.RatingMapper;
import com.practice.backend.repository.RatingRepository;
import com.practice.backend.repository.RestaurantRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    private final RatingMapper ratingMapper;

    public RatingResponseDto save(RatingRequestDto requestDto) {
        //Не уверен кстати нужно ли возвращать при сейве, по хорошему
        //наверное нет, но оставлю так т.к четко не указано
        Rating rating = ratingMapper.toEntity(requestDto);
        Rating savedRating = ratingRepository.save(rating);
        recalculateRestaurantRating(requestDto.getRestaurantId());
        return ratingMapper.toResponseDto(savedRating);
    }

    public boolean remove(Long id) {
        boolean removed = ratingRepository.remove(id);
        for (Restaurant restaurant : restaurantRepository.findAll()) {
            recalculateRestaurantRating(restaurant.getId());
        }
        return removed;
    }

    public RatingResponseDto update(Long id, @Valid RatingRequestDto requestDto) {
        Rating existingRating = ratingRepository.findById(id);
        if (existingRating == null) {
            throw new RuntimeException("Отзыв не найден: " + id);
        }
        existingRating.setRestaurantId(requestDto.getRestaurantId());
        existingRating.setVisitorId(requestDto.getVisitorId());
        existingRating.setRating(requestDto.getRating());
        existingRating.setReviewText(requestDto.getReviewText());

        Rating updatedRating = ratingRepository.save(existingRating);
        return ratingMapper.toResponseDto(updatedRating);
    }

    public List<RatingResponseDto> findAll() {
        return ratingRepository.findAll().stream()
                .map(ratingMapper::toResponseDto)
                .toList();
    }

    public RatingResponseDto findById(Long id) {
        Rating rating = ratingRepository.findById(id);
        return rating != null ? ratingMapper.toResponseDto(rating) : null;
    }

    private void recalculateRestaurantRating(Long restaurantId) {
        List<Rating> ratings = ratingRepository.findAll().stream()
                .filter(rating -> rating.getRestaurantId().equals(restaurantId))
                .toList();

        if (ratings.isEmpty()) {
            restaurantRepository.findAll().stream()
                    .filter(r -> r.getId().equals(restaurantId))
                    .findFirst().ifPresent(restaurant -> restaurant.setUserRating(BigDecimal.ZERO));
            return;
        }

        BigDecimal sum = ratings.stream()
                .map(rating -> BigDecimal.valueOf(rating.getRating()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal average = sum.divide(BigDecimal.valueOf(ratings.size()), 2, RoundingMode.HALF_UP);

        restaurantRepository.findAll().stream()
                .filter(r -> r.getId().equals(restaurantId))
                .findFirst().ifPresent(restaurant -> restaurant.setUserRating(average));
    }
}