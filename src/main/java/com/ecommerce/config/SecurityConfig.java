package com.ecommerce.config;

import com.ecommerce.exception.SecurityExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    
    @Autowired
    private SecurityExceptionHandler securityExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/auth/**").permitAll()
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/register/send-otp").permitAll()
                .requestMatchers("/api/register/verify-otp").permitAll()
                .requestMatchers("/api/forgot-password").permitAll()
                .requestMatchers("/api/reset-password").permitAll()
                .requestMatchers("/api/products").permitAll()
                .requestMatchers("/api/product/**").permitAll()
                .requestMatchers("/api/product-search").permitAll()
                .requestMatchers("/api/products/category/**").permitAll()
                .requestMatchers("/api/categories").permitAll()
                .requestMatchers("/login/oauth2/code/**").permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                
                // Admin-only endpoints
                .requestMatchers("/api/admin/user/**").hasRole("ADMIN")
                .requestMatchers("/api/admin/users").hasAnyRole("ADMIN", "STAFF")
                
                // Staff and Admin endpoints (these can be accessed by either)
                .requestMatchers("/api/admin/staff/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers("/api/admin/products").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers("/api/admin/product/**").hasAnyRole("ADMIN", "STAFF") 
                .requestMatchers("/api/admin/orders").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers("/api/admin/order/**").hasAnyRole("ADMIN", "STAFF")
                
                .requestMatchers(req -> req.getMethod().equals("OPTIONS")).permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement()
            .sessionCreationPolicy(STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(securityExceptionHandler)
            .accessDeniedHandler(securityExceptionHandler)
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://127.0.0.1:4200", "http://localhost:8080", "http://127.0.0.1:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
} 