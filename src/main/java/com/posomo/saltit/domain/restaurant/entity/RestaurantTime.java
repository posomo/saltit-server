package com.posomo.saltit.domain.restaurant.entity;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.enums.Day;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "restaurant_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;
    private LocalTime timeFrom;
    private LocalTime timeTo;
    @Enumerated(value = EnumType.STRING)
    private Day day;
    public static RestaurantTime create(Long id, Restaurant restaurant, LocalTime timeFrom, LocalTime timeTo, Day day){
        return new RestaurantTime(id,restaurant,timeFrom,timeTo,day);
    }
    protected RestaurantTime(Long id, Restaurant restaurant, LocalTime timeFrom, LocalTime timeTo, Day day){
        this.id=id;
        this.restaurant=restaurant;
        this.timeFrom=timeFrom;
        this.timeTo=timeTo;
        this.day=day;
    }
}
