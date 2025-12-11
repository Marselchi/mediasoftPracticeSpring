package com.practice.backend.controller;

import com.practice.backend.dto.request.RestaurantRequestDto;
import com.practice.backend.dto.response.RestaurantResponseDto;
import com.practice.backend.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
@Tag(name = "Рестораны", description = "Операции для управления ресторанами")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping
    @Operation(summary = "Создать новый ресторан", description = "Создает новый ресторан с предоставленными данными")
    @ApiResponse(responseCode = "201", description = "Ресторан успешно создан")
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@Valid @RequestBody RestaurantRequestDto requestDto) {
        RestaurantResponseDto responseDto = restaurantService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить ресторан по ID", description = "Возвращает ресторан по его идентификатору")
    @ApiResponse(responseCode = "200", description = "Ресторан найден")
    @ApiResponse(responseCode = "404", description = "Ресторан не найден")
    public ResponseEntity<RestaurantResponseDto> getRestaurantById(
            @Parameter(description = "ID ресторана для получения") @PathVariable Long id) {
        RestaurantResponseDto responseDto = restaurantService.findById(id);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Получить все рестораны", description = "Возвращает все рестораны")
    @ApiResponse(responseCode = "200", description = "Список ресторанов")
    public ResponseEntity<List<RestaurantResponseDto>> getAllRestaurants() {
        List<RestaurantResponseDto> responseDtos = restaurantService.findAll();
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить ресторан", description = "Обновляет существующий ресторан")
    @ApiResponse(responseCode = "200", description = "Ресторан успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Ресторан не найден")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(
            @Parameter(description = "ID ресторана для обновления") @PathVariable Long id,
            @Valid @RequestBody RestaurantRequestDto requestDto) {
        return ResponseEntity.ok(restaurantService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить ресторан", description = "Удаляет ресторан по его ID")
    @ApiResponse(responseCode = "204", description = "Ресторан успешно удален")
    @ApiResponse(responseCode = "404", description = "Ресторан не найден")
    public ResponseEntity<Void> deleteRestaurant(
            @Parameter(description = "ID ресторана для удаления") @PathVariable Long id) {
        restaurantService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-rating")
    @Operation(summary = "Найти рестораны с рейтингом выше порога", description = "Возвращает рестораны с рейтингом пользователя, большим или равным указанному значению")
    @ApiResponse(responseCode = "200", description = "Список ресторанов, соответствующих критерию")
    public ResponseEntity<List<RestaurantResponseDto>> getRestaurantsByMinRating(
            @RequestParam("minRating") BigDecimal minRating) {
        List<RestaurantResponseDto> responseDtos = restaurantService.findRestaurantsWithRatingAbove(minRating);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/by-rating-jpql")
    @Operation(summary = "Найти рестораны с рейтингом выше порога (QUERY)", description = "Возвращает рестораны с рейтингом пользователя, большим или равным указанному значению, используя JPQL-запрос")
    @ApiResponse(responseCode = "200", description = "Список ресторанов, соответствующих критерию")
    public ResponseEntity<List<RestaurantResponseDto>> getRestaurantsByMinRatingJPQL(
            @RequestParam("minRating") BigDecimal minRating) {
        List<RestaurantResponseDto> responseDtos = restaurantService.findRestaurantsWithRatingAboveJPQL(minRating);
        return ResponseEntity.ok(responseDtos);
    }
}