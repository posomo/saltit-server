package com.posomo.saltit.domain.restaurant.entity.manyToOne;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class RestaurantCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;
}
