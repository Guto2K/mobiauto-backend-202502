package com.mobiauto.manager.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiauto.manager.models.Revenda;

public interface RevendaRepository extends JpaRepository<Revenda, Long> {
    Optional<Revenda> findByCnpj(String cnpj);
    boolean existsByCnpj(String cnpj);
}
