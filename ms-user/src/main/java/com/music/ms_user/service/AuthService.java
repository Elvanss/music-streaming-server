package com.music.ms_user.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.music.ms_user.domain.dto.req.LoginDtoReq;
import com.music.ms_user.domain.dto.res.LoginDtoRes;
import com.music.ms_user.domain.entity.User;
import com.music.ms_user.domain.entity.UserRole;
import com.music.ms_user.domain.entity.UserSetting;
import com.music.ms_user.event.OtpRequestEvent;
import com.music.ms_user.exception.InvalidInputException;
import com.music.ms_user.producer.OtpKafkaProducer;
import com.music.ms_user.repository.UserRepository;
import com.music.ms_user.repository.UserRoleRepository;
import com.music.ms_user.repository.UserSettingRepository;
import com.music.ms_user.security.UserDetailsImpl;
import com.music.ms_user.security.UserDetailsServiceImpl;
import com.music.ms_user.utils.constants.Role;
import com.music.ms_user.utils.constants.Status;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserSettingRepository userSettingRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final OtpKafkaProducer otpKafkaProducer;

    @Transactional
    public String sendOtpRequest(LoginDtoReq loginDtoReq) {
        // Create an OTP request event
        OtpRequestEvent event = new OtpRequestEvent(loginDtoReq.getEmail(), loginDtoReq.getPassword());

        // Send the OTP request event to Kafka
        otpKafkaProducer.sendOtpRequest(event);

        // Return a success message
        return "OTP request sent!";
    }
    
    @Transactional
    public LoginDtoRes verifyOtp(String email, Integer otpNumber) {
        // Validate the OTP (this assumes you have a method to verify the OTP)
        boolean isOtpValid = validateOtp(otpNumber);
        if (!isOtpValid) {
            throw new InvalidInputException("Invalid OTP provided.");
        }
    
        // Load user details after OTP verification
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername(email);

        // Generate a JWT token for the user
        String token = jwtService.generateToken(userDetails);

        // Return the token and email
        return new LoginDtoRes(token, userDetails.getUsername());
    }
    
    private boolean validateOtp(Integer otpNumber) {
        return otpNumber != null && otpNumber.equals(123456); 
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
            this.userRoleRepository.save(role);

            UserSetting userSetting = new UserSetting();
            userSetting.setUserId(user.getUserId());
            userSetting.setLanguage("VN");
            userSetting.setStatus(Status.PENDING_VERIFICATION);
            this.userSettingRepository.save(userSetting);
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
