package com.project.common.jwt;

import com.project.common.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {
    private static final String BEARER_PREFIX = "Bearer ";
    public static final String USER_ID_ATTRIBUTE = "userId";

    private final String jwtSecret;

    public JwtAuthInterceptor(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (!(handler instanceof HandlerMethod method)) return true;

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            String token = header.substring(BEARER_PREFIX.length());
            try {
                AuthToken authToken = AuthToken.parse(token, jwtSecret);
                request.setAttribute(USER_ID_ATTRIBUTE, authToken.getUserId());
            } catch (ExpiredJwtException e) {
                throw e;
            } catch (Exception ignored) {
            }
        }

        if (method.hasMethodAnnotation(JwtRequired.class)) {
            String userId = (String) request.getAttribute(USER_ID_ATTRIBUTE);
            if (userId == null) throw new UnauthorizedException();
        }

        return true;
    }
}