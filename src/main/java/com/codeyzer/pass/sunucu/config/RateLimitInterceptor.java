package com.codeyzer.pass.sunucu.config;

import com.codeyzer.pass.sunucu.entity.Kullanici;
import com.codeyzer.pass.sunucu.exception.CodeyzerPassException;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.codeyzer.pass.sunucu.config.WebGuvenlikYapilandirma.WHITE_LIST;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> bucketMap = new ConcurrentHashMap<>();
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String identifier = authentication.getPrincipal() instanceof Kullanici kullanici ?
                kullanici.getKullaniciKimlik() :
                request.getRemoteAddr();

        String path = request.getRequestURI();
        String key = identifier + ":" + path;

        Bucket bucket = bucketMap.computeIfAbsent(key, k -> createBucket(path));
        if (!bucket.tryConsume(1)) {
            throw new CodeyzerPassException(HttpStatus.TOO_MANY_REQUESTS, "Çok fazla istek gönderildi.");
        }
        return true;
    }

    private Bucket createBucket(String path) {
        boolean isWhitelisted = Stream.of(WHITE_LIST).anyMatch(p -> pathMatcher.match(p, path));
        if (isWhitelisted) {
            return Bucket.builder()
                    .addLimit(limit -> limit.capacity(5).refillGreedy(5, Duration.ofMinutes(1)))
                    .build();
        } else {
            return Bucket.builder()
                    .addLimit(limit -> limit.capacity(30).refillGreedy(30, Duration.ofMinutes(1)))
                    .build();

        }
    }
}
