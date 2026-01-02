package com.abnamro.recipe.services;

import com.abnamro.recipe.TestcontainersConfiguration;
import com.abnamro.recipe.dtos.UserDto;
import com.abnamro.recipe.errors.ApplicationException;
import com.abnamro.recipe.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void user_should_be_created_successfully() {
        var user = new UserDto("user1", "user1Passowrd");

        userService.createUser(user);

        var createdUser = userRepository.findByUsername("user1");
        assertThat(createdUser).isPresent();
        assertThat(createdUser.get().getUsername()).isEqualTo("user1");
        assertThat(passwordEncoder.matches("user1Passowrd", createdUser.get().getPassword())).isTrue();
    }

    @Test
    void duplicate_user_should_throw_exception() {
        var user = new UserDto("user1", "user1Passowrd");
        var duplicateUser = new UserDto("user1", "different_password");

        userService.createUser(user);

        ApplicationException exception = assertThrows(
                ApplicationException.class,
                () -> userService.createUser(duplicateUser)
        );

        assertThat(exception.getMessage()).isEqualTo("Username already exists");
    }
}
