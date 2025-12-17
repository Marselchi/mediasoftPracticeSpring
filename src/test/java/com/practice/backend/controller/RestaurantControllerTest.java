package com.practice.backend.controller;

import com.practice.backend.dto.request.RestaurantRequestDto;
import com.practice.backend.dto.response.RestaurantResponseDto;
import com.practice.backend.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @Test
    void createRestaurant_ShouldReturnCreatedRestaurant() throws Exception {
        RestaurantRequestDto requestDto = new RestaurantRequestDto("Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"));
        RestaurantResponseDto responseDto = new RestaurantResponseDto(1L, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);

        when(restaurantService.save(requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Bistro\",\"description\":\"Great food\",\"cuisineType\":\"ITALIAN\",\"averageCheck\":25.00}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Bistro"))
                .andExpect(jsonPath("$.description").value("Great food"))
                .andExpect(jsonPath("$.cuisineType").value("ITALIAN"))
                .andExpect(jsonPath("$.averageCheck").value(25.00));

        verify(restaurantService).save(any(RestaurantRequestDto.class));
    }

    @Test
    void getRestaurantById_WhenRestaurantExists_ShouldReturnRestaurant() throws Exception {
        RestaurantResponseDto responseDto = new RestaurantResponseDto(1L, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);

        when(restaurantService.findById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Bistro"))
                .andExpect(jsonPath("$.description").value("Great food"))
                .andExpect(jsonPath("$.cuisineType").value("ITALIAN"))
                .andExpect(jsonPath("$.averageCheck").value(25.00));

        verify(restaurantService).findById(1L);
    }

    @Test
    void getRestaurantById_WhenRestaurantDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(restaurantService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isNotFound());

        verify(restaurantService).findById(1L);
    }

    @Test
    void getAllRestaurants_ShouldReturnListOfRestaurants() throws Exception {
        RestaurantResponseDto responseDto1 = new RestaurantResponseDto(1L, "Bistro", "Great food", "ITALIAN", new BigDecimal("25.00"), BigDecimal.ZERO);
        RestaurantResponseDto responseDto2 = new RestaurantResponseDto(2L, "Sushi Bar", "Fresh sushi", "JAPANESE", new BigDecimal("40.00"), BigDecimal.ZERO);
        List<RestaurantResponseDto> restaurants = Arrays.asList(responseDto1, responseDto2);

        when(restaurantService.findAll()).thenReturn(restaurants);

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Bistro"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Sushi Bar"));

        verify(restaurantService).findAll();
    }

    @Test
    void updateRestaurant_ShouldReturnUpdatedRestaurant() throws Exception {
        RestaurantRequestDto requestDto = new RestaurantRequestDto("Bistro Updated", "Great food updated", "FRENCH", new BigDecimal("30.00"));
        RestaurantResponseDto responseDto = new RestaurantResponseDto(1L, "Bistro Updated", "Great food updated", "FRENCH", new BigDecimal("30.00"), BigDecimal.ZERO);

        when(restaurantService.update(eq(1L), eq(requestDto))).thenReturn(responseDto);

        mockMvc.perform(put("/api/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Bistro Updated\",\"description\":\"Great food updated\",\"cuisineType\":\"FRENCH\",\"averageCheck\":30.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Bistro Updated"))
                .andExpect(jsonPath("$.description").value("Great food updated"))
                .andExpect(jsonPath("$.cuisineType").value("FRENCH"))
                .andExpect(jsonPath("$.averageCheck").value(30.00));

        verify(restaurantService).update(eq(1L), eq(requestDto));
    }

    @Test
    void deleteRestaurant_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isNoContent());

        verify(restaurantService).remove(1L);
    }
}
