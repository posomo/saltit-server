package com.posomo.saltit.domain.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Schema(description = "레스토랑 정보")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantSummary {

    @Schema(description = "가게 ID", example = "1")
    private Long restaurantId;
    @Schema(description = "가게 사진", defaultValue = "null")
    private String titleImageUrl;
    @Schema(description = "가게 이름", example = "육쌈냉면")
    private String restaurantName;
    @Schema(description = "맛집 점수", example = "85")
    private Integer rating;
    @Schema(description = "메인 메뉴 가격", example = "8500")
    private Integer mainMenuPrice;
    @Schema(description = "메인 메뉴 이름", example = "물냉면")
    private String mainMenuName;
    @Schema(description = "식당 카테고리 이름", example = "한식")
    private String categoryName;
    @Schema(description = "거리(단위 m)", example = "300")
    private Double distance;

    public static RestaurantSummary create(Long restaurantId, String titleImageUrl, String restaurantName, Integer rating, Integer mainMenuPrice,
                                           String mainMenuName, String categoryName,Double distance){
        return new RestaurantSummary(restaurantId, titleImageUrl,restaurantName,rating,mainMenuPrice,mainMenuName,categoryName,distance);
    }
}
