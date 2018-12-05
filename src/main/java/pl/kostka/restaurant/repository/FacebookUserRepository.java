package pl.kostka.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kostka.restaurant.model.FacebookUser;

import java.util.Optional;

@Repository
public interface FacebookUserRepository extends JpaRepository<FacebookUser, Long> {
}
