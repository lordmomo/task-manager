package com.momo.task.manager.security;

import com.momo.task.manager.service.impl.JwtServiceImpl;
import com.momo.task.manager.utils.ResourceInformation;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class JwtAuthenticateFilter extends OncePerRequestFilter {
    JwtServiceImpl jwtService;
    UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticateFilter(JwtServiceImpl jwtService,
                                 UserDetailsService userDetailsService){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var jwtTokenOptional = getTokenFromRequest(request);

        jwtTokenOptional.ifPresent(
                jwtToken -> {
                    if (jwtService.validateToken(jwtToken)) {
                        try {
                            // Fetch user details with the help of username

                            var usernameOptional = jwtService.getUsernameFromToken(jwtToken);
                            usernameOptional.ifPresent(username -> {
                                var userDetails = userDetailsService.loadUserByUsername(username);//error --null return
                                //Create Authentication token
                                var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                                //Set authentication token to Security Context
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            });

                        } catch (IllegalAccessError e) {
                            log.error(ResourceInformation.ILLEGAL_ACCESS_ERROR_MESSAGE);
                            e.printStackTrace();
                        } catch (ExpiredJwtException e) {
                            log.error(ResourceInformation.TOKEN_EXPIRED_MESSAGE);
                            e.printStackTrace();
                        } catch (MalformedJwtException e) {
                            log.error(ResourceInformation.MALFORMED_JWT_EXCEPTION_MESSAGE);
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error(ResourceInformation.USER_NOT_FOUND_MESSAGE);
                        }

                    } else {
                        log.info(ResourceInformation.TOKEN_EXPIRED_MESSAGE);
                    }
                }
        );
        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        //Extract authentication header
        var authHeader = request.getHeader("Authorization");
        //Bearer <JWT TOKEN>
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            return Optional.of(authHeader.substring(7));
        } else {
            return Optional.empty();
        }
    }
}
