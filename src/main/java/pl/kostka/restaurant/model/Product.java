package pl.kostka.restaurant.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.kostka.restaurant.model.enums.ProductType;

import javax.persistence.*;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private ProductType type;
    private Float price;
    private Long imageId;

    public Product() {
    }

    public Product(String name, String description, ProductType type, Float price, Long imageId) {

        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
        this.imageId = imageId;
    }

    public Product(Product product){
        this(product.getName(), product.getDescription(), product.getType(), product.getPrice(), product.getImageId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
