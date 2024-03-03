package study.supplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.supplier.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
