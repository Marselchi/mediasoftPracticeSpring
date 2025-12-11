package com.practice.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ratings")
@IdClass(RatingId.class)
public class Rating {
    @Id
    @Column(name = "visitor_id")
    private Long visitorId;
    @Id
    @Column(name = "restaurant_id")
    private Long restaurantId;

    private int rating;
    private String reviewText;

    @ManyToOne
    @JoinColumn(name = "visitor_id", insertable = false, updatable = false)
    private Visitor visitor;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    private Restaurant restaurant;
}
