package com.practice.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Restaurant {
    private Long id;
    @NonNull
    private String name;
    private String description;
    @NonNull
    private CuisineType cuisineType;
    @NonNull
    private BigDecimal averageCheck;
    @NonNull
    private BigDecimal userRating;
}
