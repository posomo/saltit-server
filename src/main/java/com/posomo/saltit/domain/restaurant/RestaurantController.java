package com.posomo.saltit.domain.restaurant;

import com.posomo.saltit.domain.restaurant.dto.RestaurantCreateManyDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    @Autowired
    private final RestaurantService restaurantService;

    @PostMapping("/list")
    public List<UUID> createMany(@RequestBody RestaurantCreateManyDto restaurantCreateManyDTO){
        return this.restaurantService.createMany(restaurantCreateManyDTO);
    }
}
