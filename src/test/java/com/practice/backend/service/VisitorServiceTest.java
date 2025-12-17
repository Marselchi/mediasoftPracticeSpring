package com.practice.backend.service;

import com.practice.backend.dto.request.VisitorRequestDto;
import com.practice.backend.dto.response.VisitorResponseDto;
import com.practice.backend.entity.Visitor;
import com.practice.backend.mapper.VisitorMapper;
import com.practice.backend.repository.VisitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VisitorServiceTest {

    @Mock
    private VisitorRepository visitorRepository;

    @Mock
    private VisitorMapper visitorMapper;

    private VisitorService visitorService;

    @BeforeEach
    void setUp() {
        visitorService = new VisitorService(visitorRepository, visitorMapper);
    }

    @Test
    void save_ShouldReturnVisitorResponseDto() {
        VisitorRequestDto requestDto = new VisitorRequestDto("John", 30, "Male");
        Visitor entity = new Visitor(1L, "John", 30, "Male");
        VisitorResponseDto responseDto = new VisitorResponseDto(1L, "John", 30, "Male");

        when(visitorMapper.toEntity(any(VisitorRequestDto.class))).thenReturn(entity);
        when(visitorRepository.save(any(Visitor.class))).thenReturn(entity);
        when(visitorMapper.toResponseDto(any(Visitor.class))).thenReturn(responseDto);

        VisitorResponseDto result = visitorService.save(requestDto);

        assertEquals(responseDto, result);
        verify(visitorRepository).save(entity);
    }

    @Test
    void findById_WhenVisitorExists_ShouldReturnVisitorResponseDto() {
        Visitor entity = new Visitor(1L, "John", 30, "Male");
        VisitorResponseDto responseDto = new VisitorResponseDto(1L, "John", 30, "Male");

        when(visitorRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(visitorMapper.toResponseDto(any(Visitor.class))).thenReturn(responseDto);

        VisitorResponseDto result = visitorService.findById(1L);

        assertEquals(responseDto, result);
    }

    @Test
    void findById_WhenVisitorDoesNotExist_ShouldThrowException() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> visitorService.findById(1L));
    }

    @Test
    void findAll_ShouldReturnListOfVisitorResponseDto() {
        Visitor entity1 = new Visitor(1L, "John", 30, "Male");
        Visitor entity2 = new Visitor(2L, "Jane", 25, "Female");
        VisitorResponseDto responseDto1 = new VisitorResponseDto(1L, "John", 30, "Male");
        VisitorResponseDto responseDto2 = new VisitorResponseDto(2L, "Jane", 25, "Female");

        when(visitorRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(visitorMapper.toResponseDto(entity1)).thenReturn(responseDto1);
        when(visitorMapper.toResponseDto(entity2)).thenReturn(responseDto2);

        List<VisitorResponseDto> result = visitorService.findAll();

        assertEquals(2, result.size());
        assertEquals(responseDto1, result.get(0));
        assertEquals(responseDto2, result.get(1));
    }

    @Test
    void remove_ShouldCallRepositoryDelete() {
        visitorService.remove(1L);

        verify(visitorRepository).deleteById(1L);
    }
}
