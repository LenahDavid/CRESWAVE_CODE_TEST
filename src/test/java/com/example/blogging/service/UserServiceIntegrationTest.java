package com.example.blogging.service;

import com.example.blogging.entity.User;
import com.example.blogging.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceIntegrationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testCreateUser() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User savedUser = userService.createUser(user);

        // Then
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        assertEquals("password", savedUser.getPassword());
    }

    @Test
    public void testGetUserById() {
        // Given
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("testuser");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // When
        Optional<User> foundUser = userService.getUserById(id);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(id, foundUser.get().getId());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    public void testDeleteUserById() {
        // Given
        Long id = 1L;

        // When
        assertDoesNotThrow(() -> userService.deleteUserById(id));

        // Then
        // No exception thrown indicates successful deletion
    }

    @Test
    public void testUpdateUser() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User updatedUser = userService.updateUser(user);

        // Then
        assertNotNull(updatedUser);
        assertEquals("testuser", updatedUser.getUsername());
        assertEquals("password", updatedUser.getPassword());
    }
}

