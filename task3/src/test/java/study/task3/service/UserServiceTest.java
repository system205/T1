package study.task3.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import study.task3.entity.User;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void createUser_shouldCreateNewUserAndLogMethodCalls(CapturedOutput output) {
        final User user = userService.createUser("test", "test@example.com");

        // Check if the user is created
        assertNotNull(user.getId());
        assertEquals("test", user.getName());
        assertEquals(1, userService.getAllUsers().size());

        // Check if the method calls are logged
        assertTrue(output.getOut().contains("Method class study.task3.service.UserService.createUser is called"));
        assertTrue(output.getOut().contains("Method createUser returns"));
        assertTrue(output.getOut().contains("Method class study.task3.service.UserService.getAllUsers is called with args []"));

    }
}