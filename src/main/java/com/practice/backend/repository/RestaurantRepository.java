package com.practice.backend.repository;

import com.practice.backend.entity.Restaurant;
import org.springframework.stereotype.Repository;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Repository
public class RestaurantRepository {
    private final List<Restaurant> restaurants = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Restaurant save(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            restaurant.setId(idGenerator.getAndIncrement());
        }
        restaurants.add(restaurant);
        return restaurant;
    }

    public boolean remove(Long id) {
        return restaurants.removeIf(rest -> rest.getId().equals(id));
    }

    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants);
    }

    public Restaurant findById(Long id) {
        return restaurants.stream()
                .filter(rest -> rest.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
