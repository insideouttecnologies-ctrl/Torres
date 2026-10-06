package com.us.Torres.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResumo {

    private Long totalPacientes;
    private Long totalConsultas;
    private Long totalMedicos;
    private Long totalTriagens;
    private Long admissoesAtivas;
    private Long totalLeitos;
    private Long leitosOcupados;
    private Long leitosDisponiveis;
    private Long receitasPendentes;
    private BigDecimal faturamentoPeriodo;
    private Long medicamentosEstoqueBaixo;
}
