package com.practice.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.backend.dto.request.RestaurantRequestDto;
import com.practice.backend.dto.response.RestaurantResponseDto;
import com.practice.backend.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RestaurantControllerIntegrationTest {

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
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void createRestaurant_ShouldReturnCreatedRestaurant() throws Exception {
        RestaurantRequestDto requestDto = new RestaurantRequestDto("Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"));
        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bistro"))
                .andExpect(jsonPath("$.description").value("Great food"))
                .andExpect(jsonPath("$.cuisineType").value("ITALIAN"))
                .andExpect(jsonPath("$.averageCheck").value(25.00))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void getRestaurantById_WhenRestaurantExists_ShouldReturnRestaurant() throws Exception {
        RestaurantRequestDto requestDto = new RestaurantRequestDto("Sushi Bar", "Fresh sushi", "JAPANESE", new BigDecimal("40.00"));
        RestaurantResponseDto createdRestaurant = restaurantService.save(requestDto);
        Long id = createdRestaurant.getId();

        mockMvc.perform(get("/api/restaurants/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Sushi Bar"))
                .andExpect(jsonPath("$.description").value("Fresh sushi"))
                .andExpect(jsonPath("$.cuisineType").value("JAPANESE"))
                .andExpect(jsonPath("$.averageCheck").value(40.00));
    }

    @Test
    void getRestaurantById_WhenRestaurantDoesNotExist_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/restaurants/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllRestaurants_ShouldReturnListOfRestaurants() throws Exception {
        RestaurantRequestDto requestDto1 = new RestaurantRequestDto("Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"));
        RestaurantRequestDto requestDto2 = new RestaurantRequestDto("Sushi Bar", "Fresh sushi", "JAPANESE", new BigDecimal("40.00"));
        restaurantService.save(requestDto1);
        restaurantService.save(requestDto2);

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void updateRestaurant_ShouldReturnUpdatedRestaurant() throws Exception {
        RestaurantRequestDto createDto = new RestaurantRequestDto("Old Name", "Old Desc", "OLD_TYPE", new BigDecimal("10.00"));
        RestaurantResponseDto createdRestaurant = restaurantService.save(createDto);
        Long id = createdRestaurant.getId();

        RestaurantRequestDto updateDto = new RestaurantRequestDto("New Name", "New Desc", "NEW_TYPE", new BigDecimal("50.00"));
        String requestJson = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(put("/api/restaurants/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.description").value("New Desc"))
                .andExpect(jsonPath("$.cuisineType").value("NEW_TYPE"))
                .andExpect(jsonPath("$.averageCheck").value(50.00));
    }

    @Test
    void deleteRestaurant_ShouldReturnOk() throws Exception {
        RestaurantRequestDto requestDto = new RestaurantRequestDto("To Delete", "Desc", "TYPE", new BigDecimal("15.00"));
        RestaurantResponseDto createdRestaurant = restaurantService.save(requestDto);
        Long id = createdRestaurant.getId();

        mockMvc.perform(delete("/api/restaurants/{id}", id))
                .andExpect(status().is2xxSuccessful());
    }
}