package com.fiap.burger.gateway.gateway;

import com.fiap.burger.entity.Client;

public interface ClientGateway {
    Client findById(Long id);

    Client save(Client client);

    Client findByCpf(String cpf);
}
