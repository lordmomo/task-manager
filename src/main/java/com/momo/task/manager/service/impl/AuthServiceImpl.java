package com.momo.task.manager.service.impl;

import com.momo.task.manager.service.interfaces.AuthService;
import com.momo.task.manager.service.interfaces.JwtService;
import com.momo.task.manager.utils.ResourceInformation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    @Autowired
    private final JwtService jwtServiceImpl;

    @Override
    public String login(String username, String password) {
        var authToken = new UsernamePasswordAuthenticationToken(username, password);

        try {
            //It is the responsibility of authenticationManger
            //to call respective authentication provider to authenticate
            //these credentials
            var authenticate = authenticationManager.authenticate(authToken);
            return jwtServiceImpl.generateToken(((UserDetails) (authenticate.getPrincipal())).getUsername());

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(ResourceInformation.BAD_CREDENTIALS_MESSAGE);
        }
    }

    @Override
    public Optional<String> getTokenFromRequest(HttpServletRequest request) {
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
