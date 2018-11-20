package pl.kostka.restaurant.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.kostka.restaurant.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

 //   @Query("SELECT u FROM User u WHERE u.login = :login AND u.password = :password")
 //   List<User> checkLogin(@Param("login") String login,
 //                          @Param("password") String password);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);
}
