package pl.kostka.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.Order;
import pl.kostka.restaurant.model.OrderProduct;
import pl.kostka.restaurant.repository.OrderProductRepository;
import pl.kostka.restaurant.repository.OrderRepository;
import pl.kostka.restaurant.repository.ProductRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderProductController {

    OrderRepository orderRepository;
    OrderProductRepository orderProductRepository;
    ProductRepository productRepository;

    @Autowired
    public OrderProductController(OrderRepository orderRepository, OrderProductRepository orderProductRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
    }
    @GetMapping("/orders/{orderId}/order-products")
    public List<OrderProduct> getAllOrderProductsByOrderId(@PathVariable Long orderId) {
        return orderProductRepository.findByOrderId(orderId);

    }

    @PostMapping("orders/{orderId}/order-products")
    public void addOrderProducts(@PathVariable Long orderId,
                                       @Valid @RequestBody List<OrderProduct> orderProducts) {

        orderProducts.forEach(orderProduct -> {
            orderRepository.findById(orderId).map(order -> {
                orderProduct.setOrder(order);
                return true;
            }).orElseThrow(() -> new ResourceNotFoundException("OrderId " + orderId + " not found"));
             productRepository.findById(orderProduct.getProduct().getId()).map(product -> {
                orderProduct.setProduct(product);
                return orderProductRepository.save(orderProduct);
            }).orElseThrow(() -> new ResourceNotFoundException("ProductId " + orderProduct.getProduct().getId() + " not found"));
        });

    }

}
