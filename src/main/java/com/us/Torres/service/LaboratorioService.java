package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.LaudoLaboratorial;
import com.us.Torres.models.Paciente;
import com.us.Torres.models.SolicitacaoExame;
import com.us.Torres.models.laboratorio.LaudoRequest;
import com.us.Torres.repository.LaudoLaboratorialRepository;
import com.us.Torres.repository.PacienteRepository;
import com.us.Torres.repository.SolicitacaoExameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LaboratorioService {

    private final SolicitacaoExameRepository solicitacaoExameRepository;
    private final LaudoLaboratorialRepository laudoLaboratorialRepository;
    private final PacienteRepository pacienteRepository;

    public SolicitacaoExame solicitarExame(SolicitacaoExame exame) {
        if (exame == null || exame.getPacienteId() == null || exame.getTipoExame() == null) {
            throw new BusinessException("Paciente e tipo de exame são obrigatórios.");
        }

        Paciente paciente = pacienteRepository.findById(exame.getPacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado para solicitação de exame."));

        exame.setPacienteNome(paciente.getNome());
        exame.setDataSolicitacao(exame.getDataSolicitacao() == null ? LocalDateTime.now() : exame.getDataSolicitacao());
        exame.setProtocolo(exame.getProtocolo() == null || exame.getProtocolo().isBlank() ? "#EX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase() : exame.getProtocolo());

        if (exame.getStatus() == null) {
            exame.setStatus(SolicitacaoExame.StatusExame.AGUARDANDO_COLETA);
        }

        if (exame.getPrioridade() == null) {
            exame.setPrioridade(SolicitacaoExame.PrioridadeExame.ROTINA);
        }

        return solicitacaoExameRepository.save(exame);
    }

    public List<SolicitacaoExame> listarExames() {
        return solicitacaoExameRepository.findAllByOrderByDataSolicitacaoDesc();
    }

    public LaudoLaboratorial registrarLaudo(String protocolo, LaudoRequest request) {
        SolicitacaoExame solicitacao = solicitacaoExameRepository.findByProtocolo(protocolo)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação de exame não encontrada."));

        if (request == null) {
            throw new BusinessException("Laudo não informado.");
        }

        String parametrosJson = request.getParametros() == null ? "[]" : request.getParametros().toString();
        String hash = "LAUDO-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();

        LaudoLaboratorial laudo = LaudoLaboratorial.builder()
                .exameId(solicitacao.getId())
                .protocolo(solicitacao.getProtocolo())
                .responsavelTecnico(request.getResponsavelTecnico())
                .numeroRegistroCRF(request.getNumeroRegistroCRF())
                .parametrosJson(parametrosJson)
                .conclusao(request.getConclusao())
                .dataLiberacao(LocalDateTime.now())
                .hashAssinaturaDigital(hash)
                .build();

        solicitacao.setStatus(SolicitacaoExame.StatusExame.LIBERADO);
        solicitacaoExameRepository.save(solicitacao);

        return laudoLaboratorialRepository.save(laudo);
    }
}
