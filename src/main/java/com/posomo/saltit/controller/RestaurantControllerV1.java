package com.posomo.saltit.controller;
import com.posomo.saltit.domain.exception.ErrorResponse;
import com.posomo.saltit.domain.exception.InvalidArgumentException;
import com.posomo.saltit.domain.restaurant.dto.RestaurantDetailResponse;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummaryList;
import com.posomo.saltit.service.RestaurantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Log4j2
public class RestaurantControllerV1 implements RestaurantControllerV1Swagger, RestaurantController {
    private final RestaurantService restaurantService;

    public ResponseEntity<Object> getRestaurantSummaries(@RequestBody RestaurantFilterRequest filterRequest) {
        try {
            checkInvalidArgument(filterRequest);
        } catch (RuntimeException ex) {
            return new ResponseEntity(new ErrorResponse("Invalid Argument(user Latitude,user Longitude cannot be null)",
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
        }
        log.info("Filter Request {}", filterRequest);
        Slice<RestaurantSummary> restaurantSummaries = restaurantService.getRestaurantSummaries(filterRequest);
        Integer pageSize = restaurantSummaries.getPageable().getPageSize();
        Integer pageNumber = restaurantSummaries.getPageable().getPageNumber();
        return new ResponseEntity(RestaurantSummaryList.create(restaurantSummaries.stream().toList(), restaurantSummaries.hasNext(),
            pageNumber, pageSize), HttpStatus.OK);
    }

    public RestaurantDetailResponse getRestaurantDetail(@PathVariable("restaurantId") Integer restaurantId) {
        return restaurantService.getRestaurantDetail(restaurantId);
    }

    private void checkInvalidArgument(RestaurantFilterRequest filterRequest) {
        if (isInvalidParameters(filterRequest)) {
            throw new InvalidArgumentException();
        }
    }

    private boolean isInvalidParameters(RestaurantFilterRequest filterRequest) {
        if (filterRequest.getUserLatitude() == null || filterRequest.getUserLongitude() == null)
            return true;
        if (filterRequest.getPage() == null || filterRequest.getSize() == null)
            return true;
        return false;
    }
}
