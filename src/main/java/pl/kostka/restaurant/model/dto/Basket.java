package pl.kostka.restaurant.model.dto;

import pl.kostka.restaurant.model.Product;
import pl.kostka.restaurant.model.enums.OrderStatus;

import java.util.List;

public class Basket {

    private Long id;
    private Float totalPrize;
    private OrderStatus orderStatus;
    private List<Integer> productsAmount;
    private List<Product> products;

    public Basket() {
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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
}
