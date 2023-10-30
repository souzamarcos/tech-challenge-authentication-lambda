package com.fiap.burger.gateway;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.fiap.burger.entity.Client;

import java.util.Optional;

public class DefaultClientGateway {
    private static final String DYNAMODB_CLIENT_TABLE = System.getenv("CLIENTS_TABLE");
    private static final Regions REGION = Regions.US_EAST_1;


    public Optional<Client> getClientId(String cpf) {
        AmazonDynamoDB dbClient = AmazonDynamoDBClientBuilder.standard().withRegion(REGION).build();
        DynamoDB dynamoDb = new DynamoDB(dbClient);
        Item result = dynamoDb.getTable(DYNAMODB_CLIENT_TABLE).getItem("cpf", cpf);
        if(result != null) {
            return Optional.of(new Client(result.toJSON()));
        }
        return Optional.empty();
    }

}
