package com.us.Torres.repository;

import com.us.Torres.models.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, String> {
    List<Consulta> findAllByOrderByDataHoraAsc();
    List<Consulta> findByPacienteIdOrderByDataHoraAsc(String pacienteId);
    List<Consulta> findByMedicoIdOrderByDataHoraAsc(String medicoId);
}
