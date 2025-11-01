package com.codeyzer.pass.sunucu.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebGuvenlikYapilandirma implements WebMvcConfigurer {

    public static final String[] WHITE_LIST = new String[] {
        "/api/kullanici/register",
        "/api/kullanici/login",
        "/api/kullanici/refresh"
    };

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RateLimitInterceptor rateLimitInterceptor;

    @Value("${rate-limiter.enabled:true}")
    private boolean rateLimiterEnabled;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // WHITE_LIST içindeki her bir string path için AntPathRequestMatcher oluştur
        List<RequestMatcher> whiteListMatchers = Arrays.stream(WHITE_LIST)
                .map(AntPathRequestMatcher::new) // Her bir string path'i AntPathRequestMatcher'a dönüştür
                .collect(Collectors.toList());

        // H2 konsolu için de bir AntPathRequestMatcher ekle
        whiteListMatchers.add(new AntPathRequestMatcher("/h2-console/**"));

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Oluşturulan RequestMatcher listesini OrRequestMatcher ile birleştirerek kullan
                        .requestMatchers(new OrRequestMatcher(whiteListMatchers)).permitAll()
                        // .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll() // Bu zaten yukarıda whiteListMatchers'a eklendi
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (rateLimiterEnabled) {
            registry.addInterceptor(rateLimitInterceptor);
        }
    }
}
