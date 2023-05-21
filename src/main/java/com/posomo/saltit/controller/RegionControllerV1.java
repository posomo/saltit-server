package com.posomo.saltit.controller;

import org.springframework.web.bind.annotation.RestController;

import com.posomo.saltit.domain.region.dto.RegionListResponseDto;
import com.posomo.saltit.service.RegionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@RestController
@Log4j2
public class RegionControllerV1 implements RegionController, RegionControllerV1Swagger {

	private final RegionService regionService;

	@Override
	public RegionListResponseDto getRegionList() {
		return regionService.getRegionList();
	}
}
