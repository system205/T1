package study.task3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.task3.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
