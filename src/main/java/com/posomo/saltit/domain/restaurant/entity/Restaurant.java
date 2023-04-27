package com.posomo.saltit.domain.restaurant.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Restaurant {

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
}
