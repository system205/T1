package study.supplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.supplier.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
