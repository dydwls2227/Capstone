package com.example.oauth2prac;

import com.example.oauth2prac.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**","/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .oauth2Login(oauth -> oauth
                        .loginPage("/")
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                            String registrationId = ((OAuth2AuthenticationToken)  authentication).getAuthorizedClientRegistrationId();

                            OAuthAttributes attributes = OAuthAttributes.of(registrationId,"sub",oAuth2User.getAttributes());

                            User user = userRepository.findByEmail(attributes.getEmail()).orElseThrow(() -> new IllegalArgumentException("해당 이메일이 존재하지 않습니다."));

                            String token = jwtTokenProvider.createToken(user.getId().toString(), user.getRoleKey());

                            response.sendRedirect("/success");
                        })
                );

        return http.build();
    }
}