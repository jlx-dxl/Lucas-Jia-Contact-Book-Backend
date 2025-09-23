package com.lucasjia.contactbookbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 关闭 CSRF，避免 POST/PUT/DELETE 被拦截
                .cors(cors -> {}) // ✅ 启用 CORS，交给 CorsConfig 或 application.properties 配置
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/health/**",
                                "/api/debug/**",
                                "/api/test/**"
                        ).permitAll() // 这些接口放行
                        .anyRequest().authenticated() // 其他接口需要认证
                );

        return http.build();
    }
}
