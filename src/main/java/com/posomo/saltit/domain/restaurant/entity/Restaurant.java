package com.posomo.saltit.domain.restaurant.entity;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantCategory;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantMenu;
import com.posomo.saltit.domain.restaurant.entity.manyToOne.RestaurantTime;
import com.posomo.saltit.domain.restaurant.entity.oneToOne.RestaurantInformation;
import com.posomo.saltit.domain.restaurant.entity.oneToOne.RestaurantLocation;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    @Column(length = 20)
    private String name;
    @Column(length = 100)
    private String titleImageUrl;

    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private RestaurantLocation location;

    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private RestaurantInformation information;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantMenu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantTime> times = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantCategory> categories = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    @Builder
    public Restaurant(@NonNull String rid, @NonNull String name, @NonNull String titleImageUrl, @NonNull RestaurantLocation location,
                      @NonNull RestaurantInformation information, List<RestaurantMenu> menus, List<RestaurantTime> times) {
        this.rid = rid;
        this.name = name;
        this.titleImageUrl = titleImageUrl;
        this.location = location;
        this.information = information;
        this.menus = menus;
        this.times = times;
    }
}
