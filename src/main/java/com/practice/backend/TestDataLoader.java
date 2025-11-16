package com.practice.backend;

import com.practice.backend.entity.CuisineType;
import com.practice.backend.entity.Rating;
import com.practice.backend.entity.Restaurant;
import com.practice.backend.entity.Visitor;
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
        Visitor visitor1 = new Visitor(null, "Маша", 25, "Женский");
        Visitor visitor2 = new Visitor(null, "Саша", 30, "Мужчина");
        Visitor visitor3 = new Visitor(null, null, 28, "Мужчина"); // Анонимный

        visitorService.save(visitor1);
        visitorService.save(visitor2);
        visitorService.save(visitor3);

        Restaurant restaurant1 = new Restaurant(null, "Итальянко", "Аутентичная итальянская кухня", CuisineType.ITALIAN, new BigDecimal("25.00"), BigDecimal.ZERO);
        Restaurant restaurant2 = new Restaurant(null, "Суши высушивай", "Прямиком из Японии", CuisineType.JAPANESE, new BigDecimal("40.00"), BigDecimal.ZERO);
        restaurantService.save(restaurant1);
        restaurantService.save(restaurant2);


        Rating rating1 = new Rating(visitor1.getId(), restaurant1.getId(), 5, "Все шикарноо!");
        Rating rating2 = new Rating(visitor2.getId(), restaurant1.getId(), 4, "Могло быть получше");
        Rating rating3 = new Rating(visitor3.getId(), restaurant2.getId(), 1, "Отвратительно");

        ratingService.save(rating1);
        ratingService.save(rating2);
        ratingService.save(rating3);

        System.out.println("Посетители:");
        visitorService.findAll().forEach(System.out::println);

        System.out.println("\nРестораны:");
        restaurantService.findAll().forEach(System.out::println);

        System.out.println("\nОценки:");
        ratingService.findAll().forEach(System.out::println);
    }
}