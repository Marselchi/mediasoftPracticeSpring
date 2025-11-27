package com.practice.backend.entity;

import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visitor {
    private Long id;
    private String name;
    private int age;
    private String gender;
}
