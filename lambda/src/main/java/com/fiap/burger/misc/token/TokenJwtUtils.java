package com.fiap.burger.misc.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fiap.burger.entity.Client;
import com.fiap.burger.misc.secret.SecretUtils;

import java.util.Date;
import java.util.UUID;

public class TokenJwtUtils {

    private static final Long ADD_TIME_EXPIRATION = (1000L * 60L * 10L);

    private TokenJwtUtils() {

    }

    public static String generateToken(Client client) {
        TokenJwtSecret jwtSecret = SecretUtils.getTokenJwtSecret();
        return JWT.create()
            .withIssuer(jwtSecret.getIssuer())
            .withSubject("Audience")
            .withClaim("clientId", client.getId())
            .withClaim("cpf", client.getCpf())
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + ADD_TIME_EXPIRATION))
            .withJWTId(UUID.randomUUID()
                .toString())
            .sign(buildAlgorithm(jwtSecret.getSecret()));
    }

    public static DecodedJWT readToken(String token) {
        TokenJwtSecret jwtSecret = SecretUtils.getTokenJwtSecret();
        JWTVerifier verifier = JWT.require(buildAlgorithm(jwtSecret.getSecret()))
            .withIssuer(jwtSecret.getIssuer())
            .build();

        return verifier.verify(token);
    }

    private static Algorithm buildAlgorithm(String secret) {
        return Algorithm.HMAC256(secret);
    }

}
