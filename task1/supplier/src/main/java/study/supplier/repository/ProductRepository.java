package study.supplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import study.supplier.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {
}
