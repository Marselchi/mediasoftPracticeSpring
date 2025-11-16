package com.practice.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Rating {
    private Long visitorId;
    @NonNull
    private Long restaurantId;
    @NonNull
    private Integer rating;
    private String reviewText;
}
