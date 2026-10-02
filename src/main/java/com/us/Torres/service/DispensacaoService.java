package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Dispensacao;
import com.us.Torres.models.Medicamento;
import com.us.Torres.models.MovimentacaoEstoque;
import com.us.Torres.models.Receita;
import com.us.Torres.models.farmacia.DispensacaoRequest;
import com.us.Torres.repository.DispensacaoRepository;
import com.us.Torres.repository.MedicamentoRepository;
import com.us.Torres.repository.MovimentacaoEstoqueRepository;
import com.us.Torres.repository.ReceitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DispensacaoService {

    private final DispensacaoRepository dispensacaoRepository;
    private final ReceitaRepository receitaRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public List<Dispensacao> listarTodas() {
        return dispensacaoRepository.findAllByOrderByDataDispensacaoDesc();
    }

    public Dispensacao dispensar(DispensacaoRequest request) {
        if (request == null) {
            throw new BusinessException("Dados da dispensação são obrigatórios.");
        }
        if (request.getReceitaId() == null || request.getReceitaId().isBlank()) {
            throw new BusinessException("Receita é obrigatória.");
        }
        if (request.getItens() == null || request.getItens().isEmpty()) {
            throw new BusinessException("Itens da dispensação são obrigatórios.");
        }

        Receita receita = receitaRepository.findById(request.getReceitaId())
                .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada."));

        if (receita.getStatus() == Receita.StatusReceita.DISPENSADA) {
            throw new BusinessException("Esta receita já foi dispensada.");
        }

        List<Dispensacao.ItemDispensado> itensDispensados = new ArrayList<>();

        for (DispensacaoRequest.ItemDispensacao item : request.getItens()) {
            if (item == null || item.getMedicamentoId() == null || item.getMedicamentoId().isBlank()) {
                throw new BusinessException("Identificação do medicamento é obrigatória.");
            }
            if (item.getQuantidadeDispensada() == null || item.getQuantidadeDispensada() <= 0) {
                throw new BusinessException("Quantidade dispensada inválida para o medicamento " + item.getMedicamentoNome());
            }

            Receita.ItemReceita itemReceita = receita.getItens().stream()
                    .filter(receitaItem -> receitaItem.getMedicamentoId() != null
                            && receitaItem.getMedicamentoId().equals(item.getMedicamentoId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("Medicamento não autorizado pela receita: " +
                            (item.getMedicamentoNome() != null ? item.getMedicamentoNome() : item.getMedicamentoId())));

            if (itemReceita.getQuantidadeSolicitada() != null
                    && item.getQuantidadeDispensada() > itemReceita.getQuantidadeSolicitada()) {
                throw new BusinessException("Quantidade dispensada excede a quantidade solicitada na receita para " +
                        (item.getMedicamentoNome() != null ? item.getMedicamentoNome() : item.getMedicamentoId()));
            }

            Medicamento medicamento = medicamentoRepository.findById(item.getMedicamentoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado: " + item.getMedicamentoId()));

            if (medicamento.getQuantidade() == null || medicamento.getQuantidade() < item.getQuantidadeDispensada()) {
                throw new BusinessException("Estoque insuficiente para o medicamento " + medicamento.getNome());
            }

            medicamento.setQuantidade(medicamento.getQuantidade() - item.getQuantidadeDispensada());
            medicamento.calcularStatusEstoque();
            medicamentoRepository.save(medicamento);

            movimentacaoEstoqueRepository.save(MovimentacaoEstoque.builder()
                    .medicamentoId(medicamento.getId())
                    .tipoMovimento(MovimentacaoEstoque.TipoMovimento.SAIDA)
                    .quantidade(item.getQuantidadeDispensada())
                    .lote(item.getLote() != null ? item.getLote() : medicamento.getLote())
                    .motivo("Dispensação de receita " + receita.getId())
                    .usuarioId(request.getFarmaceuticoId())
                    .build());

            itensDispensados.add(Dispensacao.ItemDispensado.builder()
                    .medicamentoId(medicamento.getId())
                    .medicamentoNome(medicamento.getNome())
                    .quantidadeDispensada(item.getQuantidadeDispensada())
                    .lote(item.getLote() != null ? item.getLote() : medicamento.getLote())
                    .build());
        }

        Dispensacao dispensacao = Dispensacao.builder()
                .receitaId(receita.getId())
                .farmaceuticoId(request.getFarmaceuticoId())
                .pacienteId(request.getPacienteId() != null ? request.getPacienteId() : receita.getPacienteId())
                .reciboId("#REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .observacao(request.getObservacao())
                .status(Dispensacao.StatusDispensacao.DISPENSADO)
                .itens(itensDispensados)
                .build();

        receita.setStatus(Receita.StatusReceita.DISPENSADA);
        receitaRepository.save(receita);

        return dispensacaoRepository.save(dispensacao);
    }
}
