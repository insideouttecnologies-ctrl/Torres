package com.us.Torres.repository;

import com.us.Torres.models.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, String> {

    List<LogAuditoria> findAllByOrderByDataHoraDesc();

    List<LogAuditoria> findByUsuarioIdOrderByDataHoraDesc(String usuarioId);
}
