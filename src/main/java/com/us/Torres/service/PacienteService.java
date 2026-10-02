package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Paciente;
import com.us.Torres.repository.PacienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;

    public ResponseEntity<Paciente> salvar(Paciente paciente) {
        validarPaciente(paciente);

        String bi = paciente.getBi().trim();
        String telefone = paciente.getTelefone().trim();
        String nome = paciente.getNome().trim();

        if (pacienteRepository.findByBi(bi).isPresent()) {
            throw new BusinessException("BI já cadastrado no sistema.");
        }

        if (pacienteRepository.findByTelefone(telefone).isPresent()) {
            throw new BusinessException("Telefone já cadastrado no sistema.");
        }

        paciente.setNome(nome);
        paciente.setBi(bi);
        paciente.setTelefone(telefone);
        paciente.setStatus(paciente.getStatus() == null || paciente.getStatus().isBlank() ? "ATIVO" : paciente.getStatus().trim().toUpperCase());

        return new ResponseEntity<>(pacienteRepository.save(paciente), HttpStatus.CREATED);
    }

    public ResponseEntity<List<Paciente>> obterPacientes(String nome, String status) {
        List<Paciente> pacientes;

        if (nome != null && !nome.isBlank() && status != null && !status.isBlank()) {
            pacientes = pacienteRepository.findByNomeContainingIgnoreCaseAndStatusOrderByNomeAsc(nome.trim(), status.trim().toUpperCase());
        } else if (nome != null && !nome.isBlank()) {
            pacientes = pacienteRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim());
        } else if (status != null && !status.isBlank()) {
            pacientes = pacienteRepository.findByStatusOrderByNomeAsc(status.trim().toUpperCase());
        } else {
            pacientes = pacienteRepository.findAllByOrderByNomeAsc();
        }

        return ResponseEntity.ok(pacientes);
    }

    public ResponseEntity<Paciente> buscarPorId(String id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));
        return ResponseEntity.ok(paciente);
    }

    public ResponseEntity<Paciente> atualizar(String id, Paciente dados) {
        Paciente existente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));

        validarPaciente(dados);

        String novoBi = dados.getBi().trim();
        String novoTelefone = dados.getTelefone().trim();

        if (!existente.getBi().equalsIgnoreCase(novoBi)
                && pacienteRepository.findByBi(novoBi).isPresent()) {
            throw new BusinessException("BI já cadastrado no sistema.");
        }

        if (!existente.getTelefone().equalsIgnoreCase(novoTelefone)
                && pacienteRepository.findByTelefone(novoTelefone).isPresent()) {
            throw new BusinessException("Telefone já cadastrado no sistema.");
        }

        existente.setNome(dados.getNome().trim());
        existente.setBi(novoBi);
        existente.setGenero(dados.getGenero());
        existente.setIdade(dados.getIdade());
        existente.setDataNascimento(dados.getDataNascimento());
        existente.setStatus((dados.getStatus() == null || dados.getStatus().isBlank()) ? "ATIVO" : dados.getStatus().trim().toUpperCase());
        existente.setSeguro(dados.getSeguro());
        existente.setTelefone(novoTelefone);
        existente.setTipoSanguineo(dados.getTipoSanguineo());
        existente.setAlergias(dados.getAlergias());
        existente.setDoencasPreExistentes(dados.getDoencasPreExistentes());
        existente.setUltimaPressaoAferida(dados.getUltimaPressaoAferida());

        return ResponseEntity.ok(pacienteRepository.save(existente));
    }

    public ResponseEntity<Void> excluir(String id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));

        pacienteRepository.delete(paciente);
        return ResponseEntity.noContent().build();
    }

    private void validarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new BusinessException("Dados do paciente são obrigatórios.");
        }
        if (paciente.getNome() == null || paciente.getNome().isBlank()) {
            throw new BusinessException("Nome do paciente é obrigatório.");
        }
        if (paciente.getBi() == null || paciente.getBi().isBlank()) {
            throw new BusinessException("BI do paciente é obrigatório.");
        }
        if (paciente.getTelefone() == null || paciente.getTelefone().isBlank()) {
            throw new BusinessException("Telefone do paciente é obrigatório.");
        }
    }
}
