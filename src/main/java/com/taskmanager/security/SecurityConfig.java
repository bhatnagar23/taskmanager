package com.taskmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.taskmanager.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    @Lazy
    private JwtFilter jwtFilter;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .map(user -> User.withUsername(user.getEmail())
                        .password(user.getPassword())
                        .build())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // ✅ Allow ALL static HTML pages
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login.html",
                                "/signup.html",
                                "/projects.html",
                                "/tasks.html",
                                "/dashboard.html",
                                "/*.html",
                                "/*.css",
                                "/*.js",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/error",
                                "/favicon.ico"
                        ).permitAll()
                        // ✅ Allow auth APIs
                        .requestMatchers("/api/auth/**").permitAll()
                        // 🔒 Everything else needs token
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}