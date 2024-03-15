package com.example.blogging.service;

import com.example.blogging.entity.Role;
import com.example.blogging.entity.User;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService = new UserServiceImpl(); // Assuming UserServiceImpl implements UserService

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setRole(Role.USER);
    }

    @Test
    void testCreateUser() {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser);

        User createdUser = userService.createUser(testUser);

        Assertions.assertEquals(testUser.getId(), createdUser.getId());
    }

    @Test
    void testGetUserById() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> optionalUser = userService.getUserById(1L);

        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals(testUser, optionalUser.get());
    }

    @Test
    void testDeleteUserById() {
        userService.deleteUserById(1L);

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testUpdateUser() {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser);

        User updatedUser = userService.updateUser(testUser);

        Assertions.assertEquals(testUser, updatedUser);
    }
}

