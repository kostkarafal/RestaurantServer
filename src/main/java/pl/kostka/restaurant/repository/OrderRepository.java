package pl.kostka.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kostka.restaurant.model.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByRestaurantId(Long restaurantId);
    List<Order> findByUserId(Long userId);
}
