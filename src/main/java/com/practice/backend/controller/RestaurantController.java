package com.practice.backend.controller;

import com.practice.backend.dto.request.RestaurantRequestDto;
import com.practice.backend.dto.response.RestaurantResponseDto;
import com.practice.backend.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Рестораны", description = "Операции для управления ресторанами")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    @Operation(summary = "Создать новый ресторан", description = "Создает новый ресторан с предоставленными данными")
    @ApiResponse(responseCode = "201", description = "Ресторан успешно создан")
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@Valid @RequestBody RestaurantRequestDto requestDto) {
        RestaurantResponseDto responseDto = restaurantService.save(requestDto);
        return ResponseEntity.ok(responseDto);
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
        RestaurantResponseDto responseDto = restaurantService.update(id, requestDto);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить ресторан", description = "Удаляет ресторан по его ID")
    @ApiResponse(responseCode = "204", description = "Ресторан успешно удален")
    @ApiResponse(responseCode = "404", description = "Ресторан не найден")
    public ResponseEntity<Void> deleteRestaurant(
            @Parameter(description = "ID ресторана для удаления") @PathVariable Long id) {
        boolean isDeleted = restaurantService.remove(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}