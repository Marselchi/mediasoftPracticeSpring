package com.practice.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    private Long visitorId;
    private Long restaurantId;
    private int rating;
    private String reviewText;
}
