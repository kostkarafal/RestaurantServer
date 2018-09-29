package pl.kostka.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kostka.restaurant.model.Product;

@Repository
public interface ProductRepository extends JpaRepository <Product, Long> {
}
