package pl.kostka.restaurant.model.dto;

import pl.kostka.restaurant.model.Product;

public class ProductAmount {

    private Product product;
    private Integer amount;

    public ProductAmount() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
