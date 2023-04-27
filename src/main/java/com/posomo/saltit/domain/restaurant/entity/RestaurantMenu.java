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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RestaurantMenu {
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
}
