package com.example.BudgetBuddy.SpringSecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Ensure proper CORS settings
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(HttpMethod.GET, "/departments", "/departments/**").permitAll() // Allow GET requests to departments
//                        .requestMatchers(HttpMethod.POST, "/departments", "/departments/**").permitAll() // Only ADMIN can create departments
                        .requestMatchers("/auth/**", "/api/v1/password/*").permitAll() // Allow authentication routes
                        .requestMatchers(HttpMethod.GET, "/departments", "/departments/**", "/admin/**", "/notification/**", "/budgets/**", "/onetimeexpenses/**", "/recurringexpenses/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/departments", "/departments/**", "/admin/**", "/notification/**", "/budgets/**", "/onetimeexpenses/**", "/recurringexpenses/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/departments", "/departments/**","/admin/**", "/notification/**", "/budgets/**", "/onetimeexpenses/**", "/recurringexpenses/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/departments", "/departments/**","/admin/**", "/notification/**", "/budgets/**", "/onetimeexpenses/**", "/recurringexpenses/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/departments", "/departments/**","/admin/**", "/notification/**", "/budgets/**", "/onetimeexpenses/**", "/recurringexpenses/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Properly configure CORS
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow specific frontend origins
        config.setAllowedOriginPatterns(List.of("http://localhost:3000", "https://budgetbuddyfe.onrender.com/")); //http://localhost:3000
        config.setAllowCredentials(true); // Required if using authentication

        // Allow all standard headers and methods
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
