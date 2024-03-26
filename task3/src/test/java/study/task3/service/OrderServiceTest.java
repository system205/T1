package study.task3.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import study.task3.entity.Order;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @BeforeEach
    void addUser(){
        userService.createUser("John", "john@gmail.com");
    }

    @Test
    void testCreateOrder(CapturedOutput output){

        final Order order = orderService.createOrder("test", 1L);
        assertNotNull(order.getId());
        assertEquals(1, orderService.getAllOrders().size());

        assertTrue(output.getOut().contains("Method class study.task3.service.OrderService.createOrder is called"));

        assertThrows(IllegalArgumentException.class, () -> orderService.getOrder(123L));
        assertTrue(output.getOut().contains("Method getOrder throws an exception: java.lang.IllegalArgumentException"));
    }

    @AfterEach
    void removeUser(){
        userService.deleteUser(1L);
    }
}
