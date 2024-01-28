package com.fiap.burger.gateway;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.internal.IteratorSupport;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.fiap.burger.entity.Customer;

import java.util.Optional;

public class DefaultCustomerGateway {
    private static final String DYNAMODB_CUSTOMER_TABLE = System.getenv("CUSTOMER_TABLE");
    private static final Regions REGION = Regions.US_EAST_1;
    private static final String INDEX_NAME = "cpf";


    public Optional<Customer> getCustomerId(String cpf) {
        AmazonDynamoDB dbClient = AmazonDynamoDBClientBuilder.standard().withRegion(REGION).build();
        DynamoDB dynamoDb = new DynamoDB(dbClient);
        Table table = dynamoDb.getTable(DYNAMODB_CUSTOMER_TABLE);
        Index index = table.getIndex(INDEX_NAME);

        QuerySpec querySpec = new QuerySpec();
        querySpec.withHashKey("cpf", cpf);
        IteratorSupport<Item, QueryOutcome> iterator = index.query(querySpec).iterator();
        if (iterator.hasNext()) {
            return Optional.of(new Customer(iterator.next().toJSON()));
        }
        return Optional.empty();
    }

}
