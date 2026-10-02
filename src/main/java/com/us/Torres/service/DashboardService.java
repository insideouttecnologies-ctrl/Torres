package com.us.Torres.service;

import com.us.Torres.models.*;
import com.us.Torres.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PacienteRepository pacienteRepository;
    private final ConsultaRepository consultaRepository;
    private final TriagemRepository triagemRepository;
    private final InternamentoRepository internamentoRepository;
    private final LeitoRepository leitoRepository;
    private final ReceitaRepository receitaRepository;
    private final FaturaRepository faturaRepository;
    private final MedicamentoRepository medicamentoRepository;

    public DashboardResumo obterResumo() {
        return DashboardResumo.builder()
                .totalPacientes(pacienteRepository.count())
                .totalConsultas(consultaRepository.count())
                .totalTriagens(triagemRepository.count())
                .admissoesAtivas((long) internamentoRepository
                        .findByStatusOrderByDataAdmissaoDesc(Internamento.StatusInternamento.ATIVO)
                        .size())
                .leitosOcupados((long) leitoRepository
                        .findByStatusOrderByCodigoAsc(Leito.StatusLeito.OCUPADO)
                        .size())
                .receitasPendentes((long) receitaRepository
                        .findByStatusOrderByDataEmissaoDesc(Receita.StatusReceita.PENDENTE)
                        .size())
                .faturamentoPeriodo(calcularFaturamentoPeriodo())
                .medicamentosEstoqueBaixo(contarMedicamentosBaixo())
                .build();
    }

    private BigDecimal calcularFaturamentoPeriodo() {
        return faturaRepository.findAllByOrderByDataEmissaoDesc().stream()
                .map(Fatura::getValorTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private long contarMedicamentosBaixo() {
        return medicamentoRepository.findAll().stream()
                .filter(medicamento -> medicamento.getQuantidade() != null
                        && medicamento.getQuantidadeMinima() != null
                        && medicamento.getQuantidade() <= medicamento.getQuantidadeMinima())
                .count();
    }
}
