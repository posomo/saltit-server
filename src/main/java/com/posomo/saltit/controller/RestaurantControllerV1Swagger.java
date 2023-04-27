package com.posomo.saltit.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.posomo.saltit.domain.exception.ErrorResponse;
import com.posomo.saltit.domain.restaurant.dto.RestaurantDetailResponse;
import com.posomo.saltit.domain.restaurant.dto.RestaurantFilterRequest;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummaryResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface RestaurantControllerV1Swagger {
	@Operation(summary = "home 화면, 식당 요약 정보 제공용 api", description = "식당의 메인 이미지 주소, 페이지 정보," +
		"식당 요약 리스트를 제공합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "정상 응답",
			content = @Content(schema = @Schema(implementation = RestaurantSummaryResponse.class))),
		@ApiResponse(responseCode = "400", description = "유저 위도 경도 정보, 페이지 번호, 페이지 크기 중 하나라도 null일 때",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	RestaurantSummaryResponse getRestaurantSummaries(@RequestBody RestaurantFilterRequest filterRequest);

	@Operation(summary = "식당 세부 정보 조회 api")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "정상 응답",
			content = @Content(schema = @Schema(implementation = RestaurantDetailResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	RestaurantDetailResponse getRestaurantDetail(@PathVariable("restaurantId") Integer restaurantId);
}
