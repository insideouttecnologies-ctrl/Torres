package com.us.Torres.models.farmacia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispensacaoRequest {
    private String receitaId;
    private String farmaceuticoId;
    private String pacienteId;
    private String observacao;
    private List<ItemDispensacao> itens;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDispensacao {
        private String medicamentoId;
        private String medicamentoNome;
        private Integer quantidadeDispensada;
        private String lote;
    }
}
