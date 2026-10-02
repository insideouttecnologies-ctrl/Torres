package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Receita;
import com.us.Torres.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceitaService {

    private final ReceitaRepository receitaRepository;

    public List<Receita> listarTodas() {
        return receitaRepository.findAllByOrderByDataEmissaoDesc();
    }

    public List<Receita> listarPendentes() {
        return receitaRepository.findByStatusOrderByDataEmissaoDesc(Receita.StatusReceita.PENDENTE);
    }

    public Receita buscarPorId(String id) {
        return receitaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada."));
    }

    public Receita criar(Receita receita) {
        validar(receita);

        if (receita.getDataEmissao() == null) {
            receita.setDataEmissao(LocalDate.now());
        }
        if (receita.getStatus() == null) {
            receita.setStatus(Receita.StatusReceita.PENDENTE);
        }

        return receitaRepository.save(receita);
    }

    public Receita atualizarStatus(String id, Receita.StatusReceita status) {
        Receita receita = buscarPorId(id);
        if (status == null) {
            throw new BusinessException("Status da receita é obrigatório.");
        }
        receita.setStatus(status);
        return receitaRepository.save(receita);
    }

    private void validar(Receita receita) {
        if (receita == null) {
            throw new BusinessException("Dados da receita são obrigatórios.");
        }
        if (receita.getPacienteId() == null || receita.getPacienteId().isBlank()) {
            throw new BusinessException("Paciente da receita é obrigatório.");
        }
        if (receita.getMedicoId() == null || receita.getMedicoId().isBlank()) {
            throw new BusinessException("Médico responsável é obrigatório.");
        }
        if (receita.getItens() == null || receita.getItens().isEmpty()) {
            throw new BusinessException("Receita deve conter pelo menos um item.");
        }
    }
}
