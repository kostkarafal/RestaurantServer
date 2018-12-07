package pl.kostka.restaurant.model.dto;

import pl.kostka.restaurant.model.Product;
import pl.kostka.restaurant.model.enums.OrderType;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Basket {

    private Long id;
    private Float totalPrize;
    private List<ProductAmount> products;
    private Long restaurantId;
    private Long deliveryAddressId;
    private OrderType orderType;

    public Basket() {
    }

    public Basket(Float totalPrize, List<ProductAmount> products, Long restaurantId) {
        this.totalPrize = totalPrize;
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

    public List<ProductAmount> getProducts() {
        return products;
    }

    public void setProducts(List<ProductAmount> products) {
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
