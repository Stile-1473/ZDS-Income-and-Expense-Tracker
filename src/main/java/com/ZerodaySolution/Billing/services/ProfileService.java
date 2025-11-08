package com.ZerodaySolution.Billing.services;

import com.ZerodaySolution.Billing.dto.AuthDTO;
import com.ZerodaySolution.Billing.dto.ProfileDTO;
import com.ZerodaySolution.Billing.entity.ProfileEntity;
import com.ZerodaySolution.Billing.repository.ProfileRepository;
import com.ZerodaySolution.Billing.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
   // @Value("${Billing_Profile_Activation}")
    private String activationUrl;
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public ProfileDTO registerProfile(ProfileDTO profileDTO) {
        ProfileEntity newprofile = toEntity(profileDTO);
        newprofile.setIsActive(true); // Auto-activate account
        newprofile = profileRepository.save(newprofile);

        // send welcome email asynchronously

       // String subject = "Welcome to Billing App";
       // String body = "Your account has been successfully created and activated. You can now log in.";
       // emailService.sendMail(newprofile.getEmail(), subject, body);

        return toDTI(newprofile);
    }

    public ProfileEntity toEntity(ProfileDTO profileDTO) {
        return ProfileEntity.builder()
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(passwordEncoder.encode(profileDTO.getPassword()))
                .profileImageUrl(profileDTO.getProfileImageUrl())
                .createdAt(profileDTO.getCreatedAt())
                .updatedAt(profileDTO.getUpdatedAt())
                .build();
    }

    public ProfileDTO toDTI(ProfileEntity profileEntity) {

        return ProfileDTO.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }



    public ProfileEntity getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return profileRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Profile not find" + authentication.getName()));

    }

    public ProfileDTO getPublicProfile(String email) {
        ProfileEntity currentUser = null;
        if (email == null) {
            currentUser = getCurrentProfile();

        } else {
            currentUser = profileRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found" + email));
        }

        return ProfileDTO.builder()

                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getEmail())
                .profileImageUrl(currentUser.getProfileImageUrl())
                .createdAt(currentUser.getCreatedAt())
                .updatedAt(currentUser.getUpdatedAt())

                .build();

    }

    public Map<String, Object> autheniticateAndGenerateToken(AuthDTO authDTO) {

        logger.info("Attempting authentication for email: {}", authDTO.getEmail());
        try {

            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));
            logger.info("Authentication successful for email: {}", authDTO.getEmail());
            // Generate Jwt token
            String token = jwtUtil.generateToken(authDTO.getEmail());
            return Map.of(
                    "token", token,
                    "user", getPublicProfile(authDTO.getEmail())

            );

        } catch (BadCredentialsException e) {
            logger.error("Bad credentials for email: {}", authDTO.getEmail());
            throw new RuntimeException("Invalid email or password");
        } catch (Exception e) {
            logger.error("Authentication failed for email: {} with error: {}", authDTO.getEmail(), e.getMessage());
            throw new RuntimeException("Invalid email or password");
        }
    }

}
