package com.posomo.saltit;

import com.posomo.saltit.domain.restaurant.entity.FoodType;
import com.posomo.saltit.domain.restaurant.entity.Restaurant;
import com.posomo.saltit.domain.restaurant.entity.RestaurantLocation;
import com.posomo.saltit.domain.restaurant.entity.RestaurantMenu;
import com.posomo.saltit.respository.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void Init(){
        initService.createMockData();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        private final FoodTypeRepository foodTypeRepository;
        private final RestaurantRepository restaurantRepository;
        private final RestaurantMenuRepository restaurantMenuRepository;
        private final RestaurantLocationRepository restaurantLocationRepository;

        private final String[] MENU_NAMES_1 = {"순대국", "한우 1등급 소고기", "전주비빔밥", "육전", "꽃삼겹", "별미 곱창",
        "김치전","왕손만두","황생칼국수","치즈돈까스"};
        private final String[] MENU_NAMES_2 = {"까르보나라 떡볶이", "양송이 스프", "토마토 스파게티", "알리오 올리오",
                "빠네 파스타", "페퍼로니 피자",
                "파인애플 피자","아웃백 스테이크","브루클린 버거","베이컨치즈버거"};

    public void createMockData(){
            FoodType foodType1 = FoodType.create(null,"한식");
            FoodType foodType2 = FoodType.create(null,"양식");
            foodTypeRepository.save(foodType1);
            foodTypeRepository.save(foodType2);
            createMockKoreanRestaurant(foodType1);
            createMockRestaurant(foodType2);
        }
        public void createMockKoreanRestaurant(FoodType foodType){
            for(int i=0;i<1000;i++){
                Restaurant restaurant = Restaurant.create(null,null,"한식당"+i,null,
                        (int)(Math.random()*100),null,null,
                        null,null,null,foodType);
                RestaurantLocation location = RestaurantLocation.create(restaurant.getId(), restaurant,null,
                        BigDecimal.valueOf(37.50+(Math.random()/100)),
                        BigDecimal.valueOf(127.05+(Math.random()/100)));

                RestaurantMenu menu1 = RestaurantMenu.create(null,restaurant,getRandomMenu(1),
                        (int)(Math.random()*100+1)*150,1,null);
                RestaurantMenu menu2 = RestaurantMenu.create(null,restaurant,getRandomMenu(1),
                        (int)(Math.random()*100+1)*150,2,null);
                restaurantRepository.save(restaurant);
                restaurantLocationRepository.save(location);
                restaurantMenuRepository.save(menu1);
                restaurantMenuRepository.save(menu2);
            }
        }
        public void createMockRestaurant(FoodType foodType){
            for(int i=0;i<1000;i++){
                Restaurant restaurant = Restaurant.create(null,null,"양식당"+i,null,
                        (int)(Math.random()*100),null,null,
                        null,null,null,foodType);
                RestaurantLocation location = RestaurantLocation.create(restaurant.getId(), restaurant,null,
                        BigDecimal.valueOf(37.50+(Math.random()/100)),
                        BigDecimal.valueOf(127.05+(Math.random()/100)));

                RestaurantMenu menu1 = RestaurantMenu.create(null,restaurant,getRandomMenu(2),
                        (int)(Math.random()*100+1)*150,1,null);
                RestaurantMenu menu2 = RestaurantMenu.create(null,restaurant,getRandomMenu(2),
                        (int)(Math.random()*100+1)*150,2,null);
                restaurantRepository.save(restaurant);
                restaurantLocationRepository.save(location);
                restaurantMenuRepository.save(menu1);
                restaurantMenuRepository.save(menu2);
            }
        }
        public String getRandomMenu(Integer num){
            if (num==1)
                return MENU_NAMES_1[(int)(Math.random()*100)%10];
            else
                return MENU_NAMES_2[(int)(Math.random()*100)%10];
        }
    }
}
