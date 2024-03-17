package com.example.blogging.service.integrationtest;
import com.example.blogging.entity.User;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplIntegrationTest {

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

        // Mock repository behavior
        when(userRepository.findById(id)).thenReturn(Optional.of(new User()));

        // When
        assertDoesNotThrow(() -> userService.deleteUserById(id));

        // Then
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteUserByIdNotFound() {
        // Given
        Long id = 1L;

        // Mock repository behavior
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUserById(id));
    }

    @Test
    public void testUpdateUser() {
        // Given
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existingUser");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updatedUser");

        // Mock repository behavior
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        User result = userService.updateUser(updatedUser);

        // Then
        assertNotNull(result);
        assertEquals("updatedUser", result.getUsername());
    }
}

