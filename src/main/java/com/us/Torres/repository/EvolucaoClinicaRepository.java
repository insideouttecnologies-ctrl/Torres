package com.us.Torres.repository;

import com.us.Torres.models.EvolucaoClinica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvolucaoClinicaRepository extends JpaRepository<EvolucaoClinica, String> {
    List<EvolucaoClinica> findByInternamentoIdOrderByDataHoraDesc(String internamentoId);
}
