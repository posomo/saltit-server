package com.posomo.saltit.domain.restaurant.entity;

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
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    private BigDecimal average;

    private BigDecimal rating;

    public static RestaurantInformation create(Long id, BigDecimal average, BigDecimal rating){
        return new RestaurantInformation(id,average,rating);
    }
    protected RestaurantInformation(Long id, BigDecimal average, BigDecimal rating){
        this.id=id;
        this.average=average;
        this.rating=rating;
    }
}
