package com.us.Torres.models.farmacia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueMovimentoRequest {
    private String medicamentoId;
    private String tipoMovimento;
    private Integer quantidade;
    private String lote;
    private LocalDate dataValidade;
    private String motivo;
    private String usuarioId;
}
