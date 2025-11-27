package com.practice.backend.mapper;

import com.practice.backend.dto.request.RatingRequestDto;
import com.practice.backend.dto.response.RatingResponseDto;
import com.practice.backend.entity.Rating;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    Rating toEntity(RatingRequestDto requestDto);

    RatingResponseDto toResponseDto(Rating entity);
}
