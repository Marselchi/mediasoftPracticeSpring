package com.practice.backend.repository;

import com.practice.backend.entity.Visitor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
public class VisitorRepositoryIntegrationTest {

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
    private VisitorRepository visitorRepository;

    @Test
    void shouldSaveAndFindVisitor() {
        Visitor visitor = new Visitor(null, "John", 30, "Male");
        Visitor saved = visitorRepository.save(visitor);

        assertNotNull(saved.getId());
        assertEquals("John", saved.getName());
        assertEquals(30, saved.getAge());
        assertEquals("Male", saved.getGender());

        Optional<Visitor> foundOptional = visitorRepository.findById(saved.getId());
        assertTrue(foundOptional.isPresent());
        Visitor found = foundOptional.get();
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void shouldFindAllVisitors() {
        Visitor visitor1 = new Visitor(null, "John", 30, "Male");
        Visitor visitor2 = new Visitor(null, "Jane", 25, "Female");

        visitorRepository.save(visitor1);
        visitorRepository.save(visitor2);

        List<Visitor> visitors = visitorRepository.findAll();
        assertEquals(2, visitors.size());
    }

    @Test
    void shouldDeleteVisitor() {
        Visitor visitor = new Visitor(null, "John", 30, "Male");
        Visitor saved = visitorRepository.save(visitor);

        visitorRepository.deleteById(saved.getId());
        List<Visitor> visitors = visitorRepository.findAll();
        assertEquals(0, visitors.size());
    }
}
