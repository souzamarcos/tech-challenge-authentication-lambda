package com.fiap.burger.misc.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fiap.burger.entity.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenJwtUtilsTest {

    @Test
    void testGenerateToken() {
        var cpf = "12345678901";
        var id = 1L;

        Client client = new Client("{\"cpf\": \"12345678901\", \"id\": 1}");

        String clientToken = TokenJwtUtils.generateToken(client);
        DecodedJWT expected = TokenJwtUtils.readToken(clientToken);
        assertEquals(cpf, expected.getClaim("cpf").asString());
        assertEquals(id, expected.getClaim("clientId").asLong());
    }

}
