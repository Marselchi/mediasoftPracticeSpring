package com.practice.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;


@Value
public class VisitorRequestDto {
    @Schema(description = "Имя посетителя", example = "Иван")
    String name;

    @Schema(description = "Возраст посетителя в годах", example = "25")
    @NotNull(message = "Возраст обязателен")
    @Min(value = 0, message = "Нельзя отрицательный возраст")
    int age;

    @Schema(description = "Пол посетителя (\"мужской\", \"женский\")", example = "мужской")
    @NotNull(message = "Пол обязателен")
    @NotBlank(message = "Пол не должен быть пустым")
    String gender;
}