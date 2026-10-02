package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ConflictException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Leito;
import com.us.Torres.repository.LeitoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeitoService {

    private final LeitoRepository leitoRepository;

    public List<Leito> listarTodos() {
        return leitoRepository.findAllByOrderByCodigoAsc();
    }

    public List<Leito> listarPorStatus(Leito.StatusLeito status) {
        if (status == null) {
            return listarTodos();
        }
        return leitoRepository.findByStatusOrderByCodigoAsc(status);
    }

    public Leito buscarPorId(String id) {
        return leitoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leito não encontrado."));
    }

    public Leito criar(Leito leito) {
        validarLeito(leito);

        if (leitoRepository.existsByCodigo(leito.getCodigo().trim())) {
            throw new ConflictException("Já existe um leito com este código.");
        }

        if (leito.getStatus() == null) {
            leito.setStatus(Leito.StatusLeito.DISPONIVEL);
        }

        return leitoRepository.save(leito);
    }

    public Leito atualizar(String id, Leito dados) {
        Leito existente = buscarPorId(id);
        validarLeito(dados);

        if (!existente.getCodigo().equalsIgnoreCase(dados.getCodigo().trim())
                && leitoRepository.existsByCodigo(dados.getCodigo().trim())) {
            throw new ConflictException("Já existe um leito com este código.");
        }

        existente.setCodigo(dados.getCodigo().trim());
        existente.setAla(dados.getAla());
        existente.setStatus(dados.getStatus() == null ? Leito.StatusLeito.DISPONIVEL : dados.getStatus());
        existente.setPacienteAtualId(dados.getPacienteAtualId());
        existente.setPacienteAtualNome(dados.getPacienteAtualNome());
        existente.setDataAdmissao(dados.getDataAdmissao());
        existente.setMedicoAssistente(dados.getMedicoAssistente());

        return leitoRepository.save(existente);
    }

    public Leito alterarStatus(String id, Leito.StatusLeito status) {
        Leito leito = buscarPorId(id);
        if (status == null) {
            throw new BusinessException("Status do leito é obrigatório.");
        }
        leito.setStatus(status);
        return leitoRepository.save(leito);
    }

    private void validarLeito(Leito leito) {
        if (leito == null) {
            throw new BusinessException("Dados do leito são obrigatórios.");
        }
        if (leito.getCodigo() == null || leito.getCodigo().isBlank()) {
            throw new BusinessException("Código do leito é obrigatório.");
        }
        if (leito.getAla() == null || leito.getAla().isBlank()) {
            throw new BusinessException("Ala do leito é obrigatória.");
        }
    }
}
