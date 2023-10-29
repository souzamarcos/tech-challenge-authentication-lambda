package com.fiap.burger.gateway.dao;


import com.fiap.burger.gateway.model.ClientJPA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientDAO extends JpaRepository<ClientJPA, Long> {
    Optional<ClientJPA> findByCpf(String cpf);
}

