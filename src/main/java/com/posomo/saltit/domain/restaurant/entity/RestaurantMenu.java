package com.posomo.saltit.domain.restaurant.entity;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class RestaurantMenu{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "restaurant_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;
    @Column(length = 300)
    private String name;
    private Integer price;
    private Integer orderNumber;
    @Column(length = 300)
    private String pictureUrl;
    private boolean mainMenu;
    public static RestaurantMenu create(Long id, Restaurant restaurant, String name, Integer price,
                                        Integer orderNumber, String pictureUrl, boolean mainMenu){
        return new RestaurantMenu(id,restaurant,name,price,orderNumber,pictureUrl, mainMenu);
    }

    public RestaurantMenu(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
