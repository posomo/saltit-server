package com.posomo.saltit.controller;

import com.posomo.saltit.domain.region.dto.RegionListResponseDto;

import io.swagger.v3.oas.annotations.Operation;

public interface RegionControllerV1Swagger {
	@Operation(summary = "Saltit에서 제공할 인기지역과 한국에 존재하는 모든 지역을 보내줍니다.")
	RegionListResponseDto getRegionList();
}
