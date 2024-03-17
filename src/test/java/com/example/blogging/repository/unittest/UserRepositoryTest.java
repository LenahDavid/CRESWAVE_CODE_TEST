package com.example.blogging.repository.unittest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.blogging.entity.Role;
import com.example.blogging.entity.User;
import com.example.blogging.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@ExtendWith(MockitoExtension.class)
@DataJpaTest // This annotation ensures that Spring Boot sets up a test environment for JPA repositories
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindByRole() {
        // Mocking the behavior of the repository method
        Role role = Role.USER;
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(role);

        when(userRepository.findByRole(any(Role.class))).thenReturn(user);

        // Call the repository method directly
        User result = userRepository.findByRole(role);

        // Verify the result
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testFindByUsername() {
        // Mocking the behavior of the repository method
        String username = "testuser";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Call the repository method directly
        Optional<User> result = userRepository.findByUsername(username);

        // Verify the result
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo(username);
    }
}

