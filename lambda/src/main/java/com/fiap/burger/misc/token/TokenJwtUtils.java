package com.fiap.burger.misc.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fiap.burger.entity.Client;

import java.util.Date;
import java.util.UUID;

public class TokenJwtUtils {
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("app-pos_tech");
    private static final String ISSUER = "Pos-Tech FIAP - Burger";

    private TokenJwtUtils() {

    }

    public static String generateToken(Client client) {
        return JWT.create()
            .withIssuer(ISSUER)
            .withSubject("Audience")
            .withClaim("clientId", client.getId())
            .withClaim("cpf", client.getCpf())
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + 5000L))
            .withJWTId(UUID.randomUUID()
                .toString())
            .sign(ALGORITHM);
    }

    public static DecodedJWT readToken(String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM)
            .withIssuer(ISSUER)
            .build();

        return verifier.verify(token);
    }

}
