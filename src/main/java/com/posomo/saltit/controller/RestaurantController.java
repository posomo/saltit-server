package com.posomo.saltit.controller;
import com.posomo.saltit.domain.exception.ErrorResponse;
import com.posomo.saltit.domain.exception.InvalidArgumentException;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummaryList;
import com.posomo.saltit.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Log4j2
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Operation(summary = "home 화면, 식당 요약 정보 제공용 api", description = "식당의 메인 이미지 주소, 페이지 정보," +
            "식당 요약 리스트를 제공합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = @Content(schema = @Schema(implementation = RestaurantSummaryList.class))),
            @ApiResponse(responseCode = "400", description = "유저 위도 경도 정보, 페이지 번호, 페이지 크기 중 하나라도 null일 때",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/home/restaurant-summary")
    public ResponseEntity<Object> getRestaurantSummaries(@RequestBody RestaurantFilterRequest filterRequest){
        try{
            checkInvalidArgument(filterRequest);
        }catch (RuntimeException ex){
            return new ResponseEntity(new ErrorResponse("Invalid Argument(user Latitude,user Longitude cannot be null)",
                    HttpStatus.BAD_REQUEST.value()),HttpStatus.BAD_REQUEST);
        }
        log.info("Filter Request {}",filterRequest);
        Slice<RestaurantSummary> restaurantSummaries = restaurantService.getRestaurantSummaries(filterRequest);
        Integer pageSize = restaurantSummaries.getPageable().getPageSize();
        Integer pageNumber = restaurantSummaries.getPageable().getPageNumber();
        return new ResponseEntity(RestaurantSummaryList.create(restaurantSummaries.stream().toList(),restaurantSummaries.hasNext(),
                pageNumber,pageSize),HttpStatus.OK);
    }
    private void checkInvalidArgument(RestaurantFilterRequest filterRequest){
        if(isInvalidParameters(filterRequest)){
            throw new InvalidArgumentException();
        }
    }
    private boolean isInvalidParameters(RestaurantFilterRequest filterRequest){
        if(filterRequest.getUserLatitude()==null || filterRequest.getUserLongitude()==null)
            return true;
        if(filterRequest.getPage()==null || filterRequest.getSize()==null)
            return true;
        return false;
    }
}
