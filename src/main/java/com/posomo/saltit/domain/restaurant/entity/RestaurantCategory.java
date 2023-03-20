package com.posomo.saltit.domain.restaurant.entity;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    public static RestaurantCategory create(Long id, Restaurant restaurant, Category category){
        return new RestaurantCategory(id,restaurant,category);
    }
    protected RestaurantCategory(Long id, Restaurant restaurant, Category category){
        this.id=id;
        this.restaurant=restaurant;
        this.category=category;
    }
}
