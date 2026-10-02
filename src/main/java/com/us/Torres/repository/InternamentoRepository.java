package com.us.Torres.repository;

import com.us.Torres.models.Internamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InternamentoRepository extends JpaRepository<Internamento, String> {
    List<Internamento> findByStatusOrderByDataAdmissaoDesc(Internamento.StatusInternamento status);
    List<Internamento> findAllByOrderByDataAdmissaoDesc();
    Optional<Internamento> findFirstByPacienteIdAndStatusOrderByDataAdmissaoDesc(String pacienteId, Internamento.StatusInternamento status);
}
