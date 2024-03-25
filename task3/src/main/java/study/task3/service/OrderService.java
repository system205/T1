package study.task3.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.task3.entity.Order;
import study.task3.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;

    public Order createOrder(String description, Long userId) {
        Order order = new Order(description);
        orderRepository.save(order);
        userService.addOrderToUser(userId, order);
        return order;
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Order is not found with such id"));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrder(Order changedOrder) {
        orderRepository.save(changedOrder);
        return changedOrder;
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
