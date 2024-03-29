package com.posomo.saltit.domain.restaurant.dto;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "홈 화면 레스토랑 필터 요청 정보 (/api/v1/home/restaurant-summary)")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RestaurantFilterRequest {

	@Schema(description = "최대 검색 거리(m)", example = "1000", defaultValue = "10000")
	@PositiveOrZero
	@NotNull
	private Double maxDistance;

	@Schema(description = "가격 상한선", example = "9500", defaultValue = "40000")
	@Positive
	@NotNull
	private Integer maxPrice;

	@Schema(description = "요청 페이지 번호 (0부터 시작)", example = "0")
	@PositiveOrZero
	@NotNull
	private Integer page;

	@Schema(description = "요청 페이지 크기", example = "3")
	@NotNull
	@Positive
	private Integer size;

	@Schema(description = "사용자 경도(최소 소수점 8자리까지) -180~180", example = "127.0521000000")
	@NotNull
	private Double userLongitude;

	@Schema(description = "사용자 위도(최소 소수점 8자리까지) -90~90", example = "37.5033000000")
	@NotNull
	private Double userLatitude;

	@Valid
	@Nullable
	private Options options = new Options();

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor
	@Getter
	public static class Options {
		@Schema(description = "검색 문자 (사용 시 1글자~20글자 사이, 사용하지 않으면 json body에 적지 않는다)", example = "국밥")
		@Nullable
		@Length(min = 1, max = 20)
		private String search;

		@Schema(description = "별점순, 거리순, 리뷰순. default 별점순", example = "거리순")
		@Nullable
		private String sort;

		@Schema(description = "식당 카테고리 이름", example = "한식", defaultValue = "null")
		private String foodTypeName;
	}

	public int getMaxPrice() {
		return (maxPrice == null) ? 40000 : maxPrice;
	}

	public double getMaxDistance() {
		return ((maxDistance == null) ? 1000D : maxDistance)/1000;
	}
	public String computeMySqlPoint() {
		return "POINT(" + userLatitude + " " + userLongitude + ")";
	}

	public Pageable createPageRequest() {
		return PageRequest.of(page, size);
	}

	public RestaurantFilterRequest(String foodTypeName, Double maxDistance, Integer maxPrice, Integer page,
		Integer size,
		Double userLongitude, Double userLatitude) {
		this.options.foodTypeName = foodTypeName;
		this.maxDistance = maxDistance;
		this.maxPrice = maxPrice;
		this.page = page;
		this.size = size;
		this.userLongitude = userLongitude;
		this.userLatitude = userLatitude;
	}
}
