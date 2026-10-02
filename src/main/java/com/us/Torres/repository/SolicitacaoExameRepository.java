package com.us.Torres.repository;

import com.us.Torres.models.SolicitacaoExame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitacaoExameRepository extends JpaRepository<SolicitacaoExame, String> {
    Optional<SolicitacaoExame> findByProtocolo(String protocolo);
    List<SolicitacaoExame> findAllByOrderByDataSolicitacaoDesc();
}
