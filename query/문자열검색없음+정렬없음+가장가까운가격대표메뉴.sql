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
                 and st_within(l1_0.location, getdistancembr(st_pointfromtext('POINT(37.6033 127.0521)', 4326), 0.7))
               order by distance

) as temp
              on temp.id = restaurant_menu.restaurant_id and temp.price_difference = (10000 - restaurant_menu.price)
group by temp.id
    limit 0,1000;
