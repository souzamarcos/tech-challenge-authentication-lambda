package com.fiap.burger.handler;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LambdaHandlerTest {

    @InjectMocks
    LambdaHandler lambdaHandler = new LambdaHandler();

    @Test
    public void testLambdaHandler() throws IOException {
        InputStream input = new ByteArrayInputStream("{\"pathParameters\":{\"cpf\":\"12345678901\"}}".getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = OutputStream.nullOutputStream();
        lambdaHandler.handleRequest(input, outputStream, new TestContext());

        assertNotNull(outputStream);
    }

}
