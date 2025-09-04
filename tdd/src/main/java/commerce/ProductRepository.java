package commerce;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(UUID id);
    List<Product> findBySellerId(UUID sellerId);
}
