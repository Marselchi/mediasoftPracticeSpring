package com.practice.backend.dto.response;

import lombok.Value;

import io.swagger.v3.oas.annotations.media.Schema;

@Value
public class RatingResponseDto {
    @Schema(description = "ID посетителя", example = "1")
    Long visitorId;

    @Schema(description = "ID ресторана", example = "5")
    Long restaurantId;

    @Schema(description = "Оценка от 1 до 5", example = "4")
    int rating;

    @Schema(description = "Текст отзыва", example = "Отличная атмосфера и вкусная паста!")
    String reviewText;
}