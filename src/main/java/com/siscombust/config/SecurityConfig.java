package com.siscombust.config;

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
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/img/**", "/js/**").permitAll() // Permite recursos estáticos
                .anyRequest().authenticated() // Obliga a loguearse para todo lo demás
            )
            .formLogin(login -> login
                .loginPage("/login") // Usa nuestro HTML personalizado
                .defaultSuccessUrl("/", true) // Redirige al inicio tras loguearse
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") // Redirige al login tras salir
                .permitAll()
            );
        
        return http.build();
    }
}