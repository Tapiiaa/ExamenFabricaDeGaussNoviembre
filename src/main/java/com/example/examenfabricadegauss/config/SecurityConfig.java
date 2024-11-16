package com.example.examenfabricadegauss.config;

import com.example.examenfabricadegauss.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF para simplificar el desarrollo
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/login.html", "/register.html", "/index.html", "/static/**").permitAll()
                        .anyRequest().authenticated() // Todas las demás requieren autenticación
                )
                .headers(AbstractHttpConfigurer::disable)    // Permite cargar h2 console
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserService userService)
            throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService((UserDetailsService) userService)
                .passwordEncoder(passwordEncoder);
        return authManagerBuilder.build();
    }


    @Bean
    public ExecutorService taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(50);
        taskExecutor.setThreadNamePrefix("GaussTask-");
        taskExecutor.setRejectedExecutionHandler((runnable, executor) -> logger.warn("Tarea {} rechazada de {}", runnable.toString(), executor.toString()));
        taskExecutor.initialize();

        ThreadPoolExecutor executor = taskExecutor.getThreadPoolExecutor();
        logThreadPoolStats(executor);

        return new DelegatingSecurityContextExecutorService(executor);
    }

    @Bean
    public ExecutorService fixedThreadPool() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        logThreadPoolStats((ThreadPoolExecutor) fixedThreadPool);
        return new DelegatingSecurityContextExecutorService(fixedThreadPool);
    }

    private void logThreadPoolStats(ThreadPoolExecutor executor) {
        new Thread(() -> {
            while (true) {
                logger.info("Hilos activos: {} | tamaño de la piscina: {} | Tamaño del grupo central: {} | tamaño máximo de la piscina: {} | Tareas completadas: {} | Recuento de tareas: {}",
                        executor.getActiveCount(),
                        executor.getPoolSize(),
                        executor.getCorePoolSize(),
                        executor.getMaximumPoolSize(),
                        executor.getCompletedTaskCount(),
                        executor.getTaskCount());
                try {
                    Thread.sleep(5000); // Muestra un log cada 5 segundos
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
}
