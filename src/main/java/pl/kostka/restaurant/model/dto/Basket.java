package pl.kostka.restaurant.model.dto;

import pl.kostka.restaurant.model.Product;
import pl.kostka.restaurant.model.enums.OrderType;


import java.util.List;

public class Basket {

    private Long id;
    private Float totalPrize;
    private List<Integer> productsAmount;
    private List<Product> products;
    private Long restaurantId;
    private Long deliveryAddressId;
    private OrderType orderType;

    public Basket() {
    }

    public Basket(Float totalPrize, List<Integer> productsAmount, List<Product> products, Long restaurantId) {
        this.totalPrize = totalPrize;
        this.productsAmount = productsAmount;
        this.products = products;
        this.restaurantId = restaurantId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTotalPrize() {
        return totalPrize;
    }

    public void setTotalPrize(Float totalPrize) {
        this.totalPrize = totalPrize;
    }

    public List<Integer> getProductsAmount() {
        return productsAmount;
    }

    public void setProductsAmount(List<Integer> productsAmount) {
        this.productsAmount = productsAmount;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Long deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }
}
