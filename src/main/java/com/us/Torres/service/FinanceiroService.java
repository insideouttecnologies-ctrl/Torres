package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Fatura;
import com.us.Torres.models.Pagamento;
import com.us.Torres.models.financeiro.FaturaRequest;
import com.us.Torres.models.financeiro.PagamentoRequest;
import com.us.Torres.repository.FaturaRepository;
import com.us.Torres.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceiroService {

    private final FaturaRepository faturaRepository;
    private final PagamentoRepository pagamentoRepository;

    public List<Fatura> listarFaturas() {
        return faturaRepository.findAllByOrderByDataEmissaoDesc();
    }

    public Fatura buscarFatura(String id) {
        return faturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fatura não encontrada."));
    }

    public Fatura criarFatura(FaturaRequest request) {
        if (request == null) {
            throw new BusinessException("Dados da fatura são obrigatórios.");
        }
        if (request.getPacienteId() == null || request.getPacienteId().isBlank()) {
            throw new BusinessException("Paciente da fatura é obrigatório.");
        }
        if (request.getValorBruto() == null || request.getValorBruto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor bruto da fatura deve ser maior que zero.");
        }

        Fatura fatura = Fatura.builder()
                .pacienteId(request.getPacienteId())
                .descricao(request.getDescricao())
                .convenio(request.getConvenio())
                .valorBruto(request.getValorBruto())
                .dataVencimento(request.getDataVencimento())
                .status(Fatura.StatusFatura.PENDENTE)
                .build();

        fatura.calcularValores();
        return faturaRepository.save(fatura);
    }

    public List<Pagamento> listarPagamentos() {
        return pagamentoRepository.findAllByOrderByDataPagamentoDesc();
    }

    public Pagamento processarPagamento(PagamentoRequest request) {
        if (request == null || request.getFaturaId() == null || request.getFaturaId().isBlank()) {
            throw new BusinessException("Fatura é obrigatória para o pagamento.");
        }
        if (request.getValorPago() == null || request.getValorPago().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor do pagamento deve ser maior que zero.");
        }
        if (request.getMetodoPagamento() == null || request.getMetodoPagamento().isBlank()) {
            throw new BusinessException("Método de pagamento é obrigatório.");
        }

        Fatura fatura = buscarFatura(request.getFaturaId());

        Pagamento.MetodoPagamento metodo;
        try {
            metodo = Pagamento.MetodoPagamento.valueOf(request.getMetodoPagamento().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Método de pagamento inválido.");
        }

        Pagamento pagamento = Pagamento.builder()
                .faturaId(fatura.getId())
                .pacienteId(request.getPacienteId() != null ? request.getPacienteId() : fatura.getPacienteId())
                .valorPago(request.getValorPago())
                .valorIva(fatura.getValorIva())
                .valorTotal(fatura.getValorTotal())
                .metodo(metodo)
                .descricao(request.getDescricao() != null ? request.getDescricao() : "Pagamento de fatura")
                .status(Pagamento.StatusPagamento.PAGO)
                .build();

        if (request.getValorPago().compareTo(fatura.getValorTotal()) >= 0) {
            fatura.setStatus(Fatura.StatusFatura.PAGA);
        } else {
            fatura.setStatus(Fatura.StatusFatura.PENDENTE);
        }
        faturaRepository.save(fatura);

        return pagamentoRepository.save(pagamento);
    }
}
