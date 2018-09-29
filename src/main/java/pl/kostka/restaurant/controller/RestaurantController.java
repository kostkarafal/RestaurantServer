package pl.kostka.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.Restaurant;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.repository.RestaurantRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RestaurantController {

    private RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getRestaurant() {
        return restaurantRepository.findAll();
    }

    @PostMapping("/restaurants")
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @PutMapping("/restaurants/{restaurantId}")
    public Restaurant updateRestaurant(@PathVariable Long restaurantId, @Valid @RequestBody Restaurant restaurantRequest) {
        return restaurantRepository.findById(restaurantId).map(restaurant -> {
            restaurant.setOpenHours(restaurantRequest.getOpenHours());
            restaurant.setDescription(restaurantRequest.getDescription());
            return restaurantRepository.save(restaurant);
        }).orElseThrow(() -> new ResourceNotFoundException("RestaurantId "+ restaurantId + " not found"));
    }

    @DeleteMapping("/restaurants/{restaurantId}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long restaurantId) {
        return restaurantRepository.findById(restaurantId).map(restaurant -> {
            restaurantRepository.delete(restaurant);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("RestaurantId "+ restaurantId + " not found"));
    }

}
