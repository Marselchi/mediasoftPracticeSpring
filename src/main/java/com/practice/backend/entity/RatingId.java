package com.practice.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RatingId implements Serializable {
    private Long visitorId;
    private Long restaurantId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingId ratingId = (RatingId) o;
        return Objects.equals(visitorId, ratingId.visitorId) &&
                Objects.equals(restaurantId, ratingId.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visitorId, restaurantId);
    }
}
