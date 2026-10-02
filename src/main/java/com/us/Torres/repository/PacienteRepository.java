package com.us.Torres.repository;

import com.us.Torres.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, String> {
    List<Paciente> findAllByOrderByNomeAsc();

    List<Paciente> findByStatusOrderByNomeAsc(String status);

    List<Paciente> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    List<Paciente> findByNomeContainingIgnoreCaseAndStatusOrderByNomeAsc(String nome, String status);

    Optional<Paciente> findByBi(String bi);

    Optional<Paciente> findByTelefone(String telefone);
}