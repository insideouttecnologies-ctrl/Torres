package com.us.Torres.repository;

import com.us.Torres.models.ConfiguracaoHospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracaoHospitalRepository extends JpaRepository<ConfiguracaoHospital, String> {

    Optional<ConfiguracaoHospital> findFirstByOrderByAtualizadoEmDesc();
}
