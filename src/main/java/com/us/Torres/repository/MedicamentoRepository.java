package com.us.Torres.repository;

import com.us.Torres.models.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicamentoRepository extends JpaRepository<Medicamento, String> {
    Optional<Medicamento> findByCodigoItem(String codigoItem);
    List<Medicamento> findAllByOrderByNomeAsc();
    List<Medicamento> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);
    List<Medicamento> findByAtivoTrueOrderByNomeAsc();
}
