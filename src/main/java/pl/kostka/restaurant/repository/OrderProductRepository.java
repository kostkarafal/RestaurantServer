package pl.kostka.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kostka.restaurant.model.OrderProduct;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findByOrderId(Long orderId);

}
