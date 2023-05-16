package com.posomo.saltit.domain.restaurant.dto;

import java.util.List;

import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.RestaurantMenu;
import com.posomo.saltit.global.constant.Url;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "식당 상세 조회 페이지")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantDetailResponse {
	@Schema(description = "식당 ID")
	private Long id;

	@Schema(description = "다이닝 코드 URL")
	private String diningcodeUrl;

	@Schema(description = "총 메뉴 갯수")
	private Integer totalMenuCount;

	@Schema(description = "식당 이름")
	private String name;

	@Schema(description = "맛집 점수(0~100)")
	private Integer rating;

	@Schema(description = "전화번호")
	private String phone;

	@Schema(description = "주소")
	private String address;

	@Schema(description = "카테고리 목록")
	private List<String> categories;

	@Schema(description = "메인 메뉴")
	private Classification main;

	@Schema(description = "사이드 메뉴")
	private Classification side;
	public static RestaurantDetailResponse of(Restaurant restaurant) {
		Long id = restaurant.getId();

		String diningcodeUrl = Url.DINING_CODE_REVIEW_URL_PREFIX + restaurant.getRid();

		Integer totalMenuCount = restaurant.getMenus().size();

		String name = restaurant.getName();

		Integer rating = restaurant.getScore();

		String phone = restaurant.getPhone();

		String address = restaurant.getLocation().getRoadAddress();

		List<String> categories = restaurant.getCategories().stream().map(restaurantCategory -> restaurantCategory.getCategory().getName()).toList();

		List<RestaurantMenu> mainMenus = restaurant.getMenus().stream()
			.filter(RestaurantMenu::isMainMenu)
			.toList();
		Classification main = Classification.of(mainMenus);

		List<RestaurantMenu> sideMenus = restaurant.getMenus().stream()
			.filter(menu -> !menu.isMainMenu())
			.toList();
		Classification side = Classification.of(sideMenus);

		return new RestaurantDetailResponse(id, diningcodeUrl, totalMenuCount, name, rating, phone, address, categories, main, side);
	}

	@AllArgsConstructor
	@Data
	public static class Classification {
		@Schema(description = "메뉴 갯수")
		private int count;
		@Schema(description = "메뉴 리스트")
		private List<Menu> menus;

		private static Classification of(List<RestaurantMenu> menus) {
			return new Classification(menus.size(), menus.stream().map(Menu::of).toList());
		}

		@AllArgsConstructor
		@Data
		public static class Menu {
			@Schema(description = "매뉴 id")
			private Long id;
			@Schema(description = "매뉴 가격")
			private Integer price;
			@Schema(description = "매뉴 이름")
			private String name;

			public static Menu of(RestaurantMenu menu) {
				return new Menu(menu.getId(), menu.getPrice(), menu.getName());
			}
		}
	}
}
