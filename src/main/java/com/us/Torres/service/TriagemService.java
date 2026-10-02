package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Paciente;
import com.us.Torres.models.Triagem;
import com.us.Torres.repository.PacienteRepository;
import com.us.Torres.repository.TriagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TriagemService {

    private final TriagemRepository triagemRepository;
    private final PacienteRepository pacienteRepository;

    public Triagem salvarTriagem(Triagem triagem) {
        if (triagem == null || triagem.getPacienteId() == null || triagem.getPacienteId().isBlank()) {
            throw new BusinessException("Paciente da triagem é obrigatório.");
        }

        Paciente paciente = pacienteRepository.findById(triagem.getPacienteId())
                .orElseThrow(() -> new BusinessException("Paciente não localizado para triagem."));

        triagem.setPacienteNome(paciente.getNome());
        triagem.setDataHora(triagem.getDataHora() == null ? LocalDateTime.now() : triagem.getDataHora());

        if (triagem.getClassificacaoRisco() == null) {
            triagem.setClassificacaoRisco(Triagem.ClassificacaoRisco.AMARELO);
        }

        if (triagem.getStatus() == null) {
            triagem.setStatus(Triagem.StatusTriagem.AGUARDANDO_ATENDIMENTO);
        }

        if (triagem.getTempoLimiteMinutos() == null) {
            triagem.setTempoLimiteMinutos(calcularTempoLimite(triagem.getClassificacaoRisco()));
        }

        if (triagem.getSenha() == null || triagem.getSenha().isBlank()) {
            triagem.setSenha(gerarSenha(triagem.getClassificacaoRisco()));
        }

        return triagemRepository.save(triagem);
    }

    public List<Triagem> listarFila() {
        return triagemRepository.findAll().stream()
                .filter(t -> t.getStatus() != null && t.getStatus() != Triagem.StatusTriagem.CONCLUIDA)
                .filter(t -> t.getStatus() != Triagem.StatusTriagem.CANCELADA)
                .sorted(Comparator
                        .comparingInt(this::prioridadeRisco).reversed()
                        .thenComparing(Triagem::getDataHora))
                .toList();
    }

    public Triagem atualizarStatus(String id, Triagem.StatusTriagem status) {
        Triagem triagem = triagemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Triagem não encontrada."));

        if (status == null) {
            throw new BusinessException("Status da triagem é obrigatório.");
        }

        validarTransicao(triagem.getStatus(), status);
        triagem.setStatus(status);
        return triagemRepository.save(triagem);
    }

    public void cancelar(String id) {
        Triagem triagem = triagemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Triagem não encontrada."));
        validarTransicao(triagem.getStatus(), Triagem.StatusTriagem.CANCELADA);
        triagem.setStatus(Triagem.StatusTriagem.CANCELADA);
        triagemRepository.save(triagem);
    }

    private void validarTransicao(Triagem.StatusTriagem atual, Triagem.StatusTriagem novo) {
        if (novo == atual) {
            return;
        }
        if (atual == null) {
            if (novo == Triagem.StatusTriagem.AGUARDANDO_ATENDIMENTO) {
                return;
            }
            throw new BusinessException("Transição de status inválida: " + novo + ".");
        }
        if (atual == Triagem.StatusTriagem.CANCELADA || atual == Triagem.StatusTriagem.CONCLUIDA) {
            throw new BusinessException("transição de status inválida: não é possível alterar uma triagem concluída ou cancelada.");
        }
        if (atual == Triagem.StatusTriagem.AGUARDANDO_ATENDIMENTO
                && (novo == Triagem.StatusTriagem.EM_ATENDIMENTO || novo == Triagem.StatusTriagem.CANCELADA)) {
            return;
        }
        if (atual == Triagem.StatusTriagem.EM_ATENDIMENTO
                && (novo == Triagem.StatusTriagem.CONCLUIDA || novo == Triagem.StatusTriagem.CANCELADA)) {
            return;
        }
        throw new BusinessException("Transição de status inválida: " + atual + " -> " + novo + ".");
    }

    private int prioridadeRisco(Triagem triagem) {
        if (triagem == null || triagem.getClassificacaoRisco() == null) {
            return 0;
        }

        return switch (triagem.getClassificacaoRisco()) {
            case VERMELHO -> 5;
            case LARANJA -> 4;
            case AMARELO -> 3;
            case VERDE -> 2;
            case AZUL -> 1;
            default -> 0;
        };
    }

    private Integer calcularTempoLimite(Triagem.ClassificacaoRisco risco) {
        if (risco == null) {
            return 60;
        }

        return switch (risco) {
            case VERMELHO -> 0;
            case LARANJA -> 10;
            case AMARELO -> 60;
            case VERDE -> 120;
            case AZUL -> 240;
            default -> 60;
        };
    }

    private String gerarSenha(Triagem.ClassificacaoRisco risco) {
        Triagem.ClassificacaoRisco classificacao = risco == null ? Triagem.ClassificacaoRisco.AMARELO : risco;

        String codigo = switch (classificacao) {
            case VERMELHO -> "E";
            case LARANJA -> "L";
            case AMARELO -> "A";
            case VERDE -> "V";
            case AZUL -> "B";
            default -> "A";
        };

        long sequencia = triagemRepository.findAll().stream()
                .filter(t -> t.getClassificacaoRisco() == classificacao)
                .count() + 1;

        return codigo + "-" + String.format("%02d", sequencia);
    }
}
