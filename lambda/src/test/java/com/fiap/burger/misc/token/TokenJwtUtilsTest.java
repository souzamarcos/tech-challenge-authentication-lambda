package com.fiap.burger.misc.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenJwtUtilsTest {

    @Test
    public void testGenerateToken() {
        var cpf = "12345678901";
        var id = "1L";

        String clientToken = TokenJwtUtils.generateToken(cpf, id);
        DecodedJWT expected = TokenJwtUtils.readToken(clientToken);
        assertEquals(cpf, expected.getClaim("cpf").asString());
        assertEquals(id, expected.getClaim("clientId").asString());
    }

}
