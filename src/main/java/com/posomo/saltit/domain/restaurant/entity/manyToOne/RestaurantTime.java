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
import lombok.Getter;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class RestaurantTime extends BaseEntity {

    @JoinColumn(name = "restaurant_id")
    @ManyToOne
    private Restaurant restaurant;
    private LocalTime timeFrom;
    private LocalTime timeTo;

    @OneToMany(mappedBy = "time")
    private List<RestaurantTimeDay> days = new ArrayList<>();
}
