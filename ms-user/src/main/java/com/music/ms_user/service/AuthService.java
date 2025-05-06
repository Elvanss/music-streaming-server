package com.music.ms_user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.music.ms_user.domain.dto.req.LoginDtoReq;
import com.music.ms_user.domain.dto.res.LoginDtoRes;
import com.music.ms_user.domain.entity.User;
import com.music.ms_user.domain.entity.UserRole;
import com.music.ms_user.domain.entity.UserSetting;
import com.music.ms_user.exception.InvalidInputException;
import com.music.ms_user.repository.UserRepository;
import com.music.ms_user.repository.UserRoleRepository;
import com.music.ms_user.repository.UserSettingRepository;
import com.music.ms_user.security.UserDetailsImpl;
import com.music.ms_user.security.UserDetailsServiceImpl;
import com.music.ms_user.utils.constants.Role;
import com.music.ms_user.utils.constants.Status;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired 
    private UserRoleRepository UserRoleRepository;
    @Autowired
    private UserSettingRepository userSettingRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public LoginDtoRes login (LoginDtoReq loginDtoReq) {
        // Validate user by email and password
        try {
            
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDtoReq.getEmail(), loginDtoReq.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect email or password", e);
        }

        // Generate token
        final UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername(loginDtoReq.getEmail());
        final String token = jwtService.generateToken(userDetails);
        final String userEmail = userDetails.getUsername();

        return new LoginDtoRes(token, userEmail);
    }

    @Transactional
    public User register (User user) {
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new InvalidInputException("The email address is already in use.");
            }
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            this.userRepository.save(user);

            UserRole role = new UserRole();
            role.setUserId(user.getUserId());
            role.setRole(Role.USER);
            this.UserRoleRepository.save(role);

            UserSetting userSetting = new UserSetting();
            userSetting.setUserId(user.getUserId());
            userSetting.setLanguage("VN");
            userSetting.setStatus(Status.PENDING_VERIFICATION);
            this.userSettingRepository.save(userSetting);

    //         UserRole role = UserRole.builder()
    //         .userId(user.getUserId())
    //         .role(Role.USER)
    //         .build();
    // this.UserRoleRepository.save(role);

    // UserSetting userSetting = UserSetting.builder()
    //         .userId(user.getUserId())
    //         .language("VN")
    //         .status(Status.PENDING_VERIFICATION)
    //         .build();

    // this.userSettingRepository.save(userSetting);

            return user;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
