package com.us.Torres.models.financeiro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaturaRequest {
    private String pacienteId;
    private String descricao;
    private String convenio;
    private BigDecimal valorBruto;
    private LocalDate dataVencimento;
}
