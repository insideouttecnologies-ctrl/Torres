package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.EvolucaoClinica;
import com.us.Torres.models.Internamento;
import com.us.Torres.models.internamento.EvolucaoRequest;
import com.us.Torres.repository.EvolucaoClinicaRepository;
import com.us.Torres.repository.InternamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvolucaoClinicaService {

    private final EvolucaoClinicaRepository evolucaoClinicaRepository;
    private final InternamentoRepository internamentoRepository;

    public List<EvolucaoClinica> listarPorInternamento(String internamentoId) {
        return evolucaoClinicaRepository.findByInternamentoIdOrderByDataHoraDesc(internamentoId);
    }

    public EvolucaoClinica registrar(String internamentoId, EvolucaoRequest request) {
        if (request == null) {
            throw new BusinessException("Dados da evolução são obrigatórios.");
        }

        Internamento internamento = internamentoRepository.findById(internamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Internamento não encontrado."));

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

    public EvolucaoClinica buscarPorId(String id) {
        return evolucaoClinicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evolução clínica não encontrada."));
    }
}
