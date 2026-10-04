package com.k955.Coden.bootstrap;

import com.k955.Coden.entity.User;
import com.k955.Coden.enums.User.Role;
import com.k955.Coden.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.super-admin.enabled", havingValue = "true")
public class SuperAdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.super-admin.name}")
    private String name;

    @Value("${app.super-admin.username}")
    private String username;

    @Value("${app.super-admin.email}")
    private String email;

    @Value("${app.super-admin.password}")
    private String rawPassword;

    @Override
    public void run(ApplicationArguments args) {
        if(userRepository.existsByEmail(email)) {
            log.info("Super admin bootstrap skipped: user already exists");
            return;
        }

        userRepository.save(User.builder()
                .name(name)
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(Role.SUPER_ADMIN)
                .build());

        log.info("Super admin bootstrap completed for {}", email);
    }

}