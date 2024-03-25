package study.task3.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.task3.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

}
