package com.abnamro.recipe.services;

import com.abnamro.recipe.TestcontainersConfiguration;
import com.abnamro.recipe.dtos.UserDto;
import com.abnamro.recipe.errors.ApplicationException;
import com.abnamro.recipe.repositories.UserRepository;
import com.abnamro.recipe.utils.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.AuthenticationException;
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
        var user = TestUtil.getUser1();

        userService.createUser(user);

        var createdUser = userRepository.findByUsername(user.username());
        assertThat(createdUser).isPresent();
        assertThat(createdUser.get().getUsername()).isEqualTo(user.username());
        assertThat(passwordEncoder.matches(user.password(), createdUser.get().getPassword())).isTrue();
    }

    @Test
    void duplicate_user_should_throw_exception() {
        var user = TestUtil.getUser1();
        var duplicateUser = new UserDto(user.username(), "different_password");

        userService.createUser(user);

        ApplicationException exception = assertThrows(
                ApplicationException.class,
                () -> userService.createUser(duplicateUser)
        );

        assertThat(exception.getMessage()).isEqualTo("Username already exists");
    }

    @Test
    void user_should_login_successfully() {
        var user = TestUtil.getUser1();
        userService.createUser(user);

        String token = userService.loginUser(user);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void user_login_should_fail_with_wrong_password() {
        var user = TestUtil.getUser1();
        userService.createUser(user);

        var wrongPassword = new UserDto(user.username(), "wrongPassword");

        assertThrows(
                AuthenticationException.class,
                () -> userService.loginUser(wrongPassword)
        );
    }

    @Test
    void user_login_should_fail_with_wrong_username() {
        var user = TestUtil.getUser1();
        userService.createUser(user);

        var wrongUsername = new UserDto("user2", user.password());

        assertThrows(
                AuthenticationException.class,
                () -> userService.loginUser(wrongUsername)
        );
    }
}
