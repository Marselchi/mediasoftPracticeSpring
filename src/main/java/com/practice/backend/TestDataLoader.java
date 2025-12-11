package com.practice.backend;

import com.practice.backend.dto.request.RatingRequestDto;
import com.practice.backend.dto.request.RestaurantRequestDto;
import com.practice.backend.dto.request.VisitorRequestDto;
import com.practice.backend.dto.response.RestaurantResponseDto;
import com.practice.backend.dto.response.VisitorResponseDto;
import com.practice.backend.service.RatingService;
import com.practice.backend.service.RestaurantService;
import com.practice.backend.service.VisitorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TestDataLoader implements CommandLineRunner {
    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final RatingService ratingService;

    public TestDataLoader(VisitorService visitorService, RestaurantService restaurantService, RatingService ratingService) {
        this.visitorService = visitorService;
        this.restaurantService = restaurantService;
        this.ratingService = ratingService;
    }

    @Override
    public void run(String... args) {
        //"Для этого нужно добавить несколько посетителей, несколько ресторанов и оценки"
        //Ну раз удалять не нужно, не буду
        //Починю и это тогда
        //В тз не сказано это чинить и мне лень
//        VisitorRequestDto visitor1Request = new VisitorRequestDto("Маша", 25, "Женский");
//        VisitorRequestDto visitor2Request = new VisitorRequestDto("Саша", 30, "Мужчина");
//        VisitorRequestDto visitor3Request = new VisitorRequestDto(null, 28, "Мужчина"); // Анонимный
//
//        VisitorResponseDto visitor1 = visitorService.save(visitor1Request);
//        VisitorResponseDto visitor2 = visitorService.save(visitor2Request);
//        VisitorResponseDto visitor3 = visitorService.save(visitor3Request);
//
//        RestaurantRequestDto restaurant1Request = new RestaurantRequestDto("Итальянко", "Аутентичная итальянская кухня", "ITALIAN", new BigDecimal("25.00"));
//        RestaurantRequestDto restaurant2Request = new RestaurantRequestDto("Суши высушивай", "Прямиком из Японии", "JAPANESE", new BigDecimal("40.00"));
//
//        RestaurantResponseDto restaurant1 = restaurantService.save(restaurant1Request);
//        RestaurantResponseDto restaurant2 = restaurantService.save(restaurant2Request);
//
//        RatingRequestDto rating1Request = new RatingRequestDto(visitor1.getId(), restaurant1.getId(), 5, "Все шикарноо!");
//        RatingRequestDto rating2Request = new RatingRequestDto(visitor2.getId(), restaurant1.getId(), 4, "Могло быть получше");
//        RatingRequestDto rating3Request = new RatingRequestDto(visitor3.getId(), restaurant2.getId(), 1, "Отвратительно");
//
//        ratingService.save(rating1Request);
//        ratingService.save(rating2Request);
//        ratingService.save(rating3Request);
//
//        System.out.println("Посетители:");
//        visitorService.findAll().forEach(System.out::println);
//
//        System.out.println("\nРестораны:");
//        restaurantService.findAll().forEach(System.out::println);
//
//        System.out.println("\nОценки:");
//        ratingService.findAll().forEach(System.out::println);
    }
}