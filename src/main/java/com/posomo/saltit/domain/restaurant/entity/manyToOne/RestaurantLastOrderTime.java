package com.posomo.saltit.domain.restaurant.entity.manyToOne;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("LAST_ORDER")
public class RestaurantLastOrderTime extends RestaurantTime{
}
