package com.ikhzan.shm.utils;

import com.ikhzan.shm.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private JwtService jwtService; // Your JWT service for token operations
    private UserDetailsService userDetailsService; // Service to load user details
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        String path = request.getRequestURI();

        // Log incoming request path
        logger.info("Incoming request path: {}", path);

        // Skip JWT validation for specific paths
        if (shouldNotFilter(request)) {
            logger.info("Skipping JWT validation for path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.replace("Bearer ", "");
            try {
                if (JwtUtil.validateToken(jwtToken)) {
                    String username = JwtUtil.getUsernameFromToken(jwtToken);
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(username, null, null)
                    );
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }
            } catch (Exception ex) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error validating token");
                ex.printStackTrace(); // Log the exception
                return;
            }
        }

        filterChain.doFilter(request, response);
    }



    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

//        return path.startsWith("/swagger-ui") ||
//                path.startsWith("/v3/api-docs") ||
//                path.equals("/") ||
//                path.startsWith("/api/auth");
        return path.startsWith("/swagger-ui/") || // Ensure trailing slash
                path.startsWith("/v3/api-docs/") || // Ensure trailing slash
                path.equals("/") ||
                path.startsWith("/api/auth/") || // Ensure trailing slash
                path.equals("/favicon.ico") || // Allow favicon
                path.equals("/error"); // Allow error page
    }



}
