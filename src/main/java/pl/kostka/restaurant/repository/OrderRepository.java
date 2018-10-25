package pl.kostka.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.kostka.restaurant.model.Order;
import pl.kostka.restaurant.model.User;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByRestaurantId(Long restaurantId);
    List<Order> findByUserId(Long userId);
    @Query("select o from Order o where o.user.id = :userId and o.status = 0")
    Order findUserBasket(@NotNull @Param("userId") Long userId);
}
