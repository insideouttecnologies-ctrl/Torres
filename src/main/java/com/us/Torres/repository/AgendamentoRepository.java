package com.us.Torres.repository;

import com.us.Torres.models.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, String> {
    List<Agendamento> findAllByOrderByDataHoraAsc();
    List<Agendamento> findByPacienteIdOrderByDataHoraAsc(String pacienteId);
    List<Agendamento> findByMedicoIdOrderByDataHoraAsc(String medicoId);
    List<Agendamento> findByStatusOrderByDataHoraAsc(Agendamento.StatusAgendamento status);
}
