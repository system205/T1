package study.supplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.supplier.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
