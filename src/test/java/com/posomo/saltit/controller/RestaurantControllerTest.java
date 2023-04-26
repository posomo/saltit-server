package com.posomo.saltit.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.posomo.saltit.domain.exception.NoRecordException;
import com.posomo.saltit.domain.restaurant.dto.RestaurantDetailResponse;
import com.posomo.saltit.global.constant.ResponseMessage;
import com.posomo.saltit.service.RestaurantService;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	RestaurantService restaurantService;

	@BeforeEach
	void setRestaurantServiceStub() {
		List<RestaurantDetailResponse.Classification.Menu> mainMenus = new ArrayList<>();
		mainMenus.add(new RestaurantDetailResponse.Classification.Menu(1L, 2, "main1"));
		mainMenus.add(new RestaurantDetailResponse.Classification.Menu(2L, 2, "main2"));
		mainMenus.add(new RestaurantDetailResponse.Classification.Menu(3L, 2, "main3"));

		List<RestaurantDetailResponse.Classification.Menu> sideMenus = new ArrayList<>();
		mainMenus.add(new RestaurantDetailResponse.Classification.Menu(4L, 1, "side1"));
		mainMenus.add(new RestaurantDetailResponse.Classification.Menu(5L, 1, "side2"));

		RestaurantDetailResponse.Classification main = new RestaurantDetailResponse.Classification(3, mainMenus);
		RestaurantDetailResponse.Classification side = new RestaurantDetailResponse.Classification(2, sideMenus);
		RestaurantDetailResponse restaurantDetailResponse = new RestaurantDetailResponse(1L, "testUrl", 5, "test store", 100, main, side);
		when(restaurantService.getRestaurantDetail(1L)).thenReturn(restaurantDetailResponse);
		when(restaurantService.getRestaurantDetail(2L)).thenThrow(new NoRecordException(String.format("restaurantId = %d record not found", 2L)));
	}

	@Nested
	@DisplayName("식당 세부 정보 조회 api")
	class getRestaurantDetail {
		@Test
		@DisplayName("정상 흐름")
		void ok() throws Exception {
			mvc.perform(get("/api/v1/restaurant/detail/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalMenuCount").value(5))
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.rating").value(100))
				.andExpect(jsonPath("$.main.count").value(3))
				.andExpect(jsonPath("$.side.count").value(2));
		}

		@Test
		@DisplayName("없는 ID 조회")
		void edge1() throws Exception {
			String content = mvc.perform(get("/api/v1/restaurant/detail/2"))
				.andExpect(status().is4xxClientError())
				.andReturn().getResponse().getContentAsString();
			assertThat(content).isEqualTo(ResponseMessage.RECODE_NOT_FOUND);
		}

		@Test
		@DisplayName("ID 형식 오류(숫자가 아닌 경우)")
		void edge2() throws Exception {
			String content = mvc.perform(get("/api/v1/restaurant/detail/error"))
				.andExpect(status().is4xxClientError())
				.andReturn().getResponse().getContentAsString();
			assertThat(content).isEqualTo(ResponseMessage.MISMATCH_PARAM);
		}
	}

}
