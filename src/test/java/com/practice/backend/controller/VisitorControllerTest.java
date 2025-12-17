package com.practice.backend.controller;

import com.practice.backend.dto.request.VisitorRequestDto;
import com.practice.backend.dto.response.VisitorResponseDto;
import com.practice.backend.service.VisitorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitorController.class)
public class VisitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VisitorService visitorService;

    @Test
    void createVisitor_ShouldReturnCreatedVisitor() throws Exception {
        VisitorRequestDto requestDto = new VisitorRequestDto("John", 30, "Male");
        VisitorResponseDto responseDto = new VisitorResponseDto(1L, "John", 30, "Male");

        when(visitorService.save(requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"age\":30,\"gender\":\"Male\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("Male"));

        verify(visitorService).save(requestDto);
    }

    @Test
    void getVisitorById_WhenVisitorExists_ShouldReturnVisitor() throws Exception {
        VisitorResponseDto responseDto = new VisitorResponseDto(1L, "John", 30, "Male");

        when(visitorService.findById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("Male"));

        verify(visitorService).findById(1L);
    }

    @Test
    void getVisitorById_WhenVisitorDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(visitorService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(visitorService).findById(1L);
    }

    @Test
    void getAllVisitors_ShouldReturnListOfVisitors() throws Exception {
        VisitorResponseDto responseDto1 = new VisitorResponseDto(1L, "John", 30, "Male");
        VisitorResponseDto responseDto2 = new VisitorResponseDto(2L, "Jane", 25, "Female");
        List<VisitorResponseDto> visitors = Arrays.asList(responseDto1, responseDto2);

        when(visitorService.findAll()).thenReturn(visitors);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jane"));

        verify(visitorService).findAll();
    }

    @Test
    void updateVisitor_ShouldReturnUpdatedVisitor() throws Exception {
        VisitorRequestDto requestDto = new VisitorRequestDto("John Updated", 31, "Male");
        VisitorResponseDto responseDto = new VisitorResponseDto(1L, "John Updated", 31, "Male");

        when(visitorService.update(eq(1L), eq(requestDto))).thenReturn(responseDto);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Updated\",\"age\":31,\"gender\":\"Male\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.age").value(31))
                .andExpect(jsonPath("$.gender").value("Male"));

        verify(visitorService).update(eq(1L), eq(requestDto));
    }

    @Test
    void deleteVisitor_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(visitorService).remove(1L);
    }
}
