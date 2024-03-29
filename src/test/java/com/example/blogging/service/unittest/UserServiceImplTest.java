package com.example.blogging.service.unittest;

import com.example.blogging.entity.BlogPost;
import com.example.blogging.entity.Role;
import com.example.blogging.entity.User;
import com.example.blogging.repository.BlogPostRepository;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BlogPostRepository blogPostRepository;


    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setRole(Role.USER);
        // You can set other fields as needed
    }

    @Test
    void testCreateUser() {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser);

        User createdUser = userService.createUser(testUser);

        Assertions.assertEquals(testUser.getId(), createdUser.getId());
        // Add more assertions as needed to verify other fields
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
        // Mock blogPostRepository's existsById method to return true
        Mockito.when(blogPostRepository.existsById(1L)).thenReturn(true);

        // Ensure userRepository returns true for existsById(1L)
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);

        // Mock behavior to consistently find the BlogPost by ID
        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        Mockito.when(blogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost));

        // Test the method
        userService.deleteUserById(1L);

        // Verify calls to repositories
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
        // Verify interaction with blogPostRepository if applicable
        Mockito.verify(blogPostRepository, Mockito.times(1)).findById(1L);
    }


    @Test
    void testUpdateUser() {
        // Ensure the repository returns a User when findById(1L) is called
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser);

        User updatedUser = userService.updateUser(testUser);

        Assertions.assertEquals(testUser, updatedUser);
        // Add more assertions as needed to verify other fields
    }


    @Test
    void testUserDetailsService() {
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername("testUser");

        Assertions.assertEquals(testUser.getUsername(), userDetails.getUsername());
        // Add more assertions as needed to verify other fields
    }

    @Test
    void testUserDetailsService_UsernameNotFound() {
        Mockito.when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.userDetailsService().loadUserByUsername("unknownUser"));
    }
}

