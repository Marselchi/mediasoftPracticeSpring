package com.practice.backend.controller;

import com.practice.backend.dto.request.VisitorRequestDto;
import com.practice.backend.dto.response.VisitorResponseDto;
import com.practice.backend.service.VisitorService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "Посетители", description = "Операции для управления посетителями")
public class VisitorController {
    private final VisitorService visitorService;

    @PostMapping
    @Operation(summary = "Создать нового посетителя", description = "Создает нового посетителя с предоставленными данными")
    @ApiResponse(responseCode = "201", description = "Посетитель успешно создан")
    public ResponseEntity<VisitorResponseDto> createVisitor(@Valid @RequestBody VisitorRequestDto requestDto) {
        VisitorResponseDto responseDto = visitorService.save(requestDto);
        //Поправляем ошибочки, надеюсь вы ничего не видели
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить посетителя по ID", description = "Возвращает посетителя по его идентификатору")
    @ApiResponse(responseCode = "200", description = "Посетитель найден")
    @ApiResponse(responseCode = "404", description = "Посетитель не найден")
    public ResponseEntity<VisitorResponseDto> getVisitorById(
            @Parameter(description = "ID посетителя для получения") @PathVariable Long id) {
        VisitorResponseDto responseDto = visitorService.findById(id);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Получить всех посетителей", description = "Возвращает всех посетителей")
    @ApiResponse(responseCode = "200", description = "Список посетителей")
    public ResponseEntity<List<VisitorResponseDto>> getAllVisitors() {
        List<VisitorResponseDto> responseDtos = visitorService.findAll();
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить посетителя", description = "Обновляет существующего посетителя")
    @ApiResponse(responseCode = "200", description = "Посетитель успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Посетитель не найден")
    public ResponseEntity<VisitorResponseDto> updateVisitor(
            @Parameter(description = "ID посетителя для обновления") @PathVariable Long id,
            @Valid @RequestBody VisitorRequestDto requestDto) {
        return ResponseEntity.ok(visitorService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить посетителя", description = "Удаляет посетителя по его ID")
    @ApiResponse(responseCode = "204", description = "Посетитель успешно удален")
    @ApiResponse(responseCode = "404", description = "Посетитель не найден")
    public ResponseEntity<Void> deleteVisitor(
            @Parameter(description = "ID посетителя для удаления") @PathVariable Long id) {
        visitorService.remove(id);
        return ResponseEntity.noContent().build();
    }
}