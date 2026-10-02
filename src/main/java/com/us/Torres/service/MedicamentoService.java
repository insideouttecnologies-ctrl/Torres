package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Medicamento;
import com.us.Torres.repository.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public List<Medicamento> listarTodos() {
        return medicamentoRepository.findAllByOrderByNomeAsc();
    }

    public Medicamento buscarPorId(String id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado."));
    }

    public Medicamento criar(Medicamento medicamento) {
        validar(medicamento);

        if (medicamentoRepository.findByCodigoItem(medicamento.getCodigoItem()).isPresent()) {
            throw new BusinessException("Código do medicamento já cadastrado.");
        }

        medicamento.setAtivo(true);
        if (medicamento.getQuantidade() == null) {
            medicamento.setQuantidade(0);
        }
        if (medicamento.getQuantidadeMinima() == null) {
            medicamento.setQuantidadeMinima(10);
        }

        medicamento.calcularStatusEstoque();
        return medicamentoRepository.save(medicamento);
    }

    public Medicamento atualizar(String id, Medicamento medicamento) {
        Medicamento atual = buscarPorId(id);
        validar(medicamento);

        if (!atual.getCodigoItem().equals(medicamento.getCodigoItem())
                && medicamentoRepository.findByCodigoItem(medicamento.getCodigoItem()).isPresent()) {
            throw new BusinessException("Código do medicamento já cadastrado.");
        }

        atual.setCodigoItem(medicamento.getCodigoItem());
        atual.setNome(medicamento.getNome());
        atual.setPrincipioAtivo(medicamento.getPrincipioAtivo());
        atual.setCategoria(medicamento.getCategoria());
        atual.setFabricante(medicamento.getFabricante());
        atual.setCodigoBarras(medicamento.getCodigoBarras());
        atual.setLote(medicamento.getLote());
        atual.setQuantidade(medicamento.getQuantidade());
        atual.setQuantidadeMinima(medicamento.getQuantidadeMinima());
        atual.setPrecoCompra(medicamento.getPrecoCompra());
        atual.setPrecoVenda(medicamento.getPrecoVenda());
        atual.setDataFabricacao(medicamento.getDataFabricacao());
        atual.setDataValidade(medicamento.getDataValidade());
        atual.setDosagem(medicamento.getDosagem());
        atual.setFormaFarmaceutica(medicamento.getFormaFarmaceutica());
        atual.setLocalizacaoAlmoxarifado(medicamento.getLocalizacaoAlmoxarifado());
        atual.setControlado(medicamento.isControlado());
        atual.setAtivo(medicamento.isAtivo());

        atual.calcularStatusEstoque();
        return medicamentoRepository.save(atual);
    }

    public void remover(String id) {
        Medicamento medicamento = buscarPorId(id);
        medicamentoRepository.delete(medicamento);
    }

    public Medicamento atualizarEstoque(String id, Integer quantidade) {
        Medicamento medicamento = buscarPorId(id);
        if (quantidade == null || quantidade < 0) {
            throw new BusinessException("Quantidade válida é obrigatória.");
        }

        medicamento.setQuantidade(quantidade);
        medicamento.calcularStatusEstoque();
        return medicamentoRepository.save(medicamento);
    }

    private void validar(Medicamento medicamento) {
        if (medicamento == null) {
            throw new BusinessException("Dados do medicamento são obrigatórios.");
        }

        if (medicamento.getNome() == null || medicamento.getNome().isBlank()) {
            throw new BusinessException("Nome do medicamento é obrigatório.");
        }

        if (medicamento.getCodigoItem() == null || medicamento.getCodigoItem().isBlank()) {
            throw new BusinessException("Código do medicamento é obrigatório.");
        }

        if (medicamento.getQuantidade() != null && medicamento.getQuantidade() < 0) {
            throw new BusinessException("Quantidade em estoque não pode ser negativa.");
        }

        if (medicamento.getQuantidadeMinima() != null && medicamento.getQuantidadeMinima() < 0) {
            throw new BusinessException("Estoque mínimo não pode ser negativo.");
        }
    }
}
