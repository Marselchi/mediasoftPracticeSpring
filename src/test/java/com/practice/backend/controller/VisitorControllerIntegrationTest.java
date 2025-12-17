package com.practice.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.backend.dto.request.VisitorRequestDto;
import com.practice.backend.dto.response.VisitorResponseDto;
import com.practice.backend.service.VisitorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VisitorControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VisitorService visitorService;

    @Test
    void createVisitor_ShouldReturnCreatedVisitor() throws Exception {
        VisitorRequestDto requestDto = new VisitorRequestDto("John", 30, "Male");
        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.id").isNumber());

    }

    @Test
    void getVisitorById_WhenVisitorExists_ShouldReturnVisitor() throws Exception {
        VisitorRequestDto requestDto = new VisitorRequestDto("Jane", 25, "Female");
        VisitorResponseDto createdVisitor = visitorService.save(requestDto);
        Long id = createdVisitor.getId();

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Jane"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.gender").value("Female"));
    }

    @Test
    void getVisitorById_WhenVisitorDoesNotExist_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllVisitors_ShouldReturnListOfVisitors() throws Exception {
        VisitorRequestDto requestDto1 = new VisitorRequestDto("John", 30, "Male");
        VisitorRequestDto requestDto2 = new VisitorRequestDto("Jane", 25, "Female");
        visitorService.save(requestDto1);
        visitorService.save(requestDto2);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void deleteVisitor_ShouldReturnOk() throws Exception {
        VisitorRequestDto requestDto = new VisitorRequestDto("Bob", 35, "Other");
        VisitorResponseDto createdVisitor = visitorService.save(requestDto);
        Long id = createdVisitor.getId();

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().is2xxSuccessful());

    }
}