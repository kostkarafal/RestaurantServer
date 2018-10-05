package pl.kostka.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kostka.restaurant.model.DBFile;

@Repository
public interface DBFileRepository extends JpaRepository<DBFile, Long> {
}
