package com.posomo.saltit.domain.restaurant.entity.manyToOne;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.core.EmbeddableRange;
import com.posomo.saltit.domain.restaurant.entity.core.entity.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class RestaurantTime extends BaseEntity {

    @JoinColumn(name = "restaurant_id")
    @ManyToOne
    private Restaurant restaurant;
    @Embedded
    private EmbeddableRange<LocalTime> range;

    @Enumerated(value = EnumType.STRING)
    private RestaurantTimeType type;

    @OneToMany(mappedBy = "time")
    private List<RestaurantTimeDay> days = new ArrayList<>();
}
