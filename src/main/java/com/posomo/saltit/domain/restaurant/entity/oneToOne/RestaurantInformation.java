package com.posomo.saltit.domain.restaurant.entity.oneToOne;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantInformation{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    private BigDecimal average;

    private BigDecimal rating;

    @Builder
    public RestaurantInformation(Restaurant restaurant, BigDecimal average, BigDecimal rating) {
        this.restaurant = restaurant;
        this.average = average;
        this.rating = rating;
    }
}
