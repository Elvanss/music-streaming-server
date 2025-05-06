package com.music.ms_user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.ms_user.domain.dto.req.LoginDtoReq;
import com.music.ms_user.domain.dto.res.LoginDtoRes;
import com.music.ms_user.domain.dto.res.UserDtoRes;
import com.music.ms_user.domain.entity.User;
import com.music.ms_user.mapper.UserMapper;
import com.music.ms_user.security.JwtRequestFilter;
import com.music.ms_user.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    public AuthController (AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("/v1/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<LoginDtoRes> login(@RequestBody LoginDtoReq loginRequest) {
        logger.info(
            "Login request: Email: {} and Password: {}", 
            loginRequest.getEmail(), 
            loginRequest.getPassword()
            );
        return ResponseEntity.ok(this.authService.login(
            loginRequest
        ));
    }

    @PostMapping("/v1/register")
    public ResponseEntity<UserDtoRes> registerUser(@RequestBody User user) {
        User obj = this.authService.register(user);
        UserDtoRes userDto = this.userMapper.toUserDto(obj);
        return ResponseEntity.ok(userDto);
    }
}
