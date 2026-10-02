package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Medicamento;
import com.us.Torres.models.MovimentacaoEstoque;
import com.us.Torres.models.farmacia.EstoqueMovimentoRequest;
import com.us.Torres.repository.MedicamentoRepository;
import com.us.Torres.repository.MovimentacaoEstoqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final MedicamentoRepository medicamentoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public List<Medicamento> listarEstoque() {
        return medicamentoRepository.findAllByOrderByNomeAsc();
    }

    public MovimentacaoEstoque registrarMovimentacao(EstoqueMovimentoRequest request) {
        if (request == null || request.getMedicamentoId() == null || request.getMedicamentoId().isBlank()) {
            throw new BusinessException("Medicamento é obrigatório para a movimentação de estoque.");
        }
        if (request.getTipoMovimento() == null || request.getTipoMovimento().isBlank()) {
            throw new BusinessException("Tipo de movimento é obrigatório.");
        }
        if (request.getQuantidade() == null || request.getQuantidade() <= 0) {
            throw new BusinessException("Quantidade da movimentação deve ser maior que zero.");
        }

        MovimentacaoEstoque.TipoMovimento tipoMovimento;
        try {
            tipoMovimento = MovimentacaoEstoque.TipoMovimento.valueOf(request.getTipoMovimento().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Tipo de movimento inválido.");
        }

        Medicamento medicamento = medicamentoRepository.findById(request.getMedicamentoId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado."));

        int saldoAnterior = medicamento.getQuantidade() == null ? 0 : medicamento.getQuantidade();
        int novoSaldo;

        switch (tipoMovimento) {
            case ENTRADA -> novoSaldo = saldoAnterior + request.getQuantidade();
            case SAIDA -> {
                if (saldoAnterior < request.getQuantidade()) {
                    throw new BusinessException("Saldo insuficiente para saída do estoque.");
                }
                novoSaldo = saldoAnterior - request.getQuantidade();
            }
            case AJUSTE -> novoSaldo = saldoAnterior + request.getQuantidade();
            case PERDA -> {
                if (saldoAnterior < request.getQuantidade()) {
                    throw new BusinessException("Quantidade de perda maior que o saldo atual.");
                }
                novoSaldo = saldoAnterior - request.getQuantidade();
            }
            default -> throw new BusinessException("Tipo de movimento inválido.");
        }

        medicamento.setQuantidade(novoSaldo);
        medicamento.calcularStatusEstoque();
        medicamentoRepository.save(medicamento);

        MovimentacaoEstoque movimentacao = MovimentacaoEstoque.builder()
                .medicamentoId(medicamento.getId())
                .tipoMovimento(tipoMovimento)
                .quantidade(request.getQuantidade())
                .lote(request.getLote())
                .dataValidade(request.getDataValidade())
                .motivo(request.getMotivo())
                .usuarioId(request.getUsuarioId())
                .build();

        return movimentacaoEstoqueRepository.save(movimentacao);
    }

    public List<Medicamento> listarEstoqueBaixo() {
        return medicamentoRepository.findAllByOrderByNomeAsc().stream()
                .filter(medicamento -> medicamento.getQuantidade() != null
                        && medicamento.getQuantidadeMinima() != null
                        && medicamento.getQuantidade() <= medicamento.getQuantidadeMinima())
                .toList();
    }
}
