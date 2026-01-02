package com.abnamro.recipe.services;

import com.abnamro.recipe.dtos.UserDto;
import com.abnamro.recipe.entities.User;
import com.abnamro.recipe.errors.ApplicationException;
import com.abnamro.recipe.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void createUser(UserDto user) {
        if (userRepository.existsByUsername(user.username())) {
            throw new ApplicationException("Username already exists");
        }

        var userEntity = User.builder().username(user.username())
                .password(passwordEncoder.encode(user.password())).build();
        userRepository.save(userEntity);
    }
}