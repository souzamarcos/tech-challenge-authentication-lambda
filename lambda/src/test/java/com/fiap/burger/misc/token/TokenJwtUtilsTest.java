package com.fiap.burger.misc.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fiap.burger.entity.Customer;
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
        var id = "1";

        Customer customer = new Customer("{\"cpf\": \"12345678901\", \"id\": \"1\"}");

        try (MockedStatic<SecretUtils> utilities = Mockito.mockStatic(SecretUtils.class)) {
            utilities.when(SecretUtils::getTokenJwtSecret).thenReturn(new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER));
            String customerToken = TokenJwtUtils.generateToken(customer);
            DecodedJWT expected = TokenJwtUtils.readToken(customerToken);
            assertEquals(cpf, expected.getClaim("cpf").asString());
            assertEquals(id, expected.getClaim("customerId").asString());
        }
    }

}
