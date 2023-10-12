package edu.bbte.bibliospring.backend.service;

import edu.bbte.bibliospring.backend.AbstractContainerBaseTest;
import edu.bbte.bibliospring.backend.model.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTests extends AbstractContainerBaseTest {

    private UserService userService;

    @BeforeAll
    void setup() {
        userService = ServiceFactory.getUserService();
    }

    @Test
    @Order(1)
    void register_positive() {
        User user = new User("zsolt", "password");
        user = userService.register(user);

        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertNotEquals("password", user.getPassword());
    }

    @Test
    @Order(2)
    void register_negative() {
        User user = new User("zsolt", "password");
        Assertions.assertThrows(ServiceException.class, () -> userService.register(user));
    }

    @Test
    @Order(3)
    void login_positive() {
        User user = new User("zsolt", "password"); // correct password
        boolean res = userService.login(user);

        Assertions.assertTrue(res);
    }

    @Test
    @Order(4)
    void login_negative() {
        User user = new User("zsolt", "password2"); // wrong password
        boolean res = userService.login(user);

        Assertions.assertFalse(res);
    }

    @Test
    @Order(5)
    void find_positive() {
        Optional<User> user = userService.findByUsername("zsolt");

        Assertions.assertFalse(user.isEmpty());
        Assertions.assertEquals("zsolt", user.get().getUsername());
        Assertions.assertNotNull(user.get().getId());
    }

    @Test
    @Order(6)
    void findall_positive() {
        List<User> users = userService.findAll();

        Assertions.assertFalse(users.isEmpty());
        Assertions.assertTrue(users.stream().anyMatch(user -> "zsolt".equals(user.getUsername())));
    }

    @Test
    @Order(7)
    void update_positive() {
        final User user = new User("zsolt", "password2");
        final User updatedUser = userService.update(user);

        Assertions.assertEquals(user.getUsername(), updatedUser.getUsername());
        Assertions.assertEquals(user.getPassword(), updatedUser.getPassword());
        Assertions.assertNotEquals("password2", user.getPassword());
    }

}
