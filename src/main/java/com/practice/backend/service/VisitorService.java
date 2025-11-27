package com.practice.backend.service;

import com.practice.backend.dto.request.VisitorRequestDto;
import com.practice.backend.dto.response.VisitorResponseDto;
import com.practice.backend.entity.Visitor;
import com.practice.backend.mapper.VisitorMapper;
import com.practice.backend.repository.VisitorRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final VisitorMapper visitorMapper;

    public VisitorResponseDto save(VisitorRequestDto requestDto) {
        Visitor visitor = visitorMapper.toEntity(requestDto);
        Visitor savedVisitor = visitorRepository.save(visitor);
        return visitorMapper.toResponseDto(savedVisitor);
    }

    public boolean remove(Long id) {
        return visitorRepository.remove(id);
    }

    public List<VisitorResponseDto> findAll() {
        return visitorRepository.findAll().stream()
                .map(visitorMapper::toResponseDto)
                .toList();
    }

    public VisitorResponseDto findById(Long id) {
        Visitor visitor = visitorRepository.findById(id);
        return visitor != null ? visitorMapper.toResponseDto(visitor) : null;
    }

    public VisitorResponseDto update(Long id, @Valid VisitorRequestDto requestDto) {
        Visitor existingVisitor = visitorRepository.findById(id);
        if (existingVisitor == null) {
            throw new RuntimeException("Пользователь не найден: " + id);
        }
        existingVisitor.setName(requestDto.getName());
        existingVisitor.setAge(requestDto.getAge());
        existingVisitor.setGender(requestDto.getGender());

        Visitor updatedVisitor = visitorRepository.save(existingVisitor);
        return visitorMapper.toResponseDto(updatedVisitor);
    }
}