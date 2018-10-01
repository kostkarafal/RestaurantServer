package pl.kostka.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.Order;
import pl.kostka.restaurant.repository.OrderRepository;
import pl.kostka.restaurant.repository.RestaurantRepository;
import pl.kostka.restaurant.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderController {

    private OrderRepository orderRepository;

    private RestaurantRepository restaurantRepository;

    private UserRepository userRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/restaurants/{restaurantId}/orders")
    public List<Order> getAllOrdersByRestaurantId(@PathVariable Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }

    @GetMapping("/users/{userId}/orders")
    public List<Order> getAllOrdersByUserId(@PathVariable Long userId) {
        return orderRepository.findByUserId(userId);
    }


    @PostMapping("users/{userId}/restaurants/{restaurantId}/orders")
    public Order createOrder(@PathVariable Long restaurantId,
                             @PathVariable Long userId,
                             @Valid @RequestBody Order order) {
        userRepository.findById(userId).map(user -> {
            order.setUser(user);
            return true;
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
        return restaurantRepository.findById(restaurantId).map(restaurant -> {
            order.setRestaurant(restaurant);
            return orderRepository.save(order);
        }).orElseThrow(() -> new ResourceNotFoundException("RestaurantId " + restaurantId + " not found"));
    }
}
