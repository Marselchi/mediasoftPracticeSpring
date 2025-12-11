package com.practice.backend;
//Произвольно так произвольно, будет просто backend:)
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class BackendApplication {

    static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
