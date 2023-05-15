package com.posomo.saltit.domain.restaurant.dto;

import org.springframework.data.domain.Pageable;

import lombok.Data;

@Data
public class RestaurantSearchCondition {

	private Double maxDistance;

	private Integer maxPrice;

	private Pageable pageable;

	private String mySqlPoint;

	private String search;

	private String sort;

	private String foodTypeName;

	public RestaurantSearchCondition(RestaurantFilterRequest filterRequest) {
		this.maxDistance = filterRequest.getMaxDistance();
		this.maxPrice = filterRequest.getMaxPrice();
		this.pageable = filterRequest.createPageRequest();
		this.mySqlPoint = filterRequest.computeMySqlPoint();
		this.search = filterRequest.getOptions().getSearch();
		this.sort = filterRequest.getOptions().getSort();
		this.foodTypeName = filterRequest.getOptions().getFoodTypeName();
	}

}
