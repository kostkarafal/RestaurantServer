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
    public List<Order> getAllOrdersByUserId(Principal principal) {
        User user =  userRepository.findByUsername(principal.getName());
        return orderRepository.findByUserId(user.getId());
    }


    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("orders/restaurants/{restaurantId}/basket")
    public Order addToBasket(@RequestBody List<Long> products,
                             @PathVariable Long restaurantId,
                             Principal principal) {
        User user =  userRepository.findByUsername(principal.getName());
        return orderService.addToBasket(user, products, restaurantId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("orders/basket")
    public Basket getBasket(Principal principal){
        User user =  userRepository.findByUsername(principal.getName());
        return orderService.getBasket(orderRepository.findUserBasket(user.getId()));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("orders/confirm")
    public Order confirmOrder(Principal principal) {
        User user =  userRepository.findByUsername(principal.getName());
        Order order = orderRepository.findUserBasket(user.getId());
        order.setStatus(OrderStatus.CONFIRMED);
        return order;
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("orders/restaurant/{restaurantId}/product/{productId}/change-amount")
    public Order changeProductAmount(@PathVariable Long productId,
                                      @PathVariable Long restaurantId,
                                      @RequestBody Long amount,
                                      Principal principal) {
        User user =  userRepository.findByUsername(principal.getName());

        return orderService.changeProductAmount(user,productId,amount,restaurantId);

    }

}
