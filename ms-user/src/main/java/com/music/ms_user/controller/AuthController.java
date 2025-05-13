package com.music.ms_user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.ms_user.domain.dto.req.LoginDtoReq;
import com.music.ms_user.domain.dto.res.UserDtoRes;
import com.music.ms_user.domain.entity.User;
import com.music.ms_user.mapper.UserMapper;
import com.music.ms_user.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController (AuthService authService, UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    // @PostMapping("/v1/login")
    // public ResponseEntity<String> login(@RequestBody LoginDtoReq loginRequest) {
    //     logger.info(
    //         "Login request: Email: {} and Password: {}", 
    //         loginRequest.getEmail(), 
    //         loginRequest.getPassword()
    //         );
    //     return ResponseEntity.ok(this.authService.sendOtpRequest(loginRequest));
    // }

    @PostMapping("/v1/register")
    public ResponseEntity<UserDtoRes> registerUser(@RequestBody User user) {
        User obj = this.authService.register(user);
        UserDtoRes userDto = this.userMapper.toUserDto(obj);
        return ResponseEntity.ok(userDto);
    }
}
