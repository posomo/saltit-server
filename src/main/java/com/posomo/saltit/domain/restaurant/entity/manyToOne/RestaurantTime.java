package com.posomo.saltit.domain.restaurant.entity.manyToOne;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.core.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantTime extends BaseEntity {

    @JoinColumn(name = "restaurant_id")
    @ManyToOne
    private Restaurant restaurant;
    private LocalTime timeFrom;

    public RestaurantTime(UUID id) {
        super(id);
    }

    private LocalTime timeTo;

    @OneToMany(mappedBy = "time")
    private List<RestaurantTimeDay> days = new ArrayList<>();

    public RestaurantTime(UUID id, Restaurant restaurant, LocalTime timeFrom, LocalTime timeTo, List<RestaurantTimeDay> days) {
        super(id);
        this.restaurant = restaurant;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.days = days;
    }
}
