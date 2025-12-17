package com.practice.backend.service;

import com.practice.backend.dto.request.RatingRequestDto;
import com.practice.backend.dto.response.RatingResponseDto;
import com.practice.backend.entity.Rating;
import com.practice.backend.entity.RatingId;
import com.practice.backend.entity.Restaurant;
import com.practice.backend.mapper.RatingMapper;
import com.practice.backend.repository.RatingRepository;
import com.practice.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RatingMapper ratingMapper;

    private final Restaurant testRestaurant = new Restaurant(1L, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);

    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        ratingService = new RatingService(ratingRepository, restaurantRepository, ratingMapper);
    }

    @Test
    void save_ShouldReturnRatingResponseDto() {
        RatingRequestDto requestDto = new RatingRequestDto(1L, 1L, 5, "Great!");
        Rating entity = new Rating(1L, 1L, 5, "Great!", null, null);
        RatingResponseDto responseDto = new RatingResponseDto(1L, 1L, 5, "Great!");

        when(ratingMapper.toEntity(any(RatingRequestDto.class))).thenReturn(entity);
        when(ratingRepository.save(any(Rating.class))).thenReturn(entity);
        when(ratingMapper.toResponseDto(any(Rating.class))).thenReturn(responseDto);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(testRestaurant));
        RatingResponseDto result = ratingService.save(requestDto);

        assertEquals(responseDto, result);
        verify(ratingRepository).save(entity);
    }

    @Test
    void findById_WhenRatingExists_ShouldReturnRatingResponseDto() {
        RatingId ratingId = new RatingId(1L, 1L);
        Rating entity = new Rating(1L, 1L, 5, "Great!", null, null);
        RatingResponseDto responseDto = new RatingResponseDto(1L, 1L, 5, "Great!");

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(entity));
        when(ratingMapper.toResponseDto(any(Rating.class))).thenReturn(responseDto);

        Optional<RatingResponseDto> result = ratingService.findById(1L, 1L);

        assertTrue(result.isPresent());
        assertEquals(responseDto, result.get());
    }

    @Test
    void findById_WhenRatingDoesNotExist_ShouldReturnEmptyOptional() {
        RatingId ratingId = new RatingId(1L, 1L);

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.empty());

        Optional<RatingResponseDto> result = ratingService.findById(1L, 1L);

        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnListOfRatingResponseDto() {
        Rating entity1 = new Rating(1L, 1L, 5, "Great!", null, null);
        Rating entity2 = new Rating(2L, 1L, 4, "Good!", null, null);
        RatingResponseDto responseDto1 = new RatingResponseDto(1L, 1L, 5, "Great!");
        RatingResponseDto responseDto2 = new RatingResponseDto(2L, 1L, 4, "Good!");

        when(ratingRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(ratingMapper.toResponseDto(entity1)).thenReturn(responseDto1);
        when(ratingMapper.toResponseDto(entity2)).thenReturn(responseDto2);

        List<RatingResponseDto> result = ratingService.findAll();

        assertEquals(2, result.size());
        assertEquals(responseDto1, result.get(0));
        assertEquals(responseDto2, result.get(1));
    }

    @Test
    void remove_ShouldCallRepositoryDelete() {
        RatingId ratingId = new RatingId(1L, 1L);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(testRestaurant));

        ratingService.remove(1L, 1L);

        verify(ratingRepository).deleteById(ratingId);
    }
}
