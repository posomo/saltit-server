package com.posomo.saltit.domain.restaurant.entity;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantMenu{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "restaurant_id")
    @ManyToOne
    private Restaurant restaurant;
    @Column(length = 20)
    private String name;
    private Integer price;
    private Integer orderNumber;
    @Column(length = 300)
    private String pictureUrl;
    @Builder
    public RestaurantMenu(Restaurant restaurant, String name, Integer price, Boolean isMainMenu,
                          String pictureUrl) {
        this.restaurant = restaurant;
        this.name = name;
        this.price = price;
        this.pictureUrl = pictureUrl;
    }
}
