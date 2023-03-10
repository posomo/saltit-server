package com.posomo.saltit.domain.restaurant.dto;

import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Schema(description = "레스토랑 정보 리스보")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantSummaryList {
    @Schema(description = "레스토랑 정보", defaultValue = "null")
    public List<RestaurantSummary> restaurantSummaries;
    @Schema(description = "다음 페이지 존재 여부", example = "false",defaultValue = "null")
    public boolean hasNext;
    @Schema(description = "현재 페이지", example = "0", defaultValue = "null")
    public Integer page;
    @Schema(description = "한 페이지 크기", example = "3", defaultValue = "null")
    public Integer size;
    public static RestaurantSummaryList create(List<RestaurantSummary> restaurantSummaries,boolean hasNext, Integer page,
                                               Integer size){
        return new RestaurantSummaryList(restaurantSummaries,hasNext,page,size);
    }
}
