package com.music.ms_user.security;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.music.ms_user.domain.entity.User;
import com.music.ms_user.repository.UserRepository;
import com.music.ms_user.repository.UserRoleRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserDetailsServiceImpl(
        UserRepository userRepository,
        UserRoleRepository userRoleRepository
        ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Debug time console
        long start = System.currentTimeMillis();

        // Find user by email
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Debug time console
        long end = System.currentTimeMillis();

        // Debug time console
        System.out.println("Database start at: " + start);
        System.out.println("Database end at: " + end);
        System.out.println("Database query time with ms: " +  (end - start));

        return UserDetailsImpl.build(user, userRoleRepository);
    }
}
