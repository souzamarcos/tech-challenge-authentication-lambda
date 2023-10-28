package com.fiap.burger.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fiap.burger.misc.token.TokenJwtUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class LambdaHandler implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        LambdaLogger logger = context.getLogger();

        logger.log("INICIANDO LAMBDA");
        logger.log("Context:" + context);
        logger.log("Input:" + input.toString());

        logger.log("Extraindo CPF");
        String cpf = extractCpfFromInput(input);

        logger.log("Pegando id");
        String id = getClient(cpf);

        logger.log("Gerando Token");
        String token = TokenJwtUtils.generateToken(cpf, id);

        logger.log("Montando Response");
        JSONObject response = buildResponse(token);

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

    private String getClient(String cpf) {
        return "ID-TESTE";
    }

    private JSONObject buildResponse(String token) {
        JSONObject responseBody = new JSONObject();
        responseBody.put("clientToken", token);

        JSONObject responseJson = new JSONObject();
        responseJson.put("statusCode", "200");
        responseJson.put("body", responseBody.toString());

        return responseJson;
    }

}
