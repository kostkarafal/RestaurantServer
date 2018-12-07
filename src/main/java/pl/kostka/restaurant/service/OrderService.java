package pl.kostka.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostka.restaurant.exception.ProductIncompatibilityException;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.*;
import pl.kostka.restaurant.model.dto.Basket;
import pl.kostka.restaurant.model.dto.ProductAmount;
import pl.kostka.restaurant.model.enums.OrderStatus;
import pl.kostka.restaurant.model.enums.OrderType;
import pl.kostka.restaurant.repository.AddressRepository;
import pl.kostka.restaurant.repository.OrderRepository;
import pl.kostka.restaurant.repository.ProductRepository;
import pl.kostka.restaurant.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    private RestaurantRepository restaurantRepository;

    private ProductRepository productRepository;

    private AddressRepository addressRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository, RestaurantRepository restaurantRepository, ProductRepository productRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
    }


    public Order makeOrder(Basket basket, User user) {
        Float totalPrice = 0f;
        List<Product> products = new ArrayList<>();

        for(ProductAmount map : basket.getProducts()){
            Product dbProduct = productRepository.findById(map.getProduct().getId()).orElseThrow(()-> new ResourceNotFoundException("Cannot find products passed in basket"));

            if(dbProduct.getPrice().equals(map.getProduct().getPrice())) {
                for(int i = 0; i < map.getAmount(); i++){
                    products.add(dbProduct);
                    totalPrice += dbProduct.getPrice();
                }
            } else {
                throw new ProductIncompatibilityException("Products in order was incompatible with products in database");
            }
        }



        /*for(int i = 0; i < basket.getProducts().size(); i++){
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
        }*/

        Order order = new Order();
        order.setProducts(products);
        order.setStatus(OrderStatus.CONFIRMED);
        if (totalPrice.equals(basket.getTotalPrize())) {
            order.setTotalPrice(totalPrice);
        } else {
            throw new ProductIncompatibilityException("Products in order was incompatible with products in database");
        }
        if(basket.getOrderType() == OrderType.SELF_PICKUP) {
            restaurantRepository.findById(basket.getRestaurantId()).ifPresent(order::setRestaurant);
            order.setOrderType(basket.getOrderType());
        } else if (basket.getOrderType() == OrderType.DELIVERY) {
            order.setOrderType(basket.getOrderType());
            Address deliveryAddres = addressRepository.findById(basket.getDeliveryAddressId()).orElseThrow(()-> new ResourceNotFoundException("Address not found"));
            order.setDeliveryAddress(deliveryAddres);
            order.setRestaurant(findNearestRestaurant(deliveryAddres.getLatitude(), deliveryAddres.getLongitude()));
        }

        order.setUser(user);
        orderRepository.save(order);
        return order;
    }

    private Restaurant findNearestRestaurant(Double latitude, Double longitude) {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        Restaurant result = null;

        Double minDistance = 15D;
        for(Restaurant restaurant: restaurants) {
            Double distance = getDistanceBetweenToPointsInKm(restaurant.getLatitude(),restaurant.getLongitude(), latitude, longitude);
            if(distance < minDistance){
                result = restaurant;
                minDistance = distance;
            }
        }
        if(result == null){
            throw new ResourceNotFoundException("Cannot find restaurant in client area");
        } else {
            return result;

        }
    }

    private double getDistanceBetweenToPointsInKm(Double lat1, Double lng1, Double lat2, Double lng2) {
        //Haversine formula
        double R = 6371D;  //Earth radious
        double hav = (1 - Math.cos(DegreeToRadians(lat2 - lat1))) / 2 + Math.cos(DegreeToRadians(lat1)) * Math.cos(DegreeToRadians(lat2)) *
                ((1 - Math.cos(DegreeToRadians(lng2 - lng1))) / 2);

        return 2 * R * Math.asin(Math.sqrt(hav));
    }

    private Double DegreeToRadians(Double degree){
        return degree * (Math.PI/180);
    }

}
