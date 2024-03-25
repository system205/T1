package study.task3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.task3.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
