package com.fiap.burger.misc.secret;

import com.fiap.burger.misc.exception.SecretAwsException;
import com.fiap.burger.misc.token.TokenJwtSecret;
import com.google.gson.Gson;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

public class SecretUtils {
    private static final String REGION_US_EAST = "us-east-1";

    private SecretUtils() {

    }

    public static TokenJwtSecret getTokenJwtSecret() {
        TokenJwtSecret token = new Gson().fromJson(SecretUtils.getSecret("token-jwt-secret"), TokenJwtSecret.class);
        token.isValid();
        return token;
    }

    public static String getSecret(String secretName) {
        try {
            Region region = Region.of(REGION_US_EAST);

            SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();

            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

            GetSecretValueResponse getSecretValueResponse;
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);

            return getSecretValueResponse.secretString();
        } catch (Exception e) {
            throw new SecretAwsException("Error while trying to read secret " + secretName + ": " + e.getMessage());
        }

    }
}
