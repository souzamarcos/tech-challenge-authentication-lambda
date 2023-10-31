package com.fiap.burger.handler;

import com.fiap.burger.misc.secret.SecretUtils;
import com.fiap.burger.misc.token.TokenJwtSecret;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LambdaHandlerTest {
    private static final String TOKEN_SECRET = "TEST-SECRET";
    private static final String TOKEN_ISSUER = "TEST-ISSUER";

    @InjectMocks
    LambdaHandler lambdaHandler = new LambdaHandler();

    @Test
    void testLambdaHandler() throws IOException {
        InputStream input = new ByteArrayInputStream("{\"pathParameters\":{\"cpf\":\"12345678901\"}}".getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = OutputStream.nullOutputStream();

        try (MockedStatic<SecretUtils> utilities = Mockito.mockStatic(SecretUtils.class)) {
            utilities.when(SecretUtils::getTokenJwtSecret).thenReturn(new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER));
            lambdaHandler.handleRequest(input, outputStream, new TestContext());
        }

        assertNotNull(outputStream);
    }

}
