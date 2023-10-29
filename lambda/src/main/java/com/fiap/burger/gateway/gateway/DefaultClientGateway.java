package com.fiap.burger.gateway.gateway;

import com.fiap.burger.entity.Client;
import com.fiap.burger.gateway.dao.ClientDAO;
import com.fiap.burger.gateway.model.ClientJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DefaultClientGateway implements ClientGateway {

    @Autowired
    private ClientDAO clientDAO;

    @Override
    public Client findById(Long id) {
        return clientDAO.findById(id).map(ClientJPA::toEntity).orElse(null);
    }

    @Override
    public Client findByCpf(String cpf) {
        Optional<Client> client = clientDAO.findByCpf(cpf).map(ClientJPA::toEntity);
        return client.orElse(null);
    }

    @Override
    public Client save(Client client) {
        return clientDAO.save(ClientJPA.toJPA(client)).toEntity();
    }

}
