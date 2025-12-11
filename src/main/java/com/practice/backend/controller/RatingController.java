package com.practice.backend.controller;

import com.practice.backend.dto.request.RatingRequestDto;
import com.practice.backend.dto.response.RatingResponseDto;
import com.practice.backend.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@Tag(name = "Отзывы", description = "Операции для управления отзывами")
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    @Operation(summary = "Создать новый отзыв", description = "Создает новый отзыв с предоставленными данными")
    @ApiResponse(responseCode = "201", description = "Отзыв успешно создан")
    public ResponseEntity<RatingResponseDto> createReview(@Valid @RequestBody RatingRequestDto requestDto) {
        RatingResponseDto responseDto = ratingService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{visitorId}/{restaurantId}")
    @Operation(summary = "Получить отзыв по ID посетителя и ресторана", description = "Возвращает отзыв по идентификатору посетителя и ресторана")
    @ApiResponse(responseCode = "200", description = "Отзыв найден")
    @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    public ResponseEntity<RatingResponseDto> getReviewById(
            @Parameter(description = "ID посетителя") @PathVariable Long visitorId,
            @Parameter(description = "ID ресторана") @PathVariable Long restaurantId) {
        return ratingService.findById(visitorId, restaurantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Получить все отзывы", description = "Возвращает все отзывы с возможностью постраничного вывода")
    @ApiResponse(responseCode = "200", description = "Список отзывов")
    public ResponseEntity<Page<RatingResponseDto>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RatingResponseDto> responseDtos = ratingService.findAllWithPagination(pageable);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Получить отзывы по ID ресторана", description = "Возвращает все отзывы для указанного ресторана с пагинацией")
    @ApiResponse(responseCode = "200", description = "Список отзывов для ресторана")
    public ResponseEntity<Page<RatingResponseDto>> getReviewsByRestaurantId(
            @Parameter(description = "ID ресторана") @PathVariable Long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RatingResponseDto> responseDtos = ratingService.findRatingsByRestaurantId(restaurantId, pageable);
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{visitorId}/{restaurantId}")
    @Operation(summary = "Обновить отзыв", description = "Обновляет существующий отзыв")
    @ApiResponse(responseCode = "200", description = "Отзыв успешно обновлён")
    @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    //Только сейчас задумался, по-хорошему void возвращать и по новой стучаться гетом, но окич
    //С созданием то же самое, но в тз не указано значит ошибки нет)
    public ResponseEntity<RatingResponseDto> updateReview(
            @Parameter(description = "ID посетителя") @PathVariable Long visitorId,
            @Parameter(description = "ID ресторана") @PathVariable Long restaurantId,
            @Valid @RequestBody RatingRequestDto requestDto) {
        RatingRequestDto updatedRequest = new RatingRequestDto(
                visitorId,
                restaurantId,
                requestDto.getRating(),
                requestDto.getReviewText()
        );
        return ResponseEntity.ok(ratingService.save(updatedRequest));
    }

    @DeleteMapping("/{visitorId}/{restaurantId}")
    @Operation(summary = "Удалить отзыв", description = "Удаляет отзыв по ID посетителя и ресторана")
    @ApiResponse(responseCode = "204", description = "Отзыв успешно удалён")
    @ApiResponse(responseCode = "404", description = "Отзыв не найден")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "ID посетителя") @PathVariable Long visitorId,
            @Parameter(description = "ID ресторана") @PathVariable Long restaurantId) {
        ratingService.remove(visitorId, restaurantId);
        return ResponseEntity.noContent().build();
    }
}