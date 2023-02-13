package com.posomo.saltit.domain.restaurant.entity.core.enums;

import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTime;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTimeDay;
import java.util.List;
import java.util.UUID;

public enum Day {
    MON,TUE,WED,THU,FRI,SAT,SUN;

    static public List<RestaurantTimeDay> toRestaurantTimeDayList(RestaurantTime time, List<Day> days){
        return days.stream()
                .map(day ->
                        RestaurantTimeDay
                                .builder()
                                .id(UUID.randomUUID())
                                .time(time)
                                .day(day)
                                .build()
                ).toList();
    }
}
