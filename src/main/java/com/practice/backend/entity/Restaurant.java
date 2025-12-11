package com.practice.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String cuisineType;
    private BigDecimal averageCheck;
    private BigDecimal userRating;
}
