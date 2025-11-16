package com.practice.backend.entity;

import lombok.Data;

@Data
//Без NoArgs т.к jpa нет и оно не обязательно тогда
public class Visitor {
    //Грустно без JPA :(
    //Если бы другие библиотеки были разрешены сделал бы через constrains
    //Тут либо конструктор писать ручками со стрингом по умолчанию "Аноним" условным (как я и сделал в этом классе)
    //Либо делать через NonNull (в остальных классах так)
    //Не очень приятно без validation
    private Long id;
    private String name;
    private Integer age;
    private String gender;

    public Visitor(Long id, String name, Integer age, String gender) {
        this.id = id;
        this.name = name == null ? "Аноним" : name;
        this.age = age;
        this.gender = gender;
    }
}
