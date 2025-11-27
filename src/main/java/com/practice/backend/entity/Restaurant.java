package com.practice.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    private Long id;
    private String name;
    private String description;
    private String cuisineType;
    private BigDecimal averageCheck;
    private BigDecimal userRating;
}
