package com.acrobtw.elei.core.security;

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

import com.acrobtw.elei.core.security.jwt.JwtService;
import com.acrobtw.elei.domain.auth.enums.AuthProvider;
import com.acrobtw.elei.domain.user.User;
import com.acrobtw.elei.domain.user.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
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

        AuthProvider authProvider = resolveProvider(token.getAuthorizedClientRegistrationId());
        if (authProvider == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported OAuth2 provider");
            return;
        }

        OAuth2UserAttributes attrs = extractAttributes(authProvider, token.getPrincipal());
        if (attrs == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not resolve provider ID");
            return;
        }

        User user = resolveUser(attrs, authProvider, request, response);
        if (user == null) return;

        String jwtToken = jwtService.generateToken(user);
        String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
                .queryParam("token", jwtToken)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }


    private AuthProvider resolveProvider(String registrationId) {
        try {
            return AuthProvider.valueOf(registrationId.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("[OAuth2] Unsupported provider: {}", registrationId);
            return null;
        }
    }

     private OAuth2UserAttributes extractAttributes(AuthProvider provider, OAuth2User oAuth2User) {
        String email;
        String username;
        String providerId;

        if (AuthProvider.GITHUB == provider) {
            username = oAuth2User.getAttribute("login");
            email = oAuth2User.getAttribute("email");
            providerId = String.valueOf((Object) oAuth2User.getAttribute("id"));
            if (email == null) email = username + "@github.local";
        } else if (AuthProvider.GOOGLE == provider) {
            email = oAuth2User.getAttribute("email");
            username = email != null ? email.split("@")[0] : oAuth2User.getAttribute("name");
            providerId = oAuth2User.getAttribute("sub");
        } else {
            return null;
        }

        if (providerId == null) {
            log.warn("[OAuth2] Provider ID is null for provider: {}", provider);
            return null;
        }

        return new OAuth2UserAttributes(email, username, providerId);
    }

    private User resolveUser(OAuth2UserAttributes attrs, AuthProvider provider, HttpServletRequest request, HttpServletResponse response) throws IOException {
        var existingByProvider = userRepository.findByProviderAndProviderId(provider, attrs.providerId());
        if (existingByProvider.isPresent()) {
            return existingByProvider.get();
        }

        var existingByEmail = userRepository.findByEmail(attrs.email());
        if (existingByEmail.isPresent()) {
            String errorUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/login")
                    .queryParam("error", "provider_conflict")
                    .build().toUriString();
            getRedirectStrategy().sendRedirect(request, response, errorUrl);
            return null;
        }

        return userRepository.save(buildNewUser(attrs, provider));
    }

    private User buildNewUser(OAuth2UserAttributes attrs, AuthProvider provider) {
        String username = userRepository.findByUsername(attrs.username()).isPresent()
                ? attrs.username() + "_" + UUID.randomUUID().toString().substring(0, 5)
                : attrs.username();

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(attrs.email());
        newUser.setProvider(provider);
        newUser.setProviderId(attrs.providerId());
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        newUser.setTotalExperience(0L);
        return newUser;
    }

    private record OAuth2UserAttributes(String email, String username, String providerId) {}

}
