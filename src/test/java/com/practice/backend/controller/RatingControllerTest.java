package com.practice.backend.controller;

import com.practice.backend.dto.request.RatingRequestDto;
import com.practice.backend.dto.response.RatingResponseDto;
import com.practice.backend.service.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RatingService ratingService;

    @Test
    void createRating_ShouldReturnCreatedRating() throws Exception {
        RatingRequestDto requestDto = new RatingRequestDto(1L, 1L, 5, "Great!");
        RatingResponseDto responseDto = new RatingResponseDto(1L, 1L, 5, "Great!");

        when(ratingService.save(requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"visitorId\":1,\"restaurantId\":1,\"rating\":5,\"reviewText\":\"Great!\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.visitorId").value(1L))
                .andExpect(jsonPath("$.restaurantId").value(1L))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.reviewText").value("Great!"));

        verify(ratingService).save(requestDto);
    }

    @Test
    void getRatingById_WhenRatingExists_ShouldReturnRating() throws Exception {
        RatingResponseDto responseDto = new RatingResponseDto(1L, 1L, 5, "Great!");

        when(ratingService.findById(1L, 1L)).thenReturn(Optional.of(responseDto));

        mockMvc.perform(get("/api/reviews/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitorId").value(1L))
                .andExpect(jsonPath("$.restaurantId").value(1L))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.reviewText").value("Great!"));

        verify(ratingService).findById(1L, 1L);
    }

    @Test
    void getRatingById_WhenRatingDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(ratingService.findById(1L, 1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reviews/1/1"))
                .andExpect(status().isNotFound());

        verify(ratingService).findById(1L, 1L);
    }

    @Test
    void getAllRatings_ShouldReturnPagedRatings() throws Exception { // Изменили имя теста
        RatingResponseDto responseDto1 = new RatingResponseDto(1L, 1L, 5, "Great!");
        RatingResponseDto responseDto2 = new RatingResponseDto(2L, 1L, 4, "Good!");

        Page<RatingResponseDto> pagedResponse = new PageImpl<>(Arrays.asList(responseDto1, responseDto2));

        // Мокаем метод сервиса, который возвращает Page
        when(ratingService.findAllWithPagination(any(Pageable.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].visitorId").value(1L))
                .andExpect(jsonPath("$.content[0].rating").value(5))
                .andExpect(jsonPath("$.content[1].visitorId").value(2L))
                .andExpect(jsonPath("$.content[1].rating").value(4))
                .andExpect(jsonPath("$.page.totalElements").value(2))
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.number").value(0));

        verify(ratingService).findAllWithPagination(any(Pageable.class));
    }

    @Test
    void updateRating_ShouldReturnUpdatedRating() throws Exception {
        RatingRequestDto requestDto = new RatingRequestDto(1L, 1L, 4, "Updated review");
        RatingResponseDto responseDto = new RatingResponseDto(1L, 1L, 4, "Updated review");

        when(ratingService.save(requestDto)).thenReturn(responseDto);

        mockMvc.perform(put("/api/reviews/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"visitorId\":1,\"restaurantId\":1,\"rating\":4,\"reviewText\":\"Updated review\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitorId").value(1L))
                .andExpect(jsonPath("$.restaurantId").value(1L))
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.reviewText").value("Updated review"));

        verify(ratingService).save(requestDto);
    }

    @Test
    void deleteRating_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/reviews/1/1"))
                .andExpect(status().isNoContent());

        verify(ratingService).remove(1L, 1L);
    }
}
