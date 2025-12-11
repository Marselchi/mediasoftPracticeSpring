package com.practice.backend.repository;

import com.practice.backend.entity.Visitor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    //Можно вообще ничего не писать если использовать только стандартные :)
}
