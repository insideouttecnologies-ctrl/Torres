package com.us.Torres.repository;

import com.us.Torres.models.Ala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlaRepository extends JpaRepository<Ala, String> {
    List<Ala> findAllByOrderByNomeAsc();
    Optional<Ala> findByNome(String nome);
    boolean existsByNome(String nome);
}
