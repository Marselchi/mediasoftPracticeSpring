package com.practice.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Value
public class RestaurantRequestDto {

    @Schema(description = "Название ресторана", example = "Буше")
    @NotNull(message = "Имя обязательно")
    @NotBlank(message = "Имя не должно быть пустым")
    String name;

    @Schema(description = "Описание ресторана", example = "Уютный ресторан с авторской кухней")
    String description;

    @Schema(description = "Тип кухни", example = "ITALIAN")
    @NotNull(message = "Тип кухни обязателен")
    String cuisineType;

    @Schema(description = "Средний чек в рублях", example = "1500.50")
    @NotNull(message = "Средний чек обязателен")
    BigDecimal averageCheck;
}
