package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ConflictException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Medico;
import com.us.Torres.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;

    public List<Medico> listarTodos() {
        return medicoRepository.findAllByOrderByNomeAsc();
    }

    public List<Medico> listarAtivos() {
        return medicoRepository.findByAtivoOrderByNomeAsc(true);
    }

    public Medico buscarPorId(String id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado."));
    }

    public Medico criar(Medico medico) {
        validarMedico(medico);

        String numeroOrdem = medico.getNumeroOrdem().trim();
        String telefone = medico.getTelefone().trim();

        if (medicoRepository.existsByNumeroOrdem(numeroOrdem)) {
            throw new ConflictException("Número da ordem do médico já cadastrado.");
        }

        if (medicoRepository.existsByTelefone(telefone)) {
            throw new ConflictException("Telefone do médico já cadastrado.");
        }

        medico.setNumeroOrdem(numeroOrdem);
        medico.setTelefone(telefone);
        medico.setAtivo(true);

        return medicoRepository.save(medico);
    }

    public Medico atualizar(String id, Medico dados) {
        Medico existente = buscarPorId(id);
        validarMedico(dados);

        String numeroOrdem = dados.getNumeroOrdem().trim();
        String telefone = dados.getTelefone().trim();

        if (!existente.getNumeroOrdem().equalsIgnoreCase(numeroOrdem)
                && medicoRepository.existsByNumeroOrdem(numeroOrdem)) {
            throw new ConflictException("Número da ordem do médico já cadastrado.");
        }

        if (!existente.getTelefone().equalsIgnoreCase(telefone)
                && medicoRepository.existsByTelefone(telefone)) {
            throw new ConflictException("Telefone do médico já cadastrado.");
        }

        existente.setNome(dados.getNome());
        existente.setNumeroOrdem(numeroOrdem);
        existente.setEspecialidade(dados.getEspecialidade());
        existente.setTelefone(telefone);
        existente.setEscala(dados.getEscala());
        existente.setFuncionarioId(dados.getFuncionarioId());
        existente.setUser(dados.getUser());
        existente.setAtivo(dados.isAtivo());

        return medicoRepository.save(existente);
    }

    public Medico alterarStatus(String id, boolean ativo) {
        Medico medico = buscarPorId(id);
        medico.setAtivo(ativo);
        return medicoRepository.save(medico);
    }

    private void validarMedico(Medico medico) {
        if (medico == null) {
            throw new BusinessException("Dados do médico são obrigatórios.");
        }
        if (medico.getNome() == null || medico.getNome().isBlank()) {
            throw new BusinessException("Nome do médico é obrigatório.");
        }
        if (medico.getNumeroOrdem() == null || medico.getNumeroOrdem().isBlank()) {
            throw new BusinessException("Número da ordem do médico é obrigatório.");
        }
        if (medico.getEspecialidade() == null || medico.getEspecialidade().isBlank()) {
            throw new BusinessException("Especialidade do médico é obrigatória.");
        }
        if (medico.getTelefone() == null || medico.getTelefone().isBlank()) {
            throw new BusinessException("Telefone do médico é obrigatório.");
        }
    }
}
