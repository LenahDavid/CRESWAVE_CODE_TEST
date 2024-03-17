package com.example.blogging.controller.unittest;

import com.example.blogging.controllers.UserController;
import com.example.blogging.entity.User;
import com.example.blogging.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userService.createUser(any(User.class))).thenReturn(user);

        User savedUser = userController.saveUser(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testGetUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = userController.getUser(1L);

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getId()).isEqualTo(1L);
        assertThat(retrievedUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testUpdateUser() {
        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setUsername("updateduser");

        when(userService.updateUser(any(User.class))).thenReturn(updateUser);

        User updatedUser = userController.updateUser(1L, updateUser);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getUsername()).isEqualTo("updateduser");
    }

    @Test
    public void testDeleteUser() {
        ResponseEntity<Void> responseEntity = userController.deleteUser(1L);

        verify(userService).deleteUserById(1L);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}

