package com.practice.backend.service;

import com.practice.backend.dto.request.RestaurantRequestDto;
import com.practice.backend.dto.response.RestaurantResponseDto;
import com.practice.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class RestaurantServiceIntegrationTest {

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
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindRestaurant() {
        RestaurantRequestDto requestDto = new RestaurantRequestDto("Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"));

        RestaurantResponseDto saved = restaurantService.save(requestDto);

        assertNotNull(saved.getId());
        assertEquals("Bistro", saved.getName());
        assertEquals("Great food", saved.getDescription());
        assertEquals("ITALIAN", saved.getCuisineType());
        assertEquals(new BigDecimal("25.00"), saved.getAverageCheck());

        RestaurantResponseDto found = restaurantService.findById(saved.getId());
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void shouldFindAllRestaurants() {
        RestaurantRequestDto requestDto1 = new RestaurantRequestDto("Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"));
        RestaurantRequestDto requestDto2 = new RestaurantRequestDto("Sushi Bar", "Fresh sushi", "JAPANESE", new BigDecimal("40.00"));

        restaurantService.save(requestDto1);
        restaurantService.save(requestDto2);

        List<RestaurantResponseDto> restaurants = restaurantService.findAll();

        assertEquals(2, restaurants.size());
    }

    @Test
    void shouldDeleteRestaurant() {
        RestaurantRequestDto requestDto = new RestaurantRequestDto("Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"));
        RestaurantResponseDto saved = restaurantService.save(requestDto);

        restaurantService.remove(saved.getId());

        List<RestaurantResponseDto> restaurants = restaurantService.findAll();
        assertEquals(0, restaurants.size());
    }
}