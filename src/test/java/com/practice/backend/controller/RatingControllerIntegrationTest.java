package com.practice.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.backend.dto.request.RatingRequestDto;
import com.practice.backend.dto.request.RestaurantRequestDto;
import com.practice.backend.dto.request.VisitorRequestDto;
import com.practice.backend.dto.response.RatingResponseDto;
import com.practice.backend.dto.response.RestaurantResponseDto;
import com.practice.backend.dto.response.VisitorResponseDto;
import com.practice.backend.service.RatingService;
import com.practice.backend.service.RestaurantService;
import com.practice.backend.service.VisitorService;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RatingControllerIntegrationTest {

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
    private RatingService ratingService;

    @Autowired
    private VisitorService visitorService;
    @Autowired
    private RestaurantService restaurantService;

    private VisitorResponseDto testVisitor1;
    private VisitorResponseDto testVisitor2;
    private RestaurantResponseDto testRestaurant1;
    private RestaurantResponseDto testRestaurant2;

    @BeforeEach
    void setUp() {
        //Тут не убрал потому что оценки не отваливаются с DirtiesContext
        //Вообще я про него поздновато вспомнил, но в тз про него ничего сказано не было
        //Так что я оставлю где-то так, а где-то через него
        List<RatingResponseDto> allRatings = ratingService.findAll();
        for (RatingResponseDto r : allRatings) {
            ratingService.remove(r.getVisitorId(), r.getRestaurantId());
        }
        List<VisitorResponseDto> allVisitors = visitorService.findAll();
        for (VisitorResponseDto v : allVisitors) {
            visitorService.remove(v.getId());
        }
        List<RestaurantResponseDto> allRestaurants = restaurantService.findAll();
        for (RestaurantResponseDto r : allRestaurants) {
            restaurantService.remove(r.getId());
        }

        VisitorRequestDto visitorDto1 = new VisitorRequestDto("John", 30, "Male");
        VisitorRequestDto visitorDto2 = new VisitorRequestDto("Jane", 25, "Female");
        testVisitor1 = visitorService.save(visitorDto1);
        testVisitor2 = visitorService.save(visitorDto2);

        RestaurantRequestDto restaurantDto1 = new RestaurantRequestDto("Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"));
        RestaurantRequestDto restaurantDto2 = new RestaurantRequestDto("Sushi Bar", "Fresh sushi", "JAPANESE", new BigDecimal("40.00"));
        testRestaurant1 = restaurantService.save(restaurantDto1);
        testRestaurant2 = restaurantService.save(restaurantDto2);
    }

    @Test
    void createRating_ShouldReturnCreatedRating() throws Exception {
        RatingRequestDto requestDto = new RatingRequestDto(testVisitor1.getId(), testRestaurant1.getId(), 5, "Great!");
        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.visitorId").value(testVisitor1.getId()))
                .andExpect(jsonPath("$.restaurantId").value(testRestaurant1.getId()))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.reviewText").value("Great!"));
    }

    @Test
    void getRatingById_WhenRatingExists_ShouldReturnRating() throws Exception {
        RatingRequestDto createDto = new RatingRequestDto(testVisitor1.getId(), testRestaurant1.getId(), 4, "Good!");
        RatingResponseDto createdRating = ratingService.save(createDto);

        Long visitorId = createdRating.getVisitorId();
        Long restaurantId = createdRating.getRestaurantId();

        mockMvc.perform(get("/api/reviews/{visitorId}/{restaurantId}", visitorId, restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitorId").value(visitorId))
                .andExpect(jsonPath("$.restaurantId").value(restaurantId))
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.reviewText").value("Good!"));
    }

    @Test
    void getRatingById_WhenRatingDoesNotExist_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/reviews/{visitorId}/{restaurantId}", 999L, 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllRatings_ShouldReturnPagedRatings() throws Exception {
        RatingRequestDto requestDto1 = new RatingRequestDto(testVisitor1.getId(), testRestaurant1.getId(), 5, "Great!");
        RatingRequestDto requestDto2 = new RatingRequestDto(testVisitor2.getId(), testRestaurant1.getId(), 4, "Good!");
        ratingService.save(requestDto1);
        ratingService.save(requestDto2);

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.page.totalElements").value(2));
    }

    @Test
    void getRatingsByRestaurantId_ShouldReturnPagedRatings() throws Exception {
        RatingRequestDto requestDto1 = new RatingRequestDto(testVisitor1.getId(), testRestaurant1.getId(), 5, "Great!");
        RatingRequestDto requestDto2 = new RatingRequestDto(testVisitor2.getId(), testRestaurant1.getId(), 4, "Good!");
        RatingRequestDto requestDto3 = new RatingRequestDto(testVisitor1.getId(), testRestaurant2.getId(), 3, "Average");
        ratingService.save(requestDto1);
        ratingService.save(requestDto2);
        ratingService.save(requestDto3);

        mockMvc.perform(get("/api/reviews/restaurant/{restaurantId}", testRestaurant1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.page.totalElements").value(2));

        mockMvc.perform(get("/api/reviews/restaurant/{restaurantId}", testRestaurant2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.page.totalElements").value(1));
    }

    @Test
    void deleteRating_ShouldReturnOk() throws Exception {
        RatingRequestDto createDto = new RatingRequestDto(testVisitor1.getId(), testRestaurant1.getId(), 3, "Average");
        RatingResponseDto createdRating = ratingService.save(createDto);
        Long visitorId = createdRating.getVisitorId();
        Long restaurantId = createdRating.getRestaurantId();

        mockMvc.perform(delete("/api/reviews/{visitorId}/{restaurantId}", visitorId, restaurantId))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/api/reviews/{visitorId}/{restaurantId}", visitorId, restaurantId))
                .andExpect(status().isNotFound());
    }
}