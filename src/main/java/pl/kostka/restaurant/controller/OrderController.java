package pl.kostka.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.Order;
import pl.kostka.restaurant.model.Product;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.model.dto.Basket;
import pl.kostka.restaurant.model.enums.OrderStatus;
import pl.kostka.restaurant.repository.OrderRepository;
import pl.kostka.restaurant.repository.RestaurantRepository;
import pl.kostka.restaurant.repository.UserRepository;
import pl.kostka.restaurant.service.OrderService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class OrderController {

    private OrderRepository orderRepository;

    private UserRepository userRepository;

    private OrderService orderService;

    @Autowired
    public OrderController(OrderRepository orderRepository, UserRepository userRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @GetMapping("/restaurants/{restaurantId}/orders")
    public List<Order> getAllOrdersByRestaurantId(@PathVariable Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/orders")
    public List<Order> getActualOrdersByUserId(Principal principal) {
        return userRepository.findByUsername(principal.getName()).map(user ->
                orderRepository.findByUserIdAAndStatus(user.getId(), Arrays.asList(OrderStatus.CONFIRMED, OrderStatus.DELIVERY, OrderStatus.PROCESSING))
            ).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/orders/history")
    public List<Order> getOrdersHistoryByUserId(Principal principal) {
        return userRepository.findByUsername(principal.getName()).map(user ->
                orderRepository.findByUserIdAAndStatus(user.getId(), Arrays.asList(OrderStatus.CANCELED, OrderStatus.COMPLETED))
        ).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/orders/make-order")
    public Order makeOrder(@RequestBody Basket basket,
                             Principal principal) {
        return userRepository.findByUsername(principal.getName()).map(user ->
                orderService.makeOrder(basket, user)
        ).orElseThrow(()-> new ResourceNotFoundException("User not found"));

    }

}
