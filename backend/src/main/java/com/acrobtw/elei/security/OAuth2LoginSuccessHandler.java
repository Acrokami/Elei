package com.acrobtw.elei.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.acrobtw.elei.entity.User;
import com.acrobtw.elei.repository.UserRepository;
import com.acrobtw.elei.security.jwt.JwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();

        String provider = token.getAuthorizedClientRegistrationId();

        String email = null;
        String username = null;

        if("github".equalsIgnoreCase(provider)) {
            username = oAuth2User.getAttribute("login");
            email = oAuth2User.getAttribute("email");
            if(email == null) {
                email = username + "@github.local";
            }
        } else if ("google".equalsIgnoreCase(provider)) {
            email = oAuth2User.getAttribute("email");
            username = email != null ? email.split("@")[0] : oAuth2User.getAttribute("name");
        }

        String finalEmail = email;
        String finalUsername = username;

        User user = userRepository.findByUsername(finalUsername).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(finalUsername);
            newUser.setEmail(finalEmail);
            newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            newUser.setTotalExperience(0L);
            return userRepository.save(newUser);
        });

        String jwtToken = jwtService.generateToken(user);
        String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
                .queryParam("token", jwtToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
