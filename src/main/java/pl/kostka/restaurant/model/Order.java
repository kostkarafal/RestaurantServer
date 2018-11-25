package pl.kostka.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.kostka.restaurant.model.enums.OrderStatus;
import pl.kostka.restaurant.model.enums.OrderType;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private OrderStatus status;
    private Float totalPrice;
    private OrderType orderType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "order_product", joinColumns
            = @JoinColumn(name = "order_id",
            referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id",
                    referencedColumnName = "id"))
    private List<Product> products;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "delivery_address_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Address deliveryAddress;

    public Order() {
    }

    public Order(OrderStatus status, Float totalPrice, Restaurant restaurant, User user, List<Product> products) {
        this.status = status;
        this.totalPrice = totalPrice;
        this.restaurant = restaurant;
        this.user = user;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
