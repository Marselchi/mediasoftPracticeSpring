package com.practice.backend.repository;

import com.practice.backend.entity.*;
import org.junit.jupiter.api.BeforeEach;
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
public class RatingRepositoryIntegrationTest {

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
    private RatingRepository ratingRepository;

    //Для ключей
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    private Visitor visitor1;
    private Visitor visitor2;
    private Visitor visitor3;
    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        ratingRepository.deleteAll();
        visitorRepository.deleteAll();
        restaurantRepository.deleteAll();

        visitor1 = new Visitor(null, "John", 30, "Male");
        visitor2 = new Visitor(null, "Jane", 25, "Female");
        visitor3 = new Visitor(null, "Bob", 35, "Other");
        visitor1 = visitorRepository.save(visitor1);
        visitor2 = visitorRepository.save(visitor2);
        visitor3 = visitorRepository.save(visitor3);

        restaurant1 = new Restaurant(null, "Bistro", "Great food",
                "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);
        restaurant2 = new Restaurant(null, "Sushi Bar", "Fresh sushi",
                "JAPANESE", new BigDecimal("40.00"), BigDecimal.ZERO);
        restaurant1 = restaurantRepository.save(restaurant1);
        restaurant2 = restaurantRepository.save(restaurant2);
    }


    @Test
    void shouldSaveAndFindRating() {
        Rating rating = new Rating(visitor1.getId(), restaurant1.getId(), 5, "Great!", null, null);

        Rating saved = ratingRepository.save(rating);

        assertNotNull(saved);
        RatingId ratingId = new RatingId(visitor1.getId(), restaurant1.getId());
        Optional<Rating> found = ratingRepository.findById(ratingId);
        assertTrue(found.isPresent());
        assertEquals(5, found.get().getRating());
        assertEquals("Great!", found.get().getReviewText());
    }

    @Test
    void shouldFindAllRatings() {
        Rating rating1 = new Rating(visitor1.getId(), restaurant1.getId(), 5, "Great!", null, null);
        Rating rating2 = new Rating(visitor2.getId(), restaurant1.getId(), 4, "Good!", null, null);

        ratingRepository.save(rating1);
        ratingRepository.save(rating2);

        List<Rating> ratings = ratingRepository.findAll();

        assertEquals(2, ratings.size());
    }

    @Test
    void shouldFindByRestaurantId() {
        Rating rating1 = new Rating(visitor1.getId(), restaurant1.getId(), 5, "Great!", null, null);
        Rating rating2 = new Rating(visitor2.getId(), restaurant1.getId(), 4, "Good!", null, null);
        Rating rating3 = new Rating(visitor3.getId(), restaurant2.getId(), 3, "Average", null, null);

        ratingRepository.save(rating1);
        ratingRepository.save(rating2);
        ratingRepository.save(rating3);

        List<Rating> ratingsForRestaurant1 = ratingRepository.findByRestaurantId(restaurant1.getId());
        List<Rating> ratingsForRestaurant2 = ratingRepository.findByRestaurantId(restaurant2.getId());

        assertEquals(2, ratingsForRestaurant1.size());
        assertEquals(1, ratingsForRestaurant2.size());
    }

    @Test
    void shouldDeleteRating() {
        Rating rating = new Rating(visitor1.getId(), restaurant1.getId(), 5, "Great!", null, null);
        Rating savedRating = ratingRepository.save(rating);
        RatingId ratingId = new RatingId(savedRating.getVisitorId(), savedRating.getRestaurantId());

        ratingRepository.deleteById(ratingId);

        Optional<Rating> found = ratingRepository.findById(ratingId);
        assertFalse(found.isPresent());
    }
}