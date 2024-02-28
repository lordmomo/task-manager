package com.momo.task.manager.service.interfaces;

import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

public interface JwtService {

    boolean validateToken(String jwtToken);

    Date getExpirationDateFromToken(String token);

    Optional<String> getUsernameFromToken(String jwtToken);

    <T> T getClaimsFromToken(String token, Function<Claims, T> claimResolver);

    Claims getAllClaimsFromToken(String token);

    Optional<Claims> parseToken(String jwtToken);

    boolean isTokenExpired(Claims token);

    String generateToken(String username);

    String generateRefreshToken(String username);
}
