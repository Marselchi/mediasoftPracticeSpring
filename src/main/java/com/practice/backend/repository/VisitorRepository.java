package com.practice.backend.repository;

import com.practice.backend.entity.Visitor;
import org.springframework.stereotype.Repository;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Repository
public class VisitorRepository {
    private final List<Visitor> visitors = new ArrayList<>();
    //Это все что пришло в голову +- адекватное без генератора из jpa
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Visitor save(Visitor visitor) {
        if (visitor.getId() == null) {
            visitor.setId(idGenerator.getAndIncrement());
        }
        visitors.add(visitor);
        return visitor;
    }

    public boolean remove(Long id) {
        return visitors.removeIf(visit -> visit.getId().equals(id));
    }

    public List<Visitor> findAll() {
        return new ArrayList<>(visitors);
    }

    public Visitor findById(Long id) {
        return visitors.stream()
                .filter(visit -> visit.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
