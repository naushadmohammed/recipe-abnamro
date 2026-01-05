package com.abnamro.recipe.services;

import com.abnamro.recipe.dtos.UserDto;
import com.abnamro.recipe.entities.User;
import com.abnamro.recipe.errors.ApplicationException;
import com.abnamro.recipe.repositories.UserRepository;
import com.abnamro.recipe.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public void createUser(UserDto user) {
        if (userRepository.existsByUsername(user.username())) {
            throw new ApplicationException(HttpStatus.CONFLICT, "Username already exists");
        }

        var userEntity = User.builder().username(user.username())
                .password(passwordEncoder.encode(user.password())).build();
        userRepository.save(userEntity);
    }

    public String loginUser(UserDto user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.username(), user.password()));
        return jwtService.generateToken(user.username());
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

}