package com.us.Torres.repository;

import com.us.Torres.models.Leito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeitoRepository extends JpaRepository<Leito, String> {
    List<Leito> findAllByOrderByCodigoAsc();
    List<Leito> findByStatusOrderByCodigoAsc(Leito.StatusLeito status);
    Optional<Leito> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}
