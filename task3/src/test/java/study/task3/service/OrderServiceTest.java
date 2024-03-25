package study.task3.service;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @PostConstruct
    void do1(){
        userService.createUser("John", "john@gmail.com");
    }

    @Test
    void testCreateOrder(CapturedOutput output){

        orderService.createOrder("test", 1L);
        assertEquals(1, orderService.getAllOrders().size());

        assertTrue(output.getOut().contains("Method class study.task3.service.OrderService.createOrder is called"));
    }
}
