package com.practice.backend.service;

import com.practice.backend.dto.request.VisitorRequestDto;
import com.practice.backend.dto.response.VisitorResponseDto;
import com.practice.backend.repository.VisitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class VisitorServiceIntegrationTest {

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
    private VisitorService visitorService;


    @Autowired
    private VisitorRepository visitorRepository;

    @BeforeEach
    void setUp() {
        visitorRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindVisitor() {
        VisitorRequestDto requestDto = new VisitorRequestDto("John", 30, "Male");
        VisitorResponseDto saved = visitorService.save(requestDto);

        assertNotNull(saved.getId());
        assertEquals("John", saved.getName());
        assertEquals(30, saved.getAge());
        assertEquals("Male", saved.getGender());

        VisitorResponseDto found = visitorService.findById(saved.getId());
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void shouldFindAllVisitors() {
        VisitorRequestDto requestDto1 = new VisitorRequestDto("John", 30, "Male");
        VisitorRequestDto requestDto2 = new VisitorRequestDto("Jane", 25, "Female");

        visitorService.save(requestDto1);
        visitorService.save(requestDto2);

        List<VisitorResponseDto> visitors = visitorService.findAll();
        assertEquals(2, visitors.size());
    }

    @Test
    void shouldDeleteVisitor() {
        VisitorRequestDto requestDto = new VisitorRequestDto("John", 30, "Male");
        VisitorResponseDto saved = visitorService.save(requestDto);

        visitorService.remove(saved.getId());
        List<VisitorResponseDto> visitors = visitorService.findAll();
        assertEquals(0, visitors.size());
    }
}
