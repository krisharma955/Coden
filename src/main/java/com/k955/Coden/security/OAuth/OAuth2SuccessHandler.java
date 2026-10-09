package com.k955.Coden.security.OAuth;

import com.k955.Coden.security.JwtAuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.oauth.redirect-base:http://localhost:5173}")
    private String redirectBase;

    private final JwtAuthUtil jwtAuthUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String token = jwtAuthUtil.generateAccessToken(customOAuth2User.getUser());

        response.sendRedirect(
                redirectBase + "/oauth-success?token=" + token
        );
    }

}
