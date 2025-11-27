package com.practice.backend.controller;

import com.practice.backend.dto.request.RatingRequestDto;
import com.practice.backend.dto.response.RatingResponseDto;
import com.practice.backend.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Отзывы", description = "Операции для управления отзывами")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    @Operation(summary = "Создать новый отзыв", description = "Создает новый отзыв с предоставленными данными")
    @ApiResponse(responseCode = "201", description = "Отзыв успешно создан")
    public ResponseEntity<RatingResponseDto> createReview(@Valid @RequestBody RatingRequestDto requestDto) {
        RatingResponseDto responseDto = ratingService.save(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отзыв по ID", description = "Возвращает отзыв по его идентификатору")
    @ApiResponse(responseCode = "200", description = "Отзыв найден")
    @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    public ResponseEntity<RatingResponseDto> getReviewById(
            @Parameter(description = "ID отзыва для получения") @PathVariable Long id) {
        RatingResponseDto responseDto = ratingService.findById(id);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Получить все отзывы", description = "Возвращает все отзывы")
    @ApiResponse(responseCode = "200", description = "Список отзывов")
    public ResponseEntity<List<RatingResponseDto>> getAllReviews() {
        List<RatingResponseDto> responseDtos = ratingService.findAll();
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить отзыв", description = "Обновляет существующий отзыв")
    @ApiResponse(responseCode = "200", description = "Отзыв успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    public ResponseEntity<RatingResponseDto> updateReview(
            @Parameter(description = "ID отзыва для обновления") @PathVariable Long id,
            @Valid @RequestBody RatingRequestDto requestDto) {
        RatingResponseDto responseDto = ratingService.update(id, requestDto);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отзыв", description = "Удаляет отзыв по его ID")
    @ApiResponse(responseCode = "204", description = "Отзыв успешно удален")
    @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "ID отзыва для удаления") @PathVariable Long id) {
        boolean isDeleted = ratingService.remove(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}