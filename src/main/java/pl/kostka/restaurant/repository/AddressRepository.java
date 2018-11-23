package pl.kostka.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.kostka.restaurant.model.Address;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId);
    List<Address> findByUserIdAndIsAvailable(Long userId, Boolean isAvailable);

    Optional<Address> findByUserIdAndId(Long userId, Long addressId);
}
