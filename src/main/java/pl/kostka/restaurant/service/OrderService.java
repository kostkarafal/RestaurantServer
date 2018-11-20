package pl.kostka.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostka.restaurant.exception.ProductIncompatibilityException;
import pl.kostka.restaurant.model.Order;
import pl.kostka.restaurant.model.Product;
import pl.kostka.restaurant.model.Restaurant;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.model.dto.Basket;
import pl.kostka.restaurant.model.enums.OrderStatus;
import pl.kostka.restaurant.repository.OrderRepository;
import pl.kostka.restaurant.repository.ProductRepository;
import pl.kostka.restaurant.repository.RestaurantRepository;
import pl.kostka.restaurant.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    private RestaurantRepository restaurantRepository;

    private ProductRepository productRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository, RestaurantRepository restaurantRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
    }


    public Order makeOrder(Basket basket, User user) {
        Float totalPrice = 0f;
        List<Product> products = new ArrayList<>();
        for(int i = 0; i < basket.getProducts().size(); i++){
            Optional optionalProduct = productRepository.findById(basket.getProducts().get(i).getId());
            if (optionalProduct.isPresent()) {
                for (int j = 0; j < basket.getProductsAmount().get(i); j++) {
                    Product product = (Product) optionalProduct.get();
                    products.add(product);
                    totalPrice += product.getPrice();
                }
            } else {
                throw new ProductIncompatibilityException("Products in order was incompatible with products in database");
            }
        }

        Order order = new Order();
        order.setProducts(products);
        order.setStatus(OrderStatus.CONFIRMED);
        if (totalPrice.equals(basket.getTotalPrize())) {
            order.setTotalPrice(totalPrice);
        } else {
            throw new ProductIncompatibilityException("Products in order was incompatible with products in database");
        }
        restaurantRepository.findById(basket.getRestaurantId()).ifPresent(order::setRestaurant);
        order.setUser(user);
        orderRepository.save(order);
        return order;
    }

}
