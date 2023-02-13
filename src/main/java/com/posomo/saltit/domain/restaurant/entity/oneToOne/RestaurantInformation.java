package com.posomo.saltit.domain.restaurant.entity.oneToOne;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantInformation extends BaseEntity {
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
