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

    public Product() {
    }

    public Product(String name, String description, ProductType type, Float price) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.price = price;
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
}
