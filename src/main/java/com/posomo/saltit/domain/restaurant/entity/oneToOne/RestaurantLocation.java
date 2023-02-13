package com.posomo.saltit.domain.restaurant.entity.oneToOne;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.core.entity.BaseEntity;
import jakarta.persistence.Column;
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
public class RestaurantLocation extends BaseEntity {
    @OneToOne
    @MapsId
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(length = 200)
    private String roadAddress;
    @Column()
    private BigDecimal latitude;



    @Column()
    private BigDecimal longitude;

    @Builder
    public RestaurantLocation(Restaurant restaurant, String roadAddress, BigDecimal latitude, BigDecimal longitude) {
        this.restaurant = restaurant;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
