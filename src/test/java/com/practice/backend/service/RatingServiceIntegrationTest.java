package com.practice.backend.service;

import com.practice.backend.dto.request.RatingRequestDto;
import com.practice.backend.dto.response.RatingResponseDto;
import com.practice.backend.entity.Restaurant;
import com.practice.backend.entity.Visitor;
import com.practice.backend.repository.RatingRepository;
import com.practice.backend.repository.RestaurantRepository;
import com.practice.backend.repository.VisitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class RatingServiceIntegrationTest {

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
    private RatingService ratingService;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    private Visitor testVisitor1;
    private Visitor testVisitor2;
    private Restaurant testRestaurant1;
    private Restaurant testRestaurant2;

    @BeforeEach
    void setUp() {
        ratingRepository.deleteAll();
        visitorRepository.deleteAll();
        restaurantRepository.deleteAll();

        testVisitor1 = new Visitor(null, "John", 30, "Male");
        testVisitor2 = new Visitor(null, "Jane", 25, "Female");
        testVisitor1 = visitorRepository.save(testVisitor1);
        testVisitor2 = visitorRepository.save(testVisitor2);

        testRestaurant1 = new Restaurant(null, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);
        testRestaurant2 = new Restaurant(null, "Sushi Bar", "Fresh sushi", "JAPANESE", new BigDecimal("40.00"), BigDecimal.ZERO);
        testRestaurant1 = restaurantRepository.save(testRestaurant1);
        testRestaurant2 = restaurantRepository.save(testRestaurant2);
    }

    @Test
    void shouldSaveAndFindRating() {
        RatingRequestDto requestDto = new RatingRequestDto(testVisitor1.getId(), testRestaurant1.getId(), 5, "Great!");

        RatingResponseDto saved = ratingService.save(requestDto);

        assertEquals(testVisitor1.getId(), saved.getVisitorId());
        assertEquals(testRestaurant1.getId(), saved.getRestaurantId());
        assertEquals(5, saved.getRating());
        assertEquals("Great!", saved.getReviewText());

        Optional<RatingResponseDto> found = ratingService.findById(testVisitor1.getId(), testRestaurant1.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getVisitorId(), found.get().getVisitorId());
        assertEquals(saved.getRestaurantId(), found.get().getRestaurantId());
    }

    @Test
    void shouldFindAllRatings() {
        RatingRequestDto requestDto1 = new RatingRequestDto(testVisitor1.getId(), testRestaurant1.getId(), 5, "Great!");
        RatingRequestDto requestDto2 = new RatingRequestDto(testVisitor2.getId(), testRestaurant1.getId(), 4, "Good!");

        ratingService.save(requestDto1);
        ratingService.save(requestDto2);

        List<RatingResponseDto> ratings = ratingService.findAll();

        assertEquals(2, ratings.size());
    }

    @Test
    void shouldDeleteRating() {
        RatingRequestDto requestDto = new RatingRequestDto(testVisitor1.getId(), testRestaurant1.getId(), 5, "Great!");
        RatingResponseDto saved = ratingService.save(requestDto);

        ratingService.remove(saved.getVisitorId(), saved.getRestaurantId());

        List<RatingResponseDto> ratings = ratingService.findAll();
        assertEquals(0, ratings.size());

        Optional<RatingResponseDto> foundAfterDelete = ratingService.findById(saved.getVisitorId(), saved.getRestaurantId());
        assertFalse(foundAfterDelete.isPresent());
    }

    @Test
    void shouldFindRatingsByRestaurantId() {
        RatingRequestDto requestDto1 = new RatingRequestDto(testVisitor1.getId(), testRestaurant1.getId(), 5, "Great!");
        RatingRequestDto requestDto2 = new RatingRequestDto(testVisitor2.getId(), testRestaurant1.getId(), 4, "Good!");
        RatingRequestDto requestDto3 = new RatingRequestDto(testVisitor1.getId(), testRestaurant2.getId(), 3, "Average");

        ratingService.save(requestDto1);
        ratingService.save(requestDto2);
        ratingService.save(requestDto3);

        Pageable pageable = PageRequest.of(0, 10);
        Page<RatingResponseDto> ratingsPageForRestaurant1 = ratingService.findRatingsByRestaurantId(testRestaurant1.getId(), pageable);
        Pageable pageable2 = PageRequest.of(0, 10);
        Page<RatingResponseDto> ratingsPageForRestaurant2 = ratingService.findRatingsByRestaurantId(testRestaurant2.getId(), pageable2);

        assertEquals(2, ratingsPageForRestaurant1.getTotalElements());
        assertEquals(1, ratingsPageForRestaurant2.getTotalElements());

        assertEquals(2, ratingsPageForRestaurant1.getContent().size());
        assertEquals(1, ratingsPageForRestaurant2.getContent().size());

        List<RatingResponseDto> content1 = ratingsPageForRestaurant1.getContent();
        List<RatingResponseDto> content2 = ratingsPageForRestaurant2.getContent();

        assertTrue(content1.stream().allMatch(r -> r.getRestaurantId().equals(testRestaurant1.getId())));
        assertTrue(content2.stream().allMatch(r -> r.getRestaurantId().equals(testRestaurant2.getId())));
    }
}