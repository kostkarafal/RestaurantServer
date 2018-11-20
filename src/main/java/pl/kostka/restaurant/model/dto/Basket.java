package pl.kostka.restaurant.model.dto;

import pl.kostka.restaurant.model.Product;


import java.util.List;

public class Basket {

    private Long id;
    private Float totalPrize;
    private List<Integer> productsAmount;
    private List<Product> products;
    private Long restaurantId;

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
}
