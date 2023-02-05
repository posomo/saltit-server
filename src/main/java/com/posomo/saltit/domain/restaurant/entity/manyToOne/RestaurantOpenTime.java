package com.posomo.saltit.domain.restaurant.entity.manyToOne;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OPEN")
public class RestaurantOpenTime extends RestaurantTime{
}
