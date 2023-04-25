package com.posomo.saltit.domain.restaurant.entity;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantLocation{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    @Column(length = 200)
    private String roadAddress;
    private Point location;

    public static RestaurantLocation create(Long id, Restaurant restaurant, String roadAddress, Point location){
        return new RestaurantLocation(id,restaurant,roadAddress,location);
    }
    protected RestaurantLocation(Long id, Restaurant restaurant, String roadAddress,Point location){
        this.id=id;
        this.restaurant=restaurant;
        this.roadAddress=roadAddress;
        this.location=location;
    }
}
