package com.fiap.burger.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Client {

    private final String cpf;
    private final Long id;

    public String getCpf() {
        return cpf;
    }

    public Long getId() {
        return id;
    }

    public Client(String json) {
        Gson gson = new Gson();
        Client request = gson.fromJson(json, Client.class);
        this.cpf = request.getCpf();
        this.id = request.getId();
    }

    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
