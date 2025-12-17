package com.practice.backend.service;

import com.practice.backend.dto.request.RestaurantRequestDto;
import com.practice.backend.dto.response.RestaurantResponseDto;
import com.practice.backend.entity.Restaurant;
import com.practice.backend.mapper.RestaurantMapper;
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
public class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        restaurantService = new RestaurantService(restaurantRepository, restaurantMapper);
    }

    @Test
    void save_ShouldReturnRestaurantResponseDto() {
        RestaurantRequestDto requestDto = new RestaurantRequestDto("Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"));
        Restaurant entity = new Restaurant(1L, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);
        RestaurantResponseDto responseDto = new RestaurantResponseDto(1L, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);

        when(restaurantMapper.toEntity(any(RestaurantRequestDto.class))).thenReturn(entity);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(entity);
        when(restaurantMapper.toResponseDto(any(Restaurant.class))).thenReturn(responseDto);

        RestaurantResponseDto result = restaurantService.save(requestDto);

        assertEquals(responseDto, result);
        verify(restaurantRepository).save(entity);
    }

    @Test
    void findById_WhenRestaurantExists_ShouldReturnRestaurantResponseDto() {
        Restaurant entity = new Restaurant(1L, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);
        RestaurantResponseDto responseDto = new RestaurantResponseDto(1L, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(restaurantMapper.toResponseDto(any(Restaurant.class))).thenReturn(responseDto);

        RestaurantResponseDto result = restaurantService.findById(1L);

        assertEquals(responseDto, result);
    }

    @Test
    void findById_WhenRestaurantDoesNotExist_ShouldThrowException() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> restaurantService.findById(1L));
    }

    @Test
    void findAll_ShouldReturnListOfRestaurantResponseDto() {
        Restaurant entity1 = new Restaurant(1L, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);
        Restaurant entity2 = new Restaurant(2L, "Sushi Bar", "Fresh sushi", "JAPANESE", new BigDecimal("40.00"), BigDecimal.ZERO);
        RestaurantResponseDto responseDto1 = new RestaurantResponseDto(1L, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);
        RestaurantResponseDto responseDto2 = new RestaurantResponseDto(2L, "Sushi Bar", "Fresh sushi", "JAPANESE", new BigDecimal("40.00"), BigDecimal.ZERO);

        when(restaurantRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(restaurantMapper.toResponseDto(entity1)).thenReturn(responseDto1);
        when(restaurantMapper.toResponseDto(entity2)).thenReturn(responseDto2);

        List<RestaurantResponseDto> result = restaurantService.findAll();

        assertEquals(2, result.size());
        assertEquals(responseDto1, result.get(0));
        assertEquals(responseDto2, result.get(1));
    }

    @Test
    void remove_ShouldCallRepositoryDelete() {
        restaurantService.remove(1L);

        verify(restaurantRepository).deleteById(1L);
    }
}
