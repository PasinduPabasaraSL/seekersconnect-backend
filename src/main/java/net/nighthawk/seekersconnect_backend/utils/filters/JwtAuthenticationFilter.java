package net.nighthawk.seekersconnect_backend.utils.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.nighthawk.seekersconnect_backend.service.auth.JWTService;
import net.nighthawk.seekersconnect_backend.service.impl.MyUserDetailsService;
import net.nighthawk.seekersconnect_backend.utils.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final ApplicationContext applicationContext;

    @Autowired
    public JwtAuthenticationFilter(JWTService jwtService, ApplicationContext applicationContext) {
        this.jwtService = jwtService;
        this.applicationContext = applicationContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            username = jwtService.extractUserName(jwtToken);

            // Add more detailed debugging
            System.out.println("Extracted JWT Token: " + jwtToken);
            System.out.println("Extracted Username: " + username);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = applicationContext.getBean(MyUserDetailsService.class).loadUserByUsername(username);
            if (jwtService.validateToken(jwtToken, userDetails)) {

                UserRoles userRole = jwtService.extractRole(jwtToken);
                System.out.println("Extracted Role: " + userRole);

                System.out.println("UserDetails authorities: " + userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                userDetails.getAuthorities()
                        );

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);

                // Add debug logging for the created token
                System.out.println("Created authentication token - Principal: " + token.getPrincipal());
                System.out.println("Created authentication token - Authorities: " + token.getAuthorities());
            }
        }

        filterChain.doFilter(request, response);
    }
}
