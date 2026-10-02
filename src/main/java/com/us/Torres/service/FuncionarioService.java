package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ConflictException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Funcionario;
import com.us.Torres.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAllByOrderByNomeAsc();
    }

    public Funcionario buscarPorId(String id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado."));
    }

    public Funcionario buscarPorCodigo(String codigoFuncionario) {
        return funcionarioRepository.findByCodigoFuncionario(codigoFuncionario)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado para o código informado."));
    }

    public Funcionario criar(Funcionario funcionario) {
        validarFuncionario(funcionario);

        if (funcionarioRepository.existsByCodigoFuncionario(funcionario.getCodigoFuncionario())) {
            throw new ConflictException("Código de funcionário já cadastrado.");
        }

        if (funcionarioRepository.existsByEmail(funcionario.getEmail())) {
            throw new ConflictException("E-mail do funcionário já cadastrado.");
        }

        if (funcionario.getDataContratacao() == null) {
            funcionario.setDataContratacao(LocalDate.now());
        }

        funcionario.setAtivo(true);
        return funcionarioRepository.save(funcionario);
    }

    public Funcionario atualizar(String id, Funcionario funcionarioAtualizado) {
        Funcionario funcionario = buscarPorId(id);

        validarFuncionario(funcionarioAtualizado);

        if (!funcionario.getCodigoFuncionario().equals(funcionarioAtualizado.getCodigoFuncionario())
                && funcionarioRepository.existsByCodigoFuncionario(funcionarioAtualizado.getCodigoFuncionario())) {
            throw new ConflictException("Código de funcionário já cadastrado.");
        }

        if (!funcionario.getEmail().equalsIgnoreCase(funcionarioAtualizado.getEmail())
                && funcionarioRepository.existsByEmail(funcionarioAtualizado.getEmail())) {
            throw new ConflictException("E-mail do funcionário já cadastrado.");
        }

        funcionario.setNome(funcionarioAtualizado.getNome());
        funcionario.setCodigoFuncionario(funcionarioAtualizado.getCodigoFuncionario());
        funcionario.setEmail(funcionarioAtualizado.getEmail());
        funcionario.setTelefone(funcionarioAtualizado.getTelefone());
        funcionario.setEndereco(funcionarioAtualizado.getEndereco());
        funcionario.setCargo(funcionarioAtualizado.getCargo());
        funcionario.setDepartamento(funcionarioAtualizado.getDepartamento());
        funcionario.setTurno(funcionarioAtualizado.getTurno());
        funcionario.setDataNascimento(funcionarioAtualizado.getDataNascimento());
        funcionario.setDataContratacao(funcionarioAtualizado.getDataContratacao());
        funcionario.setAtivo(funcionarioAtualizado.isAtivo());
        funcionario.setUsuarioId(funcionarioAtualizado.getUsuarioId());

        return funcionarioRepository.save(funcionario);
    }

    public Funcionario alterarStatus(String id, boolean ativo) {
        Funcionario funcionario = buscarPorId(id);
        funcionario.setAtivo(ativo);
        return funcionarioRepository.save(funcionario);
    }

    private void validarFuncionario(Funcionario funcionario) {
        if (funcionario == null) {
            throw new BusinessException("Dados do funcionário são obrigatórios.");
        }
        if (funcionario.getNome() == null || funcionario.getNome().isBlank()) {
            throw new BusinessException("Nome do funcionário é obrigatório.");
        }
        if (funcionario.getCodigoFuncionario() == null || funcionario.getCodigoFuncionario().isBlank()) {
            throw new BusinessException("Código do funcionário é obrigatório.");
        }
        if (funcionario.getEmail() == null || funcionario.getEmail().isBlank()) {
            throw new BusinessException("E-mail do funcionário é obrigatório.");
        }
        if (funcionario.getCargo() == null) {
            throw new BusinessException("Cargo do funcionário é obrigatório.");
        }
    }
}
