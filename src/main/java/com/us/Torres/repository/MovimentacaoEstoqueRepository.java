package com.us.Torres.repository;

import com.us.Torres.models.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, String> {
    List<MovimentacaoEstoque> findByMedicamentoIdOrderByDataHoraDesc(String medicamentoId);
}
