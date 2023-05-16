-- 중요! 가격의 경우는 order by를 바깥에 명시하도록 한다.

select temp.id,
       title_image_url,
       temp.restaurant_name,
       score,
       price,
       count,
       name,
       food_type_name,
       latitude,
       longitude,
       distance
from restaurant_menu
         join (select r1_0.name                                           as restaurant_name,
                      r1_0.score,
                      r1_0.title_image_url,
                      f1_0.name                                           as food_type_name,
                      ST_X(l1_0.location)                                 AS latitude,
                      ST_Y(l1_0.location)                                 AS longitude,
                      r1_0.id,
                      st_distance_sphere(l1_0.location, st_pointfromtext('POINT(37.6033 127.0521)', 4326)) as distance,
                      min(10000 - m1_0.price) over (partition by r1_0.id) as price_difference,
                      count(r1_0.id) over (partition by r1_0.id) as count
               from restaurant r1_0
                   join restaurant_menu m1_0 on r1_0.id = m1_0.restaurant_id
                   join restaurant_location l1_0 on r1_0.id = l1_0.restaurant_id
                   join food_type f1_0 on f1_0.id = r1_0.food_type_id
               where m1_0.price <= 10000
                 and f1_0.name = '한식'
                 and main_menu = true
                 and st_within(l1_0.location, getdistancembr(st_pointfromtext('POINT(37.6033 127.0521)', 4326), 5))
                 and (r1_0.id in (select distinct restaurant_id
                   from restaurant_category
                   where category_id in (select id from category where name like '%돼지%' escape '!'))
                  or r1_0.id IN (SELECT DISTINCT restaurant_id
                   FROM restaurant_menu
                   WHERE name LIKE '%돼지%' ESCAPE '!'
                 and price <= 10000)
                  or r1_0.name like '%돼지%' escape '!')
) as temp
              on temp.id = restaurant_menu.restaurant_id and temp.price_difference = (10000 - restaurant_menu.price)
group by temp.id
    limit 0,1000;
