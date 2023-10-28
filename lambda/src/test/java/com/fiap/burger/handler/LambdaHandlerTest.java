package com.fiap.burger.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.xmlunit.builder.Input;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
