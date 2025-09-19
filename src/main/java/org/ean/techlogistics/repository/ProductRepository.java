package org.ean.techlogistics.repository;

import org.ean.techlogistics.entity.Product;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code);

    boolean existsByCode(String code);

    @Modifying
    @Query("update Product p set p.stock = p.stock + :delta where p.id = :productId")
    int adjustStock(Long productId, Integer delta);
}
