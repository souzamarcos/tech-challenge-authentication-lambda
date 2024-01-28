package com.fiap.burger.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Customer {

    private final String cpf;
    private final String id;

    public String getCpf() {
        return cpf;
    }

    public String getId() {
        return id;
    }

    public Customer(String json) {
        Gson gson = new Gson();
        Customer request = gson.fromJson(json, Customer.class);
        this.cpf = request.getCpf();
        this.id = request.getId();
    }

    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
