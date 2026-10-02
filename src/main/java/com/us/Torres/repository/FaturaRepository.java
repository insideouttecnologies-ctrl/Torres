package com.us.Torres.repository;

import com.us.Torres.models.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaturaRepository extends JpaRepository<Fatura, String> {
    List<Fatura> findAllByOrderByDataEmissaoDesc();
    Optional<Fatura> findByNumeroFatura(String numeroFatura);
}
