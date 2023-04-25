package com.posomo.saltit.domain.restaurant.entity;
import com.posomo.saltit.domain.restaurant.dto.RestaurantSummary;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant{

    /**
     * id GenerationType 크롤링 코드 작성 후 수정 예정
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String rid;
    @Column(length = 50)
    private String name;
    @Column(length = 200)
    private String titleImageUrl;
    @Column
    private Integer score;
    @Column(length = 50)
    private String phone;
    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL) //LazyLoading 적용 안됨 - 연관관계의 주인이 아님
    private RestaurantLocation location;

    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL) //LazyLoading 적용 안됨 - 연관관계의 주인이 아님
    private RestaurantInformation information;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantMenu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantTime> times = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantCategory> categories = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FoodType foodType;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static Restaurant create(Long id, String rid, String name, String titleImageUrl, Integer score,
                                    RestaurantLocation location, RestaurantInformation information,
                                    List<RestaurantMenu> menus, List<RestaurantTime> times, List<RestaurantCategory> categories,
                                    FoodType foodType){
        return new Restaurant(id,rid,name,titleImageUrl,score,location,information,menus,times,categories,foodType);
    }
    protected Restaurant(Long id, String rid, String name, String titleImageUrl, Integer score,
                         RestaurantLocation location, RestaurantInformation information,
                         List<RestaurantMenu> menus, List<RestaurantTime> times, List<RestaurantCategory> categories,
                         FoodType foodType){
        this.id=id;
        this.rid=rid;
        this.name=name;
        this.titleImageUrl=titleImageUrl;
        this.score=score;
        this.location=location;
        this.information=information;
        this.menus=menus;
        this.times=times;
        this.categories=categories;
        this.foodType=foodType;
    }
}
