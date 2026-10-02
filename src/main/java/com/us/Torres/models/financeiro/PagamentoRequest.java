package com.us.Torres.models.financeiro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoRequest {
    private String faturaId;
    private String pacienteId;
    private BigDecimal valorPago;
    private String metodoPagamento;
    private String descricao;
}
