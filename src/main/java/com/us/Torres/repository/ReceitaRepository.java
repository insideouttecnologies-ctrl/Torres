package com.us.Torres.repository;

import com.us.Torres.models.Receita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceitaRepository extends JpaRepository<Receita, String> {
    List<Receita> findAllByOrderByDataEmissaoDesc();
    List<Receita> findByStatusOrderByDataEmissaoDesc(Receita.StatusReceita status);
}
