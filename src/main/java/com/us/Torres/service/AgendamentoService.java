package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Agendamento;
import com.us.Torres.models.Medico;
import com.us.Torres.models.Paciente;
import com.us.Torres.repository.AgendamentoRepository;
import com.us.Torres.repository.MedicoRepository;
import com.us.Torres.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public Agendamento agendar(Agendamento agendamento) {
        if (agendamento == null) {
            throw new BusinessException("Dados do agendamento são obrigatórios.");
        }
        if (agendamento.getPacienteId() == null || agendamento.getPacienteId().isBlank()) {
            throw new BusinessException("Paciente é obrigatório.");
        }
        if (agendamento.getMedicoId() == null || agendamento.getMedicoId().isBlank()) {
            throw new BusinessException("Médico é obrigatório.");
        }
        if (agendamento.getDataHora() == null) {
            agendamento.setDataHora(LocalDateTime.now());
        }

        Paciente paciente = pacienteRepository.findById(agendamento.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));
        Medico medico = medicoRepository.findById(agendamento.getMedicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado."));

        agendamento.setPacienteNome(paciente.getNome());
        agendamento.setMedicoNome(medico.getNome());

        if (agendamento.getStatus() == null) {
            agendamento.setStatus(Agendamento.StatusAgendamento.PENDENTE);
        }

        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAllByOrderByDataHoraAsc();
    }

    public Agendamento buscarPorId(String id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado."));
    }

    public Agendamento atualizarStatus(String id, Agendamento.StatusAgendamento status) {
        Agendamento agendamento = buscarPorId(id);
        if (status == null) {
            throw new BusinessException("Status do agendamento é obrigatório.");
        }
        agendamento.setStatus(status);
        return agendamentoRepository.save(agendamento);
    }

    public void cancelar(String id) {
        Agendamento agendamento = buscarPorId(id);
        agendamento.setStatus(Agendamento.StatusAgendamento.CANCELADO);
        agendamentoRepository.save(agendamento);
    }
}
