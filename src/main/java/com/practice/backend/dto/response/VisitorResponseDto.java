package com.practice.backend.dto.response;

import lombok.Value;

import io.swagger.v3.oas.annotations.media.Schema;

@Value
public class VisitorResponseDto {
    @Schema(description = "Уникальный идентификатор посетителя", example = "1")
    Long id;

    @Schema(description = "Имя посетителя", example = "Анна")
    String name;

    @Schema(description = "Возраст посетителя в годах", example = "30")
    int age;

    @Schema(description = "Пол посетителя", example = "женский")
    String gender;
}