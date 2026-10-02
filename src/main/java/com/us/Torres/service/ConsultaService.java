package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Consulta;
import com.us.Torres.models.Medico;
import com.us.Torres.models.Paciente;
import com.us.Torres.repository.ConsultaRepository;
import com.us.Torres.repository.MedicoRepository;
import com.us.Torres.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public Consulta criar(Consulta consulta) {
        if (consulta == null) {
            throw new BusinessException("Dados da consulta são obrigatórios.");
        }

        if (consulta.getPacienteId() == null || consulta.getPacienteId().isBlank()) {
            throw new BusinessException("Paciente é obrigatório.");
        }

        if (consulta.getMedicoId() == null || consulta.getMedicoId().isBlank()) {
            throw new BusinessException("Médico é obrigatório.");
        }

        if (consulta.getMotivo() == null || consulta.getMotivo().isBlank()) {
            throw new BusinessException("Motivo da consulta é obrigatório.");
        }

        Paciente paciente = pacienteRepository.findById(consulta.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));

        Medico medico = medicoRepository.findById(consulta.getMedicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado."));

        consulta.setPacienteNome(paciente.getNome());
        consulta.setMedicoNome(medico.getNome());

        if (consulta.getEspecialidade() == null || consulta.getEspecialidade().isBlank()) {
            consulta.setEspecialidade(medico.getEspecialidade());
        }

        if (consulta.getDataHora() == null) {
            consulta.setDataHora(LocalDateTime.now());
        }

        if (consulta.getStatus() == null) {
            consulta.setStatus(Consulta.StatusConsulta.AGENDADA);
        }

        return consultaRepository.save(consulta);
    }

    public List<Consulta> listarTodos() {
        return consultaRepository.findAllByOrderByDataHoraAsc();
    }

    public Consulta buscarPorId(String id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada."));
    }

    public Consulta atualizarStatus(String id, Consulta.StatusConsulta status) {
        Consulta consulta = buscarPorId(id);

        if (status == null) {
            throw new BusinessException("Status da consulta é obrigatório.");
        }

        validarTransicao(consulta.getStatus(), status);
        consulta.setStatus(status);
        return consultaRepository.save(consulta);
    }

    public void cancelar(String id) {
        Consulta consulta = buscarPorId(id);
        validarTransicao(consulta.getStatus(), Consulta.StatusConsulta.CANCELADA);
        consulta.setStatus(Consulta.StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);
    }

    private void validarTransicao(Consulta.StatusConsulta atual, Consulta.StatusConsulta novo) {
        if (novo == atual) {
            return;
        }
        if (atual == null) {
            if (novo == Consulta.StatusConsulta.AGENDADA) {
                return;
            }
            throw new BusinessException("Transição de status inválida: " + novo + ".");
        }
        if (atual == Consulta.StatusConsulta.CANCELADA || atual == Consulta.StatusConsulta.CONCLUIDA) {
            throw new BusinessException("transição de status inválida: não é possível alterar uma consulta concluída ou cancelada.");
        }
        if (atual == Consulta.StatusConsulta.AGENDADA
                && (novo == Consulta.StatusConsulta.EM_ANDAMENTO || novo == Consulta.StatusConsulta.CANCELADA)) {
            return;
        }
        if (atual == Consulta.StatusConsulta.EM_ANDAMENTO
                && (novo == Consulta.StatusConsulta.CONCLUIDA || novo == Consulta.StatusConsulta.CANCELADA)) {
            return;
        }
        throw new BusinessException("Transição de status inválida: " + atual + " -> " + novo + ".");
    }
}
