package com.k955.Coden.security.OAuth;

import com.k955.Coden.entity.User;
import com.k955.Coden.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Object id = oAuth2User.getAttribute("id");
        if (id == null) {
            throw new OAuth2AuthenticationException("GitHub ID not found");
        }
        Long gitHubId = Long.valueOf(id.toString());
        String login = oAuth2User.getAttribute("login");

        User user = userRepository.findByGithubId(gitHubId)
                .orElseGet(() -> findByEmailOrCreate(oAuth2User, gitHubId, login));

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

    private User findByEmailOrCreate(OAuth2User oAuth2User, Long gitHubId, String login) {
        String email = normalize(oAuth2User.getAttribute("email"));

        if (email != null) {
            Optional<User> existing = userRepository.findByEmail(email);
            if (existing.isPresent()) {
                User user = existing.get();
                if (user.getGithubId() != null && !user.getGithubId().equals(gitHubId)) {
                    throw new OAuth2AuthenticationException("GitHub account is already linked to a different user");
                }
                user.setGithubId(gitHubId);
                user.setAvatarUrl(oAuth2User.getAttribute("avatar_url"));
                user.setProfileUrl(oAuth2User.getAttribute("html_url"));
                return userRepository.save(user);
            }
        }

        return createUser(oAuth2User, gitHubId, login, email);
    }

    private User createUser(OAuth2User oAuth2User, Long gitHubId, String login, String email) {
        String name = normalize(oAuth2User.getAttribute("name"));
        if (name == null) {
            name = login;
        }

        return userRepository.save(User.builder()
                .githubId(gitHubId)
                .name(name)
                .username(resolveUniqueUsername(login))
                .email(email != null ? email : githubNoreplyEmail(gitHubId, login))
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .avatarUrl(oAuth2User.getAttribute("avatar_url"))
                .profileUrl(oAuth2User.getAttribute("html_url"))
                .build());
    }

    private String resolveUniqueUsername(String login) {
        if (userRepository.findByUsername(login).isEmpty()) {
            return login;
        }

        String candidate = login + "-gh";
        int suffix = 2;
        while (userRepository.findByUsername(candidate).isPresent()) {
            candidate = login + "-gh" + suffix++;
        }
        return candidate;
    }

    private String githubNoreplyEmail(Long gitHubId, String login) {
        return gitHubId + "+" + login + "@users.noreply.github.com";
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

}