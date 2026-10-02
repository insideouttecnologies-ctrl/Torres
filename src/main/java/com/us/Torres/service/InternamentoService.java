package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.*;
import com.us.Torres.models.internamento.EvolucaoRequest;
import com.us.Torres.models.internamento.InternamentoRequest;
import com.us.Torres.models.internamento.LeitoDTO;
import com.us.Torres.repository.EvolucaoClinicaRepository;
import com.us.Torres.repository.InternamentoRepository;
import com.us.Torres.repository.LeitoRepository;
import com.us.Torres.repository.MedicoRepository;
import com.us.Torres.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternamentoService {

    private final InternamentoRepository internamentoRepository;
    private final LeitoRepository leitoRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final EvolucaoClinicaRepository evolucaoClinicaRepository;

    public List<LeitoDTO> listarLeitos() {
        return leitoRepository.findAll().stream().map(this::mapearLeito).toList();
    }

    public Internamento admitirPaciente(InternamentoRequest request) {
        if (request == null || request.getPacienteId() == null || request.getLeitoId() == null) {
            throw new BusinessException("Paciente e leito são obrigatórios para a admissão.");
        }
        if (request.getDiagnosticoAdmissao() == null || request.getDiagnosticoAdmissao().isBlank()) {
            throw new BusinessException("Diagnóstico de admissão é obrigatório.");
        }
        if (request.getMedicoId() == null || request.getMedicoId().isBlank()) {
            throw new BusinessException("Médico responsável é obrigatório para a admissão.");
        }

        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));

        internamentoRepository.findFirstByPacienteIdAndStatusOrderByDataAdmissaoDesc(
                        paciente.getId(),
                        Internamento.StatusInternamento.ATIVO)
                .ifPresent(atual -> {
                    throw new BusinessException("Paciente já possui um internamento ativo.");
                });

        Leito leito = leitoRepository.findById(request.getLeitoId())
                .orElseThrow(() -> new ResourceNotFoundException("Leito não encontrado."));

        if (request.getAla() != null && !request.getAla().isBlank()
                && !request.getAla().equalsIgnoreCase(leito.getAla())) {
            throw new BusinessException("A ala informada não corresponde ao leito selecionado.");
        }

        if (leito.getStatus() != Leito.StatusLeito.DISPONIVEL) {
            throw new BusinessException("O leito informado não está disponível para admissão.");
        }

        leito.setStatus(Leito.StatusLeito.OCUPADO);
        leito.setPacienteAtualId(paciente.getId());
        leito.setPacienteAtualNome(paciente.getNome());
        leito.setDataAdmissao(LocalDateTime.now());
        leito.setMedicoAssistente(request.getMedicoId());
        leitoRepository.save(leito);

        Internamento internamento = Internamento.builder()
                .pacienteId(paciente.getId())
                .pacienteNome(paciente.getNome())
                .leitoId(leito.getId())
                .leitoCodigo(leito.getCodigo())
                .ala(leito.getAla())
                .medicoResponsavelId(request.getMedicoId())
                .medicoResponsavelNome(obterNomeMedico(request.getMedicoId()))
                .diagnosticoAdmissao(request.getDiagnosticoAdmissao())
                .dataAdmissao(LocalDateTime.now())
                .status(Internamento.StatusInternamento.ATIVO)
                .build();

        return internamentoRepository.save(internamento);
    }

    public EvolucaoClinica registrarEvolucao(String internamentoId, EvolucaoRequest request) {
        Internamento internamento = internamentoRepository.findById(internamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Internamento não encontrado."));

        if (internamento.getStatus() != Internamento.StatusInternamento.ATIVO) {
            throw new BusinessException("Só é possível registrar evolução em internamentos ativos.");
        }

        EvolucaoClinica evolucao = EvolucaoClinica.builder()
                .internamentoId(internamento.getId())
                .profissionalId(request.getProfissionalId())
                .profissionalNome(request.getProfissionalNome())
                .sinaisVitais(request.getSinaisVitais())
                .dieta(request.getDieta())
                .acessoVenoso(request.getAcessoVenoso())
                .conduta(request.getConduta())
                .dataHora(LocalDateTime.now())
                .build();

        return evolucaoClinicaRepository.save(evolucao);
    }

    public Internamento atualizarStatus(String internamentoId, Internamento.StatusInternamento status) {
        Internamento internamento = internamentoRepository.findById(internamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Internamento não encontrado."));

        if (status == null) {
            throw new BusinessException("Status do internamento é obrigatório.");
        }

        validarTransicao(internamento.getStatus(), status);
        internamento.setStatus(status);
        return internamentoRepository.save(internamento);
    }

    public Internamento darAlta(String internamentoId) {
        Internamento internamento = internamentoRepository.findById(internamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Internamento não encontrado."));

        if (internamento.getStatus() != Internamento.StatusInternamento.ATIVO) {
            throw new BusinessException("Só é possível dar alta de um internamento ativo.");
        }

        internamento.setStatus(Internamento.StatusInternamento.ALTA_CONCEDIDA);
        internamento.setDataAlta(LocalDateTime.now());
        internamentoRepository.save(internamento);

        Leito leito = leitoRepository.findById(internamento.getLeitoId())
                .orElseThrow(() -> new ResourceNotFoundException("Leito do internamento não encontrado."));

        leito.setStatus(Leito.StatusLeito.DISPONIVEL);
        leito.setPacienteAtualId(null);
        leito.setPacienteAtualNome(null);
        leito.setDataAdmissao(null);
        leito.setMedicoAssistente(null);
        leitoRepository.save(leito);

        return internamento;
    }

    public Internamento transferirPaciente(String internamentoId, String novoLeitoId) {
        Internamento internamento = internamentoRepository.findById(internamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Internamento não encontrado."));

        if (internamento.getStatus() != Internamento.StatusInternamento.ATIVO) {
            throw new BusinessException("Só é possível transferir um internamento ativo.");
        }
        if (novoLeitoId == null || novoLeitoId.isBlank()) {
            throw new BusinessException("Leito de destino é obrigatório para a transferência.");
        }

        Leito destino = leitoRepository.findById(novoLeitoId)
                .orElseThrow(() -> new ResourceNotFoundException("Leito de destino não encontrado."));

        if (destino.getStatus() != Leito.StatusLeito.DISPONIVEL && !destino.getId().equals(internamento.getLeitoId())) {
            throw new BusinessException("O leito de destino não está disponível para transferência.");
        }

        Leito origem = leitoRepository.findById(internamento.getLeitoId())
                .orElseThrow(() -> new ResourceNotFoundException("Leito atual do internamento não encontrado."));

        origem.setStatus(Leito.StatusLeito.DISPONIVEL);
        origem.setPacienteAtualId(null);
        origem.setPacienteAtualNome(null);
        origem.setDataAdmissao(null);
        origem.setMedicoAssistente(null);
        leitoRepository.save(origem);

        destino.setStatus(Leito.StatusLeito.OCUPADO);
        destino.setPacienteAtualId(internamento.getPacienteId());
        destino.setPacienteAtualNome(internamento.getPacienteNome());
        destino.setDataAdmissao(LocalDateTime.now());
        destino.setMedicoAssistente(internamento.getMedicoResponsavelId());
        leitoRepository.save(destino);

        internamento.setLeitoId(destino.getId());
        internamento.setLeitoCodigo(destino.getCodigo());
        internamento.setAla(destino.getAla());
        internamento.setStatus(Internamento.StatusInternamento.TRANSFERIDO);
        return internamentoRepository.save(internamento);
    }

    private void validarTransicao(Internamento.StatusInternamento atual, Internamento.StatusInternamento novo) {
        if (novo == atual) {
            return;
        }
        if (atual == null) {
            if (novo == Internamento.StatusInternamento.ATIVO) {
                return;
            }
            throw new BusinessException("Transição de status inválida: " + novo + ".");
        }
        if (atual != Internamento.StatusInternamento.ATIVO) {
            throw new BusinessException("transição de status inválida: não é possível alterar um internamento que não está ativo.");
        }
        if (novo == Internamento.StatusInternamento.ALTA_CONCEDIDA
                || novo == Internamento.StatusInternamento.TRANSFERIDO
                || novo == Internamento.StatusInternamento.OBITO) {
            return;
        }
        throw new BusinessException("Transição de status inválida: " + atual + " -> " + novo + ".");
    }

    private LeitoDTO mapearLeito(Leito leito) {
        LeitoDTO.PacienteAtualDTO pacienteAtual = null;

        if (leito.getPacienteAtualId() != null) {
            Paciente paciente = pacienteRepository.findById(leito.getPacienteAtualId()).orElse(null);
            if (paciente != null) {
                pacienteAtual = LeitoDTO.PacienteAtualDTO.builder()
                        .id(paciente.getId())
                        .nome(paciente.getNome())
                        .dataAdmissao(leito.getDataAdmissao())
                        .medicoAssistente(leito.getMedicoAssistente())
                        .build();
            }
        }

        return LeitoDTO.builder()
                .id(leito.getId())
                .codigo(leito.getCodigo())
                .ala(leito.getAla())
                .status(leito.getStatus())
                .pacienteAtual(pacienteAtual)
                .build();
    }

    private String obterNomeMedico(String medicoId) {
        if (medicoId == null) {
            return null;
        }

        return medicoRepository.findById(medicoId)
                .map(Medico::getNome)
                .orElse(null);
    }
}
