package com.music.ms_user.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.music.ms_user.domain.entity.User;
import com.music.ms_user.domain.entity.UserRole;
import com.music.ms_user.repository.UserRoleRepository;

import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UserDetailsImpl implements UserDetails {

    // Implement methods from UserDetails interface
    @Getter
    private UUID userId;

    private final String email;

    @JsonIgnore
    private String password;

    private final Collection<? extends GrantedAuthority> authorities;

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    public UserDetailsImpl(UUID userId, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user, UserRoleRepository userRoleRepository) {
        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getUserId());
        if (userRoles.isEmpty()) {
            logger.error("User roles not found for user: " + user.getEmail());
        } else {
            logger.info("User roles found for user: " + user.getEmail());
            for (UserRole role: userRoles) {
                grantedAuthority.add(new SimpleGrantedAuthority(role.getRole().toString()));
            }
        }
        return new UserDetailsImpl(
                user.getUserId(),
                user.getEmail(),
                user.getPassword(),
                grantedAuthority
        );
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
