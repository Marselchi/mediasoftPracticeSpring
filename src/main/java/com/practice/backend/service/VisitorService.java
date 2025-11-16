package com.practice.backend.service;

import com.practice.backend.entity.Visitor;
import com.practice.backend.repository.VisitorRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;

    public Visitor save(Visitor visitor) {
        return visitorRepository.save(visitor);
    }

    public void remove(Long id) {
        visitorRepository.remove(id);
    }

    public List<Visitor> findAll() {
        return visitorRepository.findAll();
    }
}
