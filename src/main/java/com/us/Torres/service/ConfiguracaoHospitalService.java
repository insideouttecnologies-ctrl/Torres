package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.models.ConfiguracaoHospital;
import com.us.Torres.repository.ConfiguracaoHospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfiguracaoHospitalService {

    private final ConfiguracaoHospitalRepository configuracaoHospitalRepository;

    public ConfiguracaoHospital obterConfiguracao() {
        return configuracaoHospitalRepository.findFirstByOrderByAtualizadoEmDesc()
                .orElseGet(this::criarPadrao);
    }

    public ConfiguracaoHospital atualizar(ConfiguracaoHospital configuracao) {
        if (configuracao == null) {
            throw new BusinessException("Dados da configuração são obrigatórios.");
        }

        validar(configuracao);

        ConfiguracaoHospital existente = configuracaoHospitalRepository.findFirstByOrderByAtualizadoEmDesc()
                .orElseGet(this::criarPadrao);

        existente.setNomeHospital(configuracao.getNomeHospital());
        existente.setNif(configuracao.getNif());
        existente.setTelefone(configuracao.getTelefone());
        existente.setEndereco(configuracao.getEndereco());
        existente.setEmail(configuracao.getEmail());
        existente.setMoedaPadrao(configuracao.getMoedaPadrao());
        existente.setAlertaEstoqueAtivo(configuracao.isAlertaEstoqueAtivo());
        existente.setDoisFatoresAtivo(configuracao.isDoisFatoresAtivo());
        existente.setAuditoriaAtiva(configuracao.isAuditoriaAtiva());

        return configuracaoHospitalRepository.save(existente);
    }

    private ConfiguracaoHospital criarPadrao() {
        ConfiguracaoHospital configuracao = ConfiguracaoHospital.builder()
                .nomeHospital("Hospital Torres")
                .nif("0000000000")
                .telefone("+244 000 000 000")
                .endereco("Luanda, Angola")
                .email("contato@hospitaltorres.com")
                .moedaPadrao("AOA")
                .alertaEstoqueAtivo(true)
                .doisFatoresAtivo(false)
                .auditoriaAtiva(true)
                .build();
        return configuracaoHospitalRepository.save(configuracao);
    }

    private void validar(ConfiguracaoHospital configuracao) {
        if (configuracao.getNomeHospital() == null || configuracao.getNomeHospital().isBlank()) {
            throw new BusinessException("Nome do hospital é obrigatório.");
        }
        if (configuracao.getNif() == null || configuracao.getNif().isBlank()) {
            throw new BusinessException("NIF do hospital é obrigatório.");
        }
        if (configuracao.getMoedaPadrao() == null || configuracao.getMoedaPadrao().isBlank()) {
            throw new BusinessException("Moeda padrão é obrigatória.");
        }
    }
}
