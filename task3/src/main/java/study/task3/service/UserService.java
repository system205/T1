package study.task3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.task3.entity.Order;
import study.task3.entity.User;
import study.task3.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(String name, String email) {
        User user = new User(name, email);
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User is not found with such id"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User changedUser) {
        userRepository.save(changedUser);
        return changedUser;
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void addOrderToUser(Long userId, Order order) {
        final User user = getUser(userId);
        user.getOrders().add(order);
        userRepository.save(user);
    }
}
