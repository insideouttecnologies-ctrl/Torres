package com.us.Torres.repository;

import com.us.Torres.models.Enfermeiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnfermeiroRepository extends JpaRepository<Enfermeiro, String> {
    List<Enfermeiro> findAllByOrderByNomeAsc();
    List<Enfermeiro> findByAtivoOrderByNomeAsc(boolean ativo);
    Optional<Enfermeiro> findByNumeroRegistro(String numeroRegistro);
    Optional<Enfermeiro> findByEmail(String email);
    boolean existsByNumeroRegistro(String numeroRegistro);
    boolean existsByEmail(String email);
}
