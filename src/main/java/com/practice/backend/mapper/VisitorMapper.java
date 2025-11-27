package com.practice.backend.mapper;

import com.practice.backend.dto.request.VisitorRequestDto;
import com.practice.backend.dto.response.VisitorResponseDto;
import com.practice.backend.entity.Visitor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitorMapper {
    @Mapping(target = "id", ignore = true)
    Visitor toEntity(VisitorRequestDto requestDto);

    VisitorResponseDto toResponseDto(Visitor entity);
}