package com.us.Torres.repository;

import com.us.Torres.models.LaudoLaboratorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LaudoLaboratorialRepository extends JpaRepository<LaudoLaboratorial, String> {
    Optional<LaudoLaboratorial> findByProtocolo(String protocolo);
}
