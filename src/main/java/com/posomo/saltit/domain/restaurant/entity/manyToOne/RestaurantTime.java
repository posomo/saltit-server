package com.posomo.saltit.domain.restaurant.entity.manyToOne;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.enums.Day;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "restaurant_id")
    @ManyToOne
    private Restaurant restaurant;
    private LocalTime timeFrom;
    private LocalTime timeTo;
    @Enumerated(value = EnumType.STRING)
    private Day day;
    @Builder
    public RestaurantTime(UUID id, Restaurant restaurant, LocalTime timeFrom, LocalTime timeTo) {
        this.restaurant = restaurant;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }
}
