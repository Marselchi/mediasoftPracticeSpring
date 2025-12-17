package com.practice.backend.repository;

import com.practice.backend.entity.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
public class RestaurantRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void shouldSaveAndFindRestaurant() {
        Restaurant restaurant = new Restaurant(null, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);
        Restaurant saved = restaurantRepository.save(restaurant);

        assertNotNull(saved.getId());
        assertEquals("Bistro", saved.getName());
        assertEquals("Great food", saved.getDescription());
        assertEquals("ITALIAN", saved.getCuisineType());
        assertEquals(new BigDecimal("25.00"), saved.getAverageCheck());

        Optional<Restaurant> foundOptional = restaurantRepository.findById(saved.getId());
        assertTrue(foundOptional.isPresent());
        Restaurant found = foundOptional.get();
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void shouldFindAllRestaurants() {
        Restaurant restaurant1 = new Restaurant(null, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);
        Restaurant restaurant2 = new Restaurant(null, "Sushi Bar", "Fresh sushi", "JAPANESE", new BigDecimal("40.00"), BigDecimal.ZERO);

        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);

        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertEquals(2, restaurants.size());
    }

    @Test
    void shouldDeleteRestaurant() {
        Restaurant restaurant = new Restaurant(null, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);
        Restaurant saved = restaurantRepository.save(restaurant);

        restaurantRepository.deleteById(saved.getId());
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertEquals(0, restaurants.size());
    }
}
