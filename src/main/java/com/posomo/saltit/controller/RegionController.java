package com.posomo.saltit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.posomo.saltit.domain.region.dto.RegionListResponseDto;

@RequestMapping("/api/v1/region")
public interface RegionController {
	@GetMapping("/list")
	RegionListResponseDto getRegionList();
}
