package com.practice.backend.repository;

import com.practice.backend.entity.Rating;
import org.springframework.stereotype.Repository;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Repository
public class RatingRepository {
    private final List<Rating> ratings = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Rating save(Rating rating) {
        ratings.add(rating);
        return rating;
    }

    public void remove(Long id) {
        ratings.removeIf(rating -> rating.getVisitorId().equals(id));
    }

    public List<Rating> findAll() {
        return new ArrayList<>(ratings);
    }

    public Optional<Rating> findById(Long id) {
        return ratings.stream()
                .filter(rating -> rating.getVisitorId().equals(id))
                .findFirst();
    }
}
