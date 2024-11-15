package com.example.examenfabricadegauss.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity in testing. Not recommended for production.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/home").permitAll() // Allow public routes without authentication
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .httpBasic(withDefaults()); // Use basic authentication for simplicity

        return http.build();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public ExecutorService taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setQueueCapacity(50);
        taskExecutor.setThreadNamePrefix("GaussTask-");
        taskExecutor.setRejectedExecutionHandler((runnable, executor) -> {
            logger.warn("Task {} rejected from {}", runnable.toString(), executor.toString());
        });
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
                logger.info("Active Threads: {} | Pool Size: {} | Core Pool Size: {} | Maximum Pool Size: {} | Completed Tasks: {} | Task Count: {}",
                        executor.getActiveCount(),
                        executor.getPoolSize(),
                        executor.getCorePoolSize(),
                        executor.getMaximumPoolSize(),
                        executor.getCompletedTaskCount(),
                        executor.getTaskCount());
                try {
                    Thread.sleep(5000); // Log every 5 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
}
