package com.example.examenfabricadegauss.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF para facilitar pruebas. No recomendable en producción.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/home").permitAll() // Rutas permitidas sin autenticación
                        .anyRequest().authenticated() // Cualquier otra solicitud requiere autenticación
                )
                .httpBasic(withDefaults()); // Usar autenticación básica para pruebas

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        // Definir un usuario en memoria con nombre de usuario y contraseña
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build());
        return manager;
    }


    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public ExecutorService taskExecutor() {
        // Definir un pool fijo de hilos para concurrencia
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(50);
        taskExecutor.initialize();
        return new DelegatingSecurityContextExecutorService(taskExecutor.getThreadPoolExecutor());
    }

    @Bean
    public ExecutorService fixedThreadPool() {
        // Otra alternativa para concurrencia: pool fijo de 5 hilos
        return new DelegatingSecurityContextExecutorService(Executors.newFixedThreadPool(5));
    }


}
