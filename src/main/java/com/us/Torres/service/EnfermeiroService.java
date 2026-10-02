package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ConflictException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Enfermeiro;
import com.us.Torres.repository.EnfermeiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnfermeiroService {

    private final EnfermeiroRepository enfermeiroRepository;

    public List<Enfermeiro> listarTodos() {
        return enfermeiroRepository.findAllByOrderByNomeAsc();
    }

    public List<Enfermeiro> listarAtivos() {
        return enfermeiroRepository.findByAtivoOrderByNomeAsc(true);
    }

    public Enfermeiro buscarPorId(String id) {
        return enfermeiroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enfermeiro não encontrado."));
    }

    public Enfermeiro criar(Enfermeiro enfermeiro) {
        validarEnfermeiro(enfermeiro);

        if (enfermeiroRepository.existsByNumeroRegistro(enfermeiro.getNumeroRegistro())) {
            throw new ConflictException("Número de registo do enfermeiro já existe.");
        }

        if (enfermeiroRepository.existsByEmail(enfermeiro.getEmail())) {
            throw new ConflictException("E-mail do enfermeiro já cadastrado.");
        }

        enfermeiro.setAtivo(true);
        return enfermeiroRepository.save(enfermeiro);
    }

    public Enfermeiro atualizar(String id, Enfermeiro dados) {
        Enfermeiro existente = buscarPorId(id);
        validarEnfermeiro(dados);

        if (!existente.getNumeroRegistro().equals(dados.getNumeroRegistro())
                && enfermeiroRepository.existsByNumeroRegistro(dados.getNumeroRegistro())) {
            throw new ConflictException("Número de registo do enfermeiro já existe.");
        }

        if (!existente.getEmail().equalsIgnoreCase(dados.getEmail())
                && enfermeiroRepository.existsByEmail(dados.getEmail())) {
            throw new ConflictException("E-mail do enfermeiro já cadastrado.");
        }

        existente.setNome(dados.getNome());
        existente.setFuncionarioId(dados.getFuncionarioId());
        existente.setNumeroRegistro(dados.getNumeroRegistro());
        existente.setEspecialidade(dados.getEspecialidade());
        existente.setTelefone(dados.getTelefone());
        existente.setEmail(dados.getEmail());
        existente.setEscalaTrabalho(dados.getEscalaTrabalho());
        existente.setAtivo(dados.isAtivo());

        return enfermeiroRepository.save(existente);
    }

    public Enfermeiro alterarStatus(String id, boolean ativo) {
        Enfermeiro enfermeiro = buscarPorId(id);
        enfermeiro.setAtivo(ativo);
        return enfermeiroRepository.save(enfermeiro);
    }

    private void validarEnfermeiro(Enfermeiro enfermeiro) {
        if (enfermeiro == null) {
            throw new BusinessException("Dados do enfermeiro são obrigatórios.");
        }
        if (enfermeiro.getNome() == null || enfermeiro.getNome().isBlank()) {
            throw new BusinessException("Nome do enfermeiro é obrigatório.");
        }
        if (enfermeiro.getNumeroRegistro() == null || enfermeiro.getNumeroRegistro().isBlank()) {
            throw new BusinessException("Número de registo do enfermeiro é obrigatório.");
        }
        if (enfermeiro.getEmail() == null || enfermeiro.getEmail().isBlank()) {
            throw new BusinessException("E-mail do enfermeiro é obrigatório.");
        }
        if (enfermeiro.getFuncionarioId() == null || enfermeiro.getFuncionarioId().isBlank()) {
            throw new BusinessException("Funcionário vinculado é obrigatório.");
        }
    }
}
