package com.acme.gastronomie.controller;

import com.acme.gastronomie.entity.Restaurant;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import static com.acme.gastronomie.controller.Constants.VERSION_1;

@HttpExchange
interface RestaurantRepository {
    @GetExchange(url = "/{id}", version = VERSION_1)
    ResponseEntity<Restaurant> getById(@PathVariable String id);

    @GetExchange(version = VERSION_1)
    List<Restaurant> get(@RequestParam MultiValueMap<String, String> suchparameter);

    @PostExchange(version = VERSION_1)
    ResponseEntity<Void> post(@RequestBody RestaurantDTO restaurant);

    @PutExchange(url = "/{id}", version = VERSION_1)
    ResponseEntity<Void> put(@PathVariable String id, @RequestBody RestaurantDTO restaurant);

}
