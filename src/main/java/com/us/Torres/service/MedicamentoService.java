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
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medicamento não encontrado."
                        )
                );
    }

    public Medicamento criar(Medicamento medicamento) {

        validar(medicamento);

        /*
         * Não definir valores automaticamente.
         *
         * Os dados devem vir do formulário/API.
         */

        medicamento.calcularStatusEstoque();

        return medicamentoRepository.save(medicamento);
    }

    public Medicamento atualizar(
            String id,
            Medicamento medicamento
    ) {

        Medicamento atual = buscarPorId(id);

        validar(medicamento);

        atual.setNome(
                medicamento.getNome()
        );

        atual.setPrincipioAtivo(
                medicamento.getPrincipioAtivo()
        );

        atual.setCategoria(
                medicamento.getCategoria()
        );

        atual.setFabricante(
                medicamento.getFabricante()
        );

        atual.setCodigoBarras(
                medicamento.getCodigoBarras()
        );

        atual.setLote(
                medicamento.getLote()
        );

        atual.setQuantidade(
                medicamento.getQuantidade()
        );

        atual.setQuantidadeMinima(
                medicamento.getQuantidadeMinima()
        );

        atual.setPrecoCompra(
                medicamento.getPrecoCompra()
        );

        atual.setPrecoVenda(
                medicamento.getPrecoVenda()
        );

        atual.setDataFabricacao(
                medicamento.getDataFabricacao()
        );

        atual.setDataValidade(
                medicamento.getDataValidade()
        );

        atual.setDosagem(
                medicamento.getDosagem()
        );

        atual.setFormaFarmaceutica(
                medicamento.getFormaFarmaceutica()
        );

        atual.setLocalizacaoAlmoxarifado(
                medicamento.getLocalizacaoAlmoxarifado()
        );

        atual.setControlado(
                medicamento.isControlado()
        );

        atual.setAtivo(
                medicamento.isAtivo()
        );

        atual.calcularStatusEstoque();

        return medicamentoRepository.save(atual);
    }

    public void remover(String id) {

        Medicamento medicamento = buscarPorId(id);

        medicamentoRepository.delete(medicamento);
    }

    public Medicamento atualizarEstoque(
            String id,
            Integer quantidade
    ) {

        Medicamento medicamento = buscarPorId(id);

        if (quantidade == null || quantidade < 0) {

            throw new BusinessException(
                    "Quantidade válida é obrigatória."
            );
        }

        medicamento.setQuantidade(quantidade);

        medicamento.calcularStatusEstoque();

        return medicamentoRepository.save(medicamento);
    }

    private void validar(Medicamento medicamento) {

        System.out.println(medicamento);

        if (medicamento == null) {

            throw new BusinessException(
                    "Dados do medicamento são obrigatórios."
            );
        }

        if (medicamento.getNome() == null
                || medicamento.getNome().isBlank()) {

            throw new BusinessException(
                    "Nome do medicamento é obrigatório."
            );
        }

        if (medicamento.getQuantidade() == null) {

            throw new BusinessException(
                    "Quantidade em estoque é obrigatória."
            );
        }

        if (medicamento.getQuantidade() < 0) {

            throw new BusinessException(
                    "Quantidade em estoque não pode ser negativa."
            );
        }

        if (medicamento.getQuantidadeMinima() == null) {

            throw new BusinessException(
                    "Estoque mínimo é obrigatório."
            );
        }

        if (medicamento.getQuantidadeMinima() < 0) {

            throw new BusinessException(
                    "Estoque mínimo não pode ser negativo."
            );
        }

        if (medicamento.getPrecoCompra() == null) {

            throw new BusinessException(
                    "Preço de compra é obrigatório."
            );
        }

        if (medicamento.getPrecoCompra().signum() < 0) {

            throw new BusinessException(
                    "Preço de compra não pode ser negativo."
            );
        }

        if (medicamento.getPrecoVenda() == null) {

            throw new BusinessException(
                    "Preço de venda é obrigatório."
            );
        }

        if (medicamento.getPrecoVenda().signum() < 0) {

            throw new BusinessException(
                    "Preço de venda não pode ser negativo."
            );
        }

        if (medicamento.getCategoria() == null
                || medicamento.getCategoria().isBlank()) {

            throw new BusinessException(
                    "Categoria do medicamento é obrigatória."
            );
        }

        if (medicamento.getFormaFarmaceutica() == null
                || medicamento.getFormaFarmaceutica().isBlank()) {

            throw new BusinessException(
                    "Forma farmacêutica é obrigatória."
            );
        }
    }
}