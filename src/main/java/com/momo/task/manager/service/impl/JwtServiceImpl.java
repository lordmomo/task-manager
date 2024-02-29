package com.momo.task.manager.service.impl;

import com.momo.task.manager.service.interfaces.JwtService;
import com.momo.task.manager.utils.ResourceInformation;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    private JwtServiceImpl() {
    }

    @Override
    public Optional<String> getUsernameFromToken(String jwtToken) {
        var claimsOptional = parseToken(jwtToken);
        log.info(String.valueOf(claimsOptional.map(Claims::getSubject)));
        return claimsOptional.map(Claims::getSubject);
    }

    @Override
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token, Claims::getExpiration);
    }

    @Override
    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        log.info(String.valueOf(claims));
        return claimResolver.apply(claims);
    }

    @Override
    public Claims getAllClaimsFromToken(String token) {
        return (Claims) Jwts.parser().verifyWith(secretKey).build().parseEncryptedClaims(token);
    }

    @Override
    public boolean validateToken(String jwtToken) {
        Optional<Claims> optionalClaims = parseToken(jwtToken);
        if (optionalClaims.isPresent()) {
            Claims claims = optionalClaims.get();
            if (!isTokenExpired(claims)) {
                return true;
            } else {
                log.info(ResourceInformation.TOKEN_EXPIRED_MESSAGE);
                return false;
            }
        }
        return false;
    }

    @Override
    public Optional<Claims> parseToken(String jwtToken) {
        var jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        try {
            return Optional.of(jwtParser.parseSignedClaims(jwtToken).getPayload());
        } catch (JwtException e) {
            log.error(ResourceInformation.JWT_EXCEPTION_OCCURS_MESSAGE);
        } catch (IllegalArgumentException e) {
            log.error(ResourceInformation.ILLEGAL_ARGUMENT_OCCURS_MESSAGE);
        }
        return Optional.empty();
    }

    @Override
    public boolean isTokenExpired(Claims token) {

        final Date expiration = token.getExpiration();
        return expiration.before(new Date());
    }

    @Override
    public String generateToken(String username) {
        var currentDate = new Date();
        var jwtExpirationInMinutes = 2;
        var expiration = DateUtils.addMinutes(currentDate, jwtExpirationInMinutes);
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(ResourceInformation.TOKEN_ISSUER)
                .subject(username)
                .signWith(secretKey)
                .issuedAt(currentDate)
                .expiration(expiration)
                .compact();
    }

    @Override
    public String generateRefreshToken(String username) {
        var currentDate = new Date();
        var jwtExpirationInHours = 2;
        var expiration = DateUtils.addHours(currentDate, jwtExpirationInHours);
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(ResourceInformation.TOKEN_ISSUER)
                .subject(username)
                .signWith(secretKey)
                .issuedAt(currentDate)
                .expiration(expiration)
                .compact();
    }
}
