package edu.bbte.bibliospring.backend.repository;

import edu.bbte.bibliospring.backend.AbstractContainerBaseTest;
import edu.bbte.bibliospring.backend.model.User;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTests extends AbstractContainerBaseTest {

    private UserRepository userRepository;

    @BeforeAll
    void setup() {
        var repositoryFactory = RepositoryFactory.getInstance();
        userRepository = repositoryFactory.getUserRepository();
    }

    @Test
    @Order(1)
    void createUser_positive() {
        var user = new User("zsolt", "password");

        user = userRepository.create(user);

        Assertions.assertNotNull(user.getId());
        Assertions.assertNotNull(user.getUuid());
        Assertions.assertEquals(1, user.getId());
    }

    @Test
    @Order(2)
    void updateUser_positive() {
        var user = new User("zsolt", "password2");
        user.setId(1L);

        user = userRepository.update(user);

        Assertions.assertNotNull(user.getId());
        Assertions.assertNotNull(user.getUuid());
        Assertions.assertEquals("password2", user.getPassword());
        Assertions.assertEquals(1, user.getId());
    }

    @Test
    @Order(6)
    void deleteUser_positive() {
        userRepository.deleteById(1L);
        var user = userRepository.getById(1L);

        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    @Order(5)
    void getAllUsers_positive() {
        var users = userRepository.getAll();

        Assertions.assertFalse(users.isEmpty());
    }

    @Test
    @Order(7)
    void getAllUsers_neutral() {
        var users = userRepository.getAll();

        Assertions.assertEquals(0, users.size());
    }

    @Test
    @Order(4)
    void getUserByUsername_positive() {
        var user = userRepository.getByUsername("zsolt");

        Assertions.assertTrue(user.isPresent());
        Assertions.assertNotNull(user.get().getId());
        Assertions.assertNotNull(user.get().getUuid());
        Assertions.assertEquals(1, user.get().getId());
        Assertions.assertEquals("zsolt", user.get().getUsername());
    }

    @Test
    @Order(3)
    void getUserById_positive() {
        var user = userRepository.getById(1L);

        Assertions.assertTrue(user.isPresent());
        Assertions.assertNotNull(user.get().getId());
        Assertions.assertNotNull(user.get().getUuid());
        Assertions.assertEquals(1, user.get().getId());
        Assertions.assertEquals("zsolt", user.get().getUsername());
    }

}
