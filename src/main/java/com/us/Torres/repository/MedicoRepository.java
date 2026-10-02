package com.us.Torres.repository;

import com.us.Torres.models.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, String> {
    List<Medico> findAllByOrderByNomeAsc();
    List<Medico> findByAtivoOrderByNomeAsc(boolean ativo);
    Optional<Medico> findByNumeroOrdem(String numeroOrdem);
    Optional<Medico> findByEspecialidade(String especialidade);
    Optional<Medico> findByTelefone(String telefone);
    boolean existsByNumeroOrdem(String numeroOrdem);
    boolean existsByTelefone(String telefone);
}

