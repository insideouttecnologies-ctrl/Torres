package com.us.Torres.service;

import com.us.Torres.models.*;
import com.us.Torres.repository.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardServiceTest {

    @Test
    void shouldAggregateResumenMetrics() {
        PacienteRepository pacienteRepository = mock(PacienteRepository.class);
        ConsultaRepository consultaRepository = mock(ConsultaRepository.class);
        MedicoRepository medicoRepository = mock(MedicoRepository.class);
        TriagemRepository triagemRepository = mock(TriagemRepository.class);
        InternamentoRepository internamentoRepository = mock(InternamentoRepository.class);
        LeitoRepository leitoRepository = mock(LeitoRepository.class);
        ReceitaRepository receitaRepository = mock(ReceitaRepository.class);
        FaturaRepository faturaRepository = mock(FaturaRepository.class);
        MedicamentoRepository medicamentoRepository = mock(MedicamentoRepository.class);

        when(pacienteRepository.count()).thenReturn(120L);
        when(consultaRepository.count()).thenReturn(38L);
        when(medicoRepository.count()).thenReturn(15L);
        when(triagemRepository.count()).thenReturn(27L);
        when(internamentoRepository.findByStatusOrderByDataAdmissaoDesc(Internamento.StatusInternamento.ATIVO)).thenReturn(List.of(
                Internamento.builder().id("i-1").status(Internamento.StatusInternamento.ATIVO).build(),
                Internamento.builder().id("i-2").status(Internamento.StatusInternamento.ATIVO).build()
        ));
        when(leitoRepository.count()).thenReturn(10L);
        when(leitoRepository.findAllByOrderByCodigoAsc()).thenReturn(List.of(
                Leito.builder().id("l-1").status(Leito.StatusLeito.DISPONIVEL).build(),
                Leito.builder().id("l-2").status(Leito.StatusLeito.OCUPADO).build(),
                Leito.builder().id("l-3").status(Leito.StatusLeito.OCUPADO).build(),
                Leito.builder().id("l-4").status(Leito.StatusLeito.OCUPADO).build(),
                Leito.builder().id("l-5").status(Leito.StatusLeito.DISPONIVEL).build(),
                Leito.builder().id("l-6").status(Leito.StatusLeito.DISPONIVEL).build(),
                Leito.builder().id("l-7").status(Leito.StatusLeito.DISPONIVEL).build(),
                Leito.builder().id("l-8").status(Leito.StatusLeito.DISPONIVEL).build(),
                Leito.builder().id("l-9").status(Leito.StatusLeito.DISPONIVEL).build(),
                Leito.builder().id("l-10").status(Leito.StatusLeito.DISPONIVEL).build()
        ));
        when(leitoRepository.findByStatusOrderByCodigoAsc(Leito.StatusLeito.OCUPADO)).thenReturn(List.of(
                Leito.builder().id("l-2").status(Leito.StatusLeito.OCUPADO).build(),
                Leito.builder().id("l-3").status(Leito.StatusLeito.OCUPADO).build(),
                Leito.builder().id("l-4").status(Leito.StatusLeito.OCUPADO).build()
        ));
        when(leitoRepository.findAllByOrderByCodigoAsc()).thenReturn(List.of(
                Leito.builder().id("l-1").status(Leito.StatusLeito.DISPONIVEL).build(),
                Leito.builder().id("l-2").status(Leito.StatusLeito.OCUPADO).build(),
                Leito.builder().id("l-3").status(Leito.StatusLeito.OCUPADO).build(),
                Leito.builder().id("l-4").status(Leito.StatusLeito.OCUPADO).build(),
                Leito.builder().id("l-5").status(Leito.StatusLeito.DISPONIVEL).build(),
                Leito.builder().id("l-6").status(Leito.StatusLeito.DISPONIVEL).build(),
                Leito.builder().id("l-7").status(Leito.StatusLeito.DISPONIVEL).build(),
                Leito.builder().id("l-8").status(Leito.StatusLeito.DISPONIVEL).build(),
                Leito.builder().id("l-9").status(Leito.StatusLeito.DISPONIVEL).build(),
                Leito.builder().id("l-10").status(Leito.StatusLeito.DISPONIVEL).build()
        ));
        when(receitaRepository.findByStatusOrderByDataEmissaoDesc(Receita.StatusReceita.PENDENTE)).thenReturn(List.of(
                Receita.builder().id("r-1").status(Receita.StatusReceita.PENDENTE).build(),
                Receita.builder().id("r-2").status(Receita.StatusReceita.PENDENTE).build()
        ));
        when(faturaRepository.findAllByOrderByDataEmissaoDesc()).thenReturn(List.of(
                Fatura.builder().valorTotal(BigDecimal.valueOf(150.00)).status(Fatura.StatusFatura.PAGA).build(),
                Fatura.builder().valorTotal(BigDecimal.valueOf(300.00)).status(Fatura.StatusFatura.PENDENTE).build(),
                Fatura.builder().valorTotal(BigDecimal.valueOf(50.00)).status(Fatura.StatusFatura.PAGA).build()
        ));
        when(medicamentoRepository.findAll()).thenReturn(List.of(
                Medicamento.builder().id("m-1").quantidade(4).quantidadeMinima(10).build(),
                Medicamento.builder().id("m-2").quantidade(12).quantidadeMinima(10).build()
        ));

        DashboardService dashboardService = new DashboardService(
                pacienteRepository,
                consultaRepository,
                medicoRepository,
                triagemRepository,
                internamentoRepository,
                leitoRepository,
                receitaRepository,
                faturaRepository,
                medicamentoRepository
        );

        DashboardResumo resumo = dashboardService.obterResumo();

        assertNotNull(resumo);
        assertEquals(120L, resumo.getTotalPacientes());
        assertEquals(38L, resumo.getTotalConsultas());
        assertEquals(15L, resumo.getTotalMedicos());
        assertEquals(27L, resumo.getTotalTriagens());
        assertEquals(2L, resumo.getAdmissoesAtivas());
        assertEquals(3L, resumo.getLeitosOcupados());
        assertEquals(7L, resumo.getLeitosDisponiveis());
        assertEquals(10L, resumo.getTotalLeitos());
        assertEquals(2L, resumo.getReceitasPendentes());
        assertEquals(500.00, resumo.getFaturamentoPeriodo().doubleValue(), 0.01);
        assertEquals(1L, resumo.getMedicamentosEstoqueBaixo());
    }

    @Test
    void shouldReturnEmptySummaryWhenNoData() {
        PacienteRepository pacienteRepository = mock(PacienteRepository.class);
        ConsultaRepository consultaRepository = mock(ConsultaRepository.class);
        MedicoRepository medicoRepository = mock(MedicoRepository.class);
        TriagemRepository triagemRepository = mock(TriagemRepository.class);
        InternamentoRepository internamentoRepository = mock(InternamentoRepository.class);
        LeitoRepository leitoRepository = mock(LeitoRepository.class);
        ReceitaRepository receitaRepository = mock(ReceitaRepository.class);
        FaturaRepository faturaRepository = mock(FaturaRepository.class);
        MedicamentoRepository medicamentoRepository = mock(MedicamentoRepository.class);

        when(pacienteRepository.count()).thenReturn(0L);
        when(consultaRepository.count()).thenReturn(0L);
        when(medicoRepository.count()).thenReturn(0L);
        when(triagemRepository.count()).thenReturn(0L);
        when(internamentoRepository.findByStatusOrderByDataAdmissaoDesc(Internamento.StatusInternamento.ATIVO)).thenReturn(List.of());
        when(leitoRepository.findByStatusOrderByCodigoAsc(Leito.StatusLeito.OCUPADO)).thenReturn(List.of());
        when(receitaRepository.findByStatusOrderByDataEmissaoDesc(Receita.StatusReceita.PENDENTE)).thenReturn(List.of());
        when(faturaRepository.findAllByOrderByDataEmissaoDesc()).thenReturn(List.of());
        when(medicamentoRepository.findAll()).thenReturn(List.of());

        DashboardService dashboardService = new DashboardService(
                pacienteRepository,
                consultaRepository,
                medicoRepository,
                triagemRepository,
                internamentoRepository,
                leitoRepository,
                receitaRepository,
                faturaRepository,
                medicamentoRepository
        );

        DashboardResumo resumo = dashboardService.obterResumo();

        assertEquals(0L, resumo.getTotalPacientes());
        assertEquals(0L, resumo.getTotalConsultas());
        assertEquals(0L, resumo.getTotalMedicos());
        assertEquals(0L, resumo.getTotalTriagens());
        assertEquals(0L, resumo.getAdmissoesAtivas());
        assertEquals(0L, resumo.getLeitosOcupados());
        assertEquals(0L, resumo.getLeitosDisponiveis());
        assertEquals(0L, resumo.getTotalLeitos());
        assertEquals(0L, resumo.getReceitasPendentes());
        assertEquals(0.00, resumo.getFaturamentoPeriodo().doubleValue(), 0.01);
        assertEquals(0L, resumo.getMedicamentosEstoqueBaixo());
    }
}
