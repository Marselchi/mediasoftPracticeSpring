package com.practice.backend.service;

import com.practice.backend.dto.request.RestaurantRequestDto;
import com.practice.backend.dto.response.RestaurantResponseDto;
import com.practice.backend.entity.Restaurant;
import com.practice.backend.mapper.RestaurantMapper;
import com.practice.backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import java.math.BigDecimal;

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

    public void remove(Long id) {
        restaurantRepository.deleteById(id);
    }

    public List<RestaurantResponseDto> findAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toResponseDto)
                .toList();
    }

    public RestaurantResponseDto findById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
        return restaurantMapper.toResponseDto(restaurant);
        /* Можно еще вот так вот, если обрабатывать null надо а не ошибку выкидывать, но в тз не указано
        *  Restaurant restaurant = restaurantRepository.findById(id).orElse(null);
        *  return restaurant != null ? restaurantMapper.toResponseDto(restaurant) : null;
        * */
    }

    public RestaurantResponseDto update(Long id, RestaurantRequestDto requestDto) {
        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));

        existingRestaurant.setName(requestDto.getName());
        existingRestaurant.setDescription(requestDto.getDescription());
        existingRestaurant.setCuisineType(requestDto.getCuisineType());
        existingRestaurant.setAverageCheck(requestDto.getAverageCheck());

        Restaurant updatedRestaurant = restaurantRepository.save(existingRestaurant);
        return restaurantMapper.toResponseDto(updatedRestaurant);
    }


    //Query можно было и не писать если что, вот так примерно было бы
    public List<RestaurantResponseDto> findRestaurantsWithRatingAbove(BigDecimal minRating) {
        return restaurantRepository.findByUserRatingGreaterThanEqual(minRating).stream()
                .map(restaurantMapper::toResponseDto)
                .toList();
    }

    public List<RestaurantResponseDto> findRestaurantsWithRatingAboveJPQL(BigDecimal minRating) {
        return restaurantRepository.findRestaurantsWithRatingAbove(minRating).stream()
                .map(restaurantMapper::toResponseDto)
                .toList();
    }
}