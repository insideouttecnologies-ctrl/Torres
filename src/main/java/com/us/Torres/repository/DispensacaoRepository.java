package com.us.Torres.repository;

import com.us.Torres.models.Dispensacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DispensacaoRepository extends JpaRepository<Dispensacao, String> {
    List<Dispensacao> findAllByOrderByDataDispensacaoDesc();
    Optional<Dispensacao> findByReciboId(String reciboId);
}
