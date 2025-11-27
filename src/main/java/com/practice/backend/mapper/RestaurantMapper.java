package com.practice.backend.mapper;

import com.practice.backend.dto.request.RestaurantRequestDto;
import com.practice.backend.dto.response.RestaurantResponseDto;
import com.practice.backend.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userRating", ignore = true)
    Restaurant toEntity(RestaurantRequestDto requestDto);

    RestaurantResponseDto toResponseDto(Restaurant entity);
}
