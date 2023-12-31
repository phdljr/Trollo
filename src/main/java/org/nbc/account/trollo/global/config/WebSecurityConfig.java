package org.nbc.account.trollo.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.global.jwt.JwtAuthorizationFilter;
import org.nbc.account.trollo.global.jwt.JwtUtil;
import org.nbc.account.trollo.global.security.UserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper;

    @Bean // encrypt password
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // authenticate
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean // authorize
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService, objectMapper);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF setting
        http.csrf((csrf) -> csrf.disable());

        // setting to not use default(Session) way and use JWT
        http.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll() // allow access to resources
                .requestMatchers("/api/v1/users/signup").permitAll() // allow requests to signup
                .requestMatchers("/api/v1/users/login").permitAll() // allow requests to login
                .anyRequest().authenticated() // other requests should go through authorization
        );

        // filter
        http.addFilterBefore(jwtAuthorizationFilter(),
            UsernamePasswordAuthenticationFilter.class); // username~ 전에 jwtAuthor 먼저

        return http.build();
    }
}
