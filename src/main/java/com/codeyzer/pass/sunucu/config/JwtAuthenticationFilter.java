package com.codeyzer.pass.sunucu.config;

import com.codeyzer.pass.sunucu.dto.CodeyzerPassErrorResponseDTO;
import com.codeyzer.pass.sunucu.entity.Kullanici;
import com.codeyzer.pass.sunucu.exception.CodeyzerPassException;
import com.codeyzer.pass.sunucu.service.KullaniciService;
import com.codeyzer.pass.sunucu.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

import static com.codeyzer.pass.sunucu.config.WebGuvenlikYapilandirma.WHITE_LIST;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final KullaniciService kullaniciService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        boolean isWhitelisted = Stream.of(WHITE_LIST).anyMatch(p -> pathMatcher.match(p, request.getRequestURI()));
        if (isWhitelisted) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String accessToken = (authHeader != null && authHeader.startsWith("Bearer ")) ?
                authHeader.substring(7) : null;

        if (accessToken != null) {
            try {
                Claims accessTokenClaims = jwtUtil.extractToken(accessToken);

                String kullaniciKimlik = accessTokenClaims.getSubject();

                // Kullanıcıyı cache’den al
                Kullanici kullanici = kullaniciService.getByKimlik(kullaniciKimlik);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        kullanici, null, new ArrayList<>()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(
                        new CodeyzerPassErrorResponseDTO("Oturumun süresi doldu.", request.getRequestURI(), 401, LocalDateTime.now())
                ));
                return;
            } catch (JwtException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(
                        new CodeyzerPassErrorResponseDTO("Geçersiz token gönderildi.", request.getRequestURI(), 401, LocalDateTime.now())
                ));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
