
package com.hostelmanagement.hostel_management.security;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Return appropriate HTTP responses for security failures
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    "Unauthorized"
                            );
                        })
                        .accessDeniedHandler((request, response, exception) -> {
                            response.sendError(
                                    HttpServletResponse.SC_FORBIDDEN,
                                    "Forbidden"
                            );
                        })
                )

                .authorizeHttpRequests(auth -> auth

                        // Login and registration are public
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        // Only verified old students can create reviews
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/hostels/*/reviews"
                        )
                        .hasRole("VERIFIED_OLD_STUDENT")

                        // Only verified old students can edit reviews
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/reviews/*"
                        )
                        .hasRole("VERIFIED_OLD_STUDENT")

                        // Only verified old students can delete reviews
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/reviews/*"
                        )
                        .hasRole("VERIFIED_OLD_STUDENT")

                        // Logged-in users can see their own reviews
                        .requestMatchers("/api/reviews/my")
                        .authenticated()

                        // Browsing hostels and their reviews is public
                        .requestMatchers("/api/hostels/**")
                        .permitAll()

                        // All remaining endpoints require authentication
                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}