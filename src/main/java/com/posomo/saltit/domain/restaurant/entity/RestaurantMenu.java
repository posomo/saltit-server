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
    @Column(length = 300)
    private String name;
    private Integer price;
    private Integer orderNumber;
    @Column(length = 300)
    private String pictureUrl;
    public static RestaurantMenu create(Long id, Restaurant restaurant, String name, Integer price,
                                        Integer orderNumber, String pictureUrl){
        return new RestaurantMenu(id,restaurant,name,price,orderNumber,pictureUrl);
    }
    protected RestaurantMenu(Long id, Restaurant restaurant, String name, Integer price,
                             Integer orderNumber, String pictureUrl){
        this.id=id;
        this.restaurant=restaurant;
        this.name=name;
        this.price=price;
        this.orderNumber=orderNumber;
        this.pictureUrl=pictureUrl;
    }
}
