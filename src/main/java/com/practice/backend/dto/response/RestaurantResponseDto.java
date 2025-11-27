package com.practice.backend.dto.response;

import lombok.Value;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Value
public class RestaurantResponseDto {
    @Schema(description = "Уникальный идентификатор ресторана", example = "5")
    Long id;

    @Schema(description = "Название ресторана", example = "Буше")
    String name;

    @Schema(description = "Описание ресторана", example = "Уютный ресторан с авторской кухней")
    String description;

    @Schema(description = "Тип кухни", example = "ITALIAN")
    String cuisineType;

    @Schema(description = "Средний чек в рублях", example = "1500.50")
    BigDecimal averageCheck;

    @Schema(description = "Средний рейтинг от пользователей", example = "4.2")
    BigDecimal userRating;
}
