package com.practice.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;

@Value
public class RatingRequestDto {

    @Schema(description = "ID посетителя", example = "1")
    @NotNull(message = "ID посетителя обязательно")
    Long visitorId;

    @Schema(description = "ID ресторана", example = "5")
    @NotNull(message = "ID ресторана обязательно")
    Long restaurantId;

    @Schema(description = "Оценка от 1 до 5", example = "4")
    @Min(value = 1, message = "Рейтинг минимум 1")
    @Max(value = 5, message = "Рейтинг максимум 5")
    int rating;
    @Schema(description = "Текст отзыва", example = "Отличная атмосфера и вкусная паста!")
    String reviewText;
}
