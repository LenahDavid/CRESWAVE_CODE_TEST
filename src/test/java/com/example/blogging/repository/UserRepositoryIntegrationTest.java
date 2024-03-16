//package com.example.blogging.repository;
//
//import com.example.blogging.entity.Role;
//import com.example.blogging.entity.User;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//public class UserRepositoryIntegrationTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void testFindByRole() {
//        // Create a role
//        Role role = new Role();
//        role.name = "ROLE_USER"; // Assuming a field named 'name'
//
//        // Create a user with the role
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPassword("password");
//        user.setRole(role);
//        userRepository.save(user);
//
//        // Find the user by role
//        User foundUser = userRepository.findByRole(role);
//
//        assertNotNull(foundUser);
//        assertEquals(user.getUsername(), foundUser.getUsername());
//    }
//
//    @Test
//    public void testFindByUsername() {
//        // Create a user and save it
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPassword("password");
//        userRepository.save(user);
//
//        // Find the user by username
//        Optional<User> foundUserOptional = userRepository.findByUsername("testuser");
//
//        assertTrue(foundUserOptional.isPresent());
//        User foundUser = foundUserOptional.get();
//        assertEquals(user.getUsername(), foundUser.getUsername());
//    }
//}
//
//
