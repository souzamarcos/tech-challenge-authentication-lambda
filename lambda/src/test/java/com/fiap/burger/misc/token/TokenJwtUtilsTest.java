package com.fiap.burger.misc.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fiap.burger.entity.Client;
import com.fiap.burger.misc.secret.SecretUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenJwtUtilsTest {
    private static final String TOKEN_SECRET = "TEST-SECRET";
    private static final String TOKEN_ISSUER = "TEST-ISSUER";

    @Test
    void testGenerateToken() {
        var cpf = "12345678901";
        var id = 1L;

        Client client = new Client("{\"cpf\": \"12345678901\", \"id\": 1}");

        try (MockedStatic<SecretUtils> utilities = Mockito.mockStatic(SecretUtils.class)) {
            utilities.when(SecretUtils::getTokenJwtSecret).thenReturn(new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER));
            String clientToken = TokenJwtUtils.generateToken(client);
            DecodedJWT expected = TokenJwtUtils.readToken(clientToken);
            assertEquals(cpf, expected.getClaim("cpf").asString());
            assertEquals(id, expected.getClaim("clientId").asLong());
        }
    }

}
