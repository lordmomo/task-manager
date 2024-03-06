package com.momo.task.manager.controller;

import com.momo.task.manager.exception.InvalidTokenException;
import com.momo.task.manager.exception.UserNameNotFoundException;
import com.momo.task.manager.request.AuthRequestDto;
import com.momo.task.manager.response.AuthResponseDto;
import com.momo.task.manager.service.interfaces.AuthService;
import com.momo.task.manager.service.interfaces.JwtService;
import com.momo.task.manager.utils.AuthStatus;
import com.momo.task.manager.utils.ConstantEndpoints;
import com.momo.task.manager.utils.ConstantInformation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping(ConstantEndpoints.MAIN_AUTH_KEY)
public class AuthController {

    private final AuthService authServiceImpl;
    private final JwtService jwtServiceImpl;

    @Autowired
    public AuthController(AuthService authServiceImpl, JwtService jwtServiceImpl) {
        this.authServiceImpl = authServiceImpl;
        this.jwtServiceImpl = jwtServiceImpl;
    }

    @PostMapping(ConstantEndpoints.LOGIN_USER_ENDPOINT)
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        try {
            var jwtToken = authServiceImpl.login(authRequestDto.username(), authRequestDto.password());
            var refreshToken = jwtServiceImpl.generateRefreshToken(authRequestDto.username());
            var authResponseDto = new AuthResponseDto(jwtToken, refreshToken, AuthStatus.LOGIN_SUCCESS);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authResponseDto);
        } catch (Exception e) {
            var authResponseDto = new AuthResponseDto(null, null, AuthStatus.LOGIN_FAILED);
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(authResponseDto);
        }
    }

    @PostMapping(ConstantEndpoints.REFRESH_TOKEN_ENDPOINT)
    public ResponseEntity<AuthResponseDto> refreshToken(HttpServletRequest request) {
        Optional<String> optionalToken = authServiceImpl.getTokenFromRequest(request);
        String username = validateAndRetrieveUsername(optionalToken);
        String token = jwtServiceImpl.generateToken(username);
        String refreshToken = jwtServiceImpl.generateRefreshToken(username);
        AuthResponseDto authResponseDto = new AuthResponseDto(token, refreshToken, AuthStatus.NEW_REFRESH_TOKEN_CREATED_SUCCESSFULLY);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authResponseDto);
    }

    private String validateAndRetrieveUsername(Optional<String> optionalToken) {
        AtomicReference<String> usernameHolder = new AtomicReference<>();

        optionalToken.ifPresent(
                jwtToken -> {
                    if (jwtServiceImpl.validateToken(jwtToken)) {
                        jwtServiceImpl.getUsernameFromToken(jwtToken).ifPresent(
                                usernameHolder::set
                        );

                    } else {
                        throw new InvalidTokenException(ConstantInformation.TOKEN_INVALID_MESSAGE);
                    }
                });

        String username = usernameHolder.get();
        if (username.isEmpty()) {
            throw new UserNameNotFoundException(ConstantInformation.USERNAME_NOT_FOUND_MESSAGE);
        }

        return username;
    }
}
