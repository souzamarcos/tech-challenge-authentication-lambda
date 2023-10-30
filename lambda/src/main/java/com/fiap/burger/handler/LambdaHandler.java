package com.fiap.burger.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fiap.burger.entity.Client;
import com.fiap.burger.gateway.DefaultClientGateway;
import com.fiap.burger.misc.token.TokenJwtUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class LambdaHandler implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        JSONObject response;
        LambdaLogger logger = context.getLogger();

        try {
            logger.log("INICIANDO LAMBDA");
            logger.log("Context:" + context);
            logger.log("Input:" + input.toString());

            logger.log("Extraindo CPF");
            String cpf = extractCpfFromInput(input);

            logger.log("Pegando id a partir do cpf: " + cpf);
            Optional<Client> maybeClient = new DefaultClientGateway().getClientId(cpf);

            if (maybeClient.isPresent()) {
                logger.log("Gerando Token");
                String token = TokenJwtUtils.generateToken(maybeClient.get());

                logger.log("Montando Response Okay to cpf: " + cpf + " and id: "+ maybeClient.get().getId());
                response = buildResponseOkay(token);
            } else {
                logger.log("Montando Response Not Found to cpf: " + cpf);
                response = buildResponseNotFound(cpf);
            }
        } catch (Exception e) {
            logger.log("Montando Response Error");
            response = buildResponseError(e);
        }

        logger.log("Montando Resposta");
        OutputStreamWriter writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
        writer.write(response.toString());
        writer.close();
    }

    private String extractCpfFromInput(InputStream input) {
        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try {
            JSONObject event = (JSONObject) parser.parse(reader);
            return extractCpfFromParam(event, "pathParameters")
                .orElse(extractCpfFromParam(event, "queryStringParameters")
                    .orElse(""));
        } catch (ParseException | IOException e) {
            return "";
        }
    }

    private Optional<String> extractCpfFromParam(JSONObject event, String fieldParam) {
        return Optional.ofNullable(event.get(fieldParam)).map(o -> {
            JSONObject json = (JSONObject) o;
            return Optional.ofNullable(json.get("cpf")).map(String.class::cast);
        }).orElse(Optional.empty());
    }

    private JSONObject buildResponseOkay(String token) {
        JSONObject responseBody = new JSONObject();
        responseBody.put("clientToken", token);

        JSONObject responseJson = new JSONObject();
        responseJson.put("statusCode", "200");
        responseJson.put("body", responseBody.toString());

        return responseJson;
    }

    private JSONObject buildResponseNotFound(String cpf) {
        JSONObject responseBody = new JSONObject();
        responseBody.put("message", "No client was found with cpf: " + cpf);

        JSONObject responseJson = new JSONObject();
        responseJson.put("statusCode", "404");
        responseJson.put("body", responseBody.toString());

        return responseJson;
    }

    private JSONObject buildResponseError(Exception e) {
        JSONObject responseJson = new JSONObject();
        responseJson.put("statusCode", "400");
        responseJson.put("exception", e.getMessage());

        return responseJson;
    }

}
