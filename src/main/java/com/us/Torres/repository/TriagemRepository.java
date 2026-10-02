package com.us.Torres.repository;

import com.us.Torres.models.Triagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TriagemRepository extends JpaRepository<Triagem, String> {
    List<Triagem> findByStatusOrderByDataHoraAsc(Triagem.StatusTriagem status);
    List<Triagem> findAllByOrderByDataHoraAsc();
}
