package com.ZerodaySolution.Billing.services;

import com.ZerodaySolution.Billing.entity.ProfileEntity;
import com.ZerodaySolution.Billing.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AppUserDetailService.class);

    private final ProfileRepository profileRepository;

    // responsible for loading user from db to check aganist provided details during
    // login
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Loading user by email: {}", email);
        ProfileEntity existingUser = profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not find" + email));
        logger.info("User found: {}", existingUser.getEmail());

        return User.builder()
                .username(existingUser.getEmail())
                .password(existingUser.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
