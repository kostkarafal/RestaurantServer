package pl.kostka.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostka.restaurant.model.Order;
import pl.kostka.restaurant.model.Product;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.model.dto.Basket;
import pl.kostka.restaurant.model.enums.OrderStatus;
import pl.kostka.restaurant.repository.OrderRepository;
import pl.kostka.restaurant.repository.ProductRepository;
import pl.kostka.restaurant.repository.RestaurantRepository;
import pl.kostka.restaurant.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    private RestaurantRepository restaurantRepository;

    private UserRepository userRepository;

    private ProductRepository productRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository, RestaurantRepository restaurantRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Basket getBasket(Order order) {
        Basket basket = new Basket();

        List<Product> products = new ArrayList<>();
        List<Integer> productsAmount = new ArrayList<>();

        order.getProducts().forEach(item -> {
            if(products.size() == 0 || !products.get(products.size() - 1).getId().equals(item.getId())) {
                products.add(item);
                productsAmount.add(1);
            } else {
                productsAmount.add(productsAmount.get(productsAmount.size() - 1) + 1);
                productsAmount.remove(productsAmount.size() - 2);
            }
        });
        basket.setId(order.getId());
        basket.setOrderStatus(order.getStatus());
        basket.setProducts(products);
        basket.setProductsAmount(productsAmount);
        basket.setTotalPrize(order.getTotalPrice());

        return basket;
    }

    public Order addToBasket(@NotNull User user, List<Long> productIds, Long restaurantId) {
        Order order = orderRepository.findUserBasket(user);
        List<Product> products = new ArrayList<>();

        productIds.forEach(item -> {
            Optional<Product> optional = productRepository.findById(item);
            optional.ifPresent(products::add);
        });

        List<Product> result;
        if(order != null) {
            result = order.getProducts();
        } else {
            order = new Order();
            result = new ArrayList<>();
            order.setUser(user);
            order.setStatus(OrderStatus.BASKET);
        }

        result.addAll(products);
        order.setProducts(result);
        order.setTotalPrice(calculateTotalPrice(result));
        restaurantRepository.findById(restaurantId).ifPresent(order::setRestaurant);

        return orderRepository.save(order);
    }

    private Float calculateTotalPrice(List<Product> products) {
        Float total = 0f;
        for(Product product: products) {
            total += product.getPrice();
        }
        return total;
    }
}
