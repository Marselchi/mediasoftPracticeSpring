package com.practice.backend.service;

import com.practice.backend.dto.request.RestaurantRequestDto;
import com.practice.backend.dto.response.RestaurantResponseDto;
import com.practice.backend.entity.Restaurant;
import com.practice.backend.mapper.RestaurantMapper;
import com.practice.backend.repository.RestaurantRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantResponseDto save(RestaurantRequestDto requestDto) {
        Restaurant restaurant = restaurantMapper.toEntity(requestDto);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponseDto(savedRestaurant);
    }

    public boolean remove(Long id) {
        return restaurantRepository.remove(id);
    }

    public List<RestaurantResponseDto> findAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toResponseDto)
                .toList();
    }

    public RestaurantResponseDto update(Long id, @Valid RestaurantRequestDto requestDto) {
        Restaurant existingRestaurant = restaurantRepository.findById(id);
        if (existingRestaurant == null) {
            throw new RuntimeException("Ресторан не найден: " + id);
        }
        existingRestaurant.setName(requestDto.getName());
        existingRestaurant.setDescription(requestDto.getDescription());
        existingRestaurant.setAverageCheck (requestDto.getAverageCheck());
        existingRestaurant.setCuisineType(requestDto.getCuisineType());

        Restaurant updatedRestaurant = restaurantRepository.save(existingRestaurant);
        return restaurantMapper.toResponseDto(updatedRestaurant);
    }

    public RestaurantResponseDto findById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id);
        return restaurant != null ? restaurantMapper.toResponseDto(restaurant) : null;
    }
}