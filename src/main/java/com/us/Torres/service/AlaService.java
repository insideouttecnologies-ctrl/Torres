package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ConflictException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Ala;
import com.us.Torres.repository.AlaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlaService {

    private final AlaRepository alaRepository;

    public List<Ala> listarTodas() {
        return alaRepository.findAllByOrderByNomeAsc();
    }

    public Ala buscarPorId(String id) {
        return alaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ala não encontrada."));
    }

    public Ala criar(Ala ala) {
        validarAla(ala);

        if (alaRepository.existsByNome(ala.getNome().trim())) {
            throw new ConflictException("Já existe uma ala com este nome.");
        }

        return alaRepository.save(ala);
    }

    public Ala atualizar(String id, Ala dados) {
        Ala existente = buscarPorId(id);
        validarAla(dados);

        if (!existente.getNome().equalsIgnoreCase(dados.getNome().trim())
                && alaRepository.existsByNome(dados.getNome().trim())) {
            throw new ConflictException("Já existe uma ala com este nome.");
        }

        existente.setNome(dados.getNome().trim());
        existente.setBloco(dados.getBloco());
        existente.setCapacidade(dados.getCapacidade());

        return alaRepository.save(existente);
    }

    private void validarAla(Ala ala) {
        if (ala == null) {
            throw new BusinessException("Dados da ala são obrigatórios.");
        }
        if (ala.getNome() == null || ala.getNome().isBlank()) {
            throw new BusinessException("Nome da ala é obrigatório.");
        }
        if (ala.getCapacidade() == null || ala.getCapacidade() <= 0) {
            throw new BusinessException("Capacidade da ala deve ser maior que zero.");
        }
    }
}
