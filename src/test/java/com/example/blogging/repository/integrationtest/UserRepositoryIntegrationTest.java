package com.example.blogging.repository.integrationtest;


import com.example.blogging.entity.Role;
import com.example.blogging.entity.User;
import com.example.blogging.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByRole() {
        Role role = Role.USER;
        userRepository.save(new User());

        User foundUser = userRepository.findByRole(role);

        assertEquals("user1", foundUser.getUsername());
    }

    @Test
    public void testFindByUsername() {
        Role role = Role.USER; // Using predefined enum value
        User user = new User();
        user.setUsername("user2");
        user.setPassword("password");
        user.setRole(role);
        userRepository.save(user);

        User foundUser = userRepository.findByUsername("user2").orElse(null);

        assertTrue(foundUser != null && foundUser.getUsername().equals("user2"));
    }
}
