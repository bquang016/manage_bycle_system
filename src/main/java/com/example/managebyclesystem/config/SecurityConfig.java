package com.example.managebyclesystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/css/**", "/js/**", "/img/**", "/vendor/**", "/uploads/**",
                                "/",
                                "/login",
                                "/register",
                                "/h2-console/**"
                        ).permitAll()
                        // yêu cầu ADMIN cho dashboard và staff
                        .requestMatchers(
                                "/dashboard",
                                "/staffs/**"
                        ).hasRole("ADMIN")
                        // yêu cầu đăng nhập cho các request còn lại
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/bikes", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                );


        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));


        return http.build();
    }
}